package com.learn.microservices.trading.auth.server.util;

import com.learn.microservices.trading.auth.server.repository.KeyConfigRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
@Setter
@Getter
public class KeyRuntimeState {

    @Value("${internal.cert.folder.path}")
    private String keyFolder;

    @Value("${key.store.password}")
    private String keyStorePassword;

    private final KeyConfigRepository repo;

    // ALL keys (used by /oauth2/jwks)
    private volatile JWKSet jwkSet;

    // ONE active signing key
    private volatile String activeKid;

    // folder fingerprint
    private volatile String lastFolderHash = "";

    public KeyRuntimeState(KeyConfigRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void init() throws Exception {
        refreshAll();
    }

    // ===== AUTO RELOAD =====
    @Scheduled(fixedDelay = 10000)
    public synchronized void refreshAll() throws Exception {

        String currentHash = calculateFolderHash();

        // Folder unchanged → only refresh DB active key
        if (currentHash.equals(lastFolderHash)) {
            refreshActiveKidOnly();
            return;
        }

        System.out.println("Key folder changed, reloading keys...");
        List<JWK> keys = loadAllKeys();
        if (keys.isEmpty()) {
            throw new IllegalStateException("No .p12 keys found in " + keyFolder);
        }

        String dbActiveKid = repo.findActiveKid();
        if (dbActiveKid == null) {
            throw new IllegalStateException("Active kid missing in DB");
        }

        boolean exists = keys.stream().anyMatch(k -> dbActiveKid.equals(k.getKeyID()));
        if (!exists) {
            throw new IllegalStateException("Active key " + dbActiveKid + " not found in folder");
        }

        // Atomic swap (safe for concurrent reads)
        this.jwkSet = new JWKSet(keys);
        this.activeKid = dbActiveKid;
        this.lastFolderHash = currentHash;

        System.out.println("Keys reloaded. Active key = " + activeKid);
    }

    /**
     * Refreshes the active key ID (kid) from the database.
     * Updates the in-memory activeKid only if the database value is
     * not null and different from the current activeKid.
     */
    private void refreshActiveKidOnly() {
        String dbKid = repo.findActiveKid();
        if (dbKid != null && !dbKid.equals(activeKid)){
            this.activeKid = dbKid;
        }
    }

    /**
     * Generates a hash representing the current state of the key folder.
     * The hash is calculated using the names and last modified timestamps
     * of all .p12 files in the folder.
     *
     * @return hexadecimal hash of the folder contents
     * @throws Exception if the directory cannot be read
     */
    private String calculateFolderHash() throws Exception {

        StringBuilder sb = new StringBuilder();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(keyFolder), "*.p12")) {
            for (Path path : stream) {
                sb.append(path.getFileName());
                sb.append(Files.getLastModifiedTime(path));
            }
        }

        return Integer.toHexString(sb.toString().hashCode());
    }

    /**
     * Loads all RSA keys from PKCS12 (.p12) files located in the configured key folder.
     *
     * Each .p12 file is expected to contain an RSA key pair. The file name (without
     * extension) is used as the key ID (kid). The method reads the private and public
     * keys from the keystore and converts them into JWK objects.
     *
     * @return list of JWK keys loaded from the key folder
     * @throws Exception if there is an error reading the keystore files
     */
    private List<JWK> loadAllKeys() throws Exception {

        // List to store all loaded JWK keys
        List<JWK> keys = new ArrayList<>();

        // Iterate through all .p12 files inside the key folder
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(keyFolder), "*.p12")) {

            for (Path path : stream) {

                // Extract the key ID (kid) from the file name by removing the .p12 extension
                String kid = path.getFileName()
                        .toString()
                        .replace(".p12", "");

                // Create a PKCS12 keystore instance
                KeyStore keyStore = KeyStore.getInstance("PKCS12");

                // Load the keystore from the file using the configured password
                try (InputStream is = Files.newInputStream(path)) {
                    keyStore.load(is, keyStorePassword.toCharArray());
                }

                // Retrieve all aliases from the keystore
                Enumeration<String> aliases = keyStore.aliases();

                // Skip the file if the keystore does not contain any entries
                if (!aliases.hasMoreElements()) {
                    continue;
                }

                // Get the first alias from the keystore
                String alias = aliases.nextElement();

                // Retrieve the RSA private key associated with the alias
                RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());

                // Retrieve the certificate associated with the alias
                Certificate cert = keyStore.getCertificate(alias);

                // Extract the RSA public key from the certificate
                RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

                // Build an RSA JWK using the public key, private key, and key ID
                RSAKey rsaKey = new RSAKey.Builder(publicKey)
                        .privateKey(privateKey)
                        .keyID(kid)
                        .build();

                // Add the generated JWK to the list
                keys.add(rsaKey);
            }
        }

        // Return all loaded JWK keys
        return keys;
    }
}