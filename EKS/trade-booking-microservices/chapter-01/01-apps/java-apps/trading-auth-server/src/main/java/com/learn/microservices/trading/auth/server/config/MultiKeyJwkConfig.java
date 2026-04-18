package com.learn.microservices.trading.auth.server.config;

import com.learn.microservices.trading.auth.server.util.KeyRuntimeState;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration that provides a dynamic JWK source for the Authorization Server.
 *
 * <p>This configuration allows the system to support multiple signing keys
 * (for key rotation) while ensuring that only the currently active key is used
 * for signing new JWT tokens.</p>
 *
 * <p>The {@link JWKSource} retrieves keys from {@link KeyRuntimeState}, which
 * maintains the current JWK set and the active key ID (kid).</p>
 *
 * <p>Behavior:</p>
 * <ul>
 *   <li>All keys remain available for verification.</li>
 *   <li>Only the active key is used for signing new tokens.</li>
 * </ul>
 */
@Configuration
public class MultiKeyJwkConfig {

    /**
     * Provides a JWKSource used by the authorization server when creating
     * or validating JWT tokens.
     *
     * @param keyRuntimeState runtime state containing loaded keys and the active key id
     * @return JWKSource used by the authorization server
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyRuntimeState keyRuntimeState) {

        return (selector, context) -> {

            // Retrieve the full set of JWK keys currently loaded in memory
            JWKSet jwkSet = keyRuntimeState.getJwkSet();

            // Get the key ID of the currently active signing key
            String activeKid = keyRuntimeState.getActiveKid();

            // Select keys that match the selector criteria
            List<JWK> selected = selector.select(jwkSet);

            // Retrieve matcher information to determine what type of key request is being made
            JWKMatcher matcher = selector.getMatcher();

            // ===== SIGNING REQUEST =====
            // If the request is for a signing key, return only the active key
            if (matcher != null
                    && matcher.getKeyUses() != null
                    && matcher.getKeyUses().contains(KeyUse.SIGNATURE)) {

                return selected.stream()
                        .filter(k -> activeKid.equals(k.getKeyID()))
                        .toList();
            }

            // For verification or other operations, return all matching keys
            return selected;
        };
    }
}