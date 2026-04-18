package com.learn.microservices.trading.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the default security filter chain for application endpoints.
     *
     * <p>This filter chain applies to requests that are not handled by the
     * authorization server filter chain (Order 1). It secures application
     * endpoints while allowing access to certain public endpoints.</p>
     *
     * <p>Key behaviors:</p>
     * <ul>
     *     <li>Allows unauthenticated access to the H2 console.</li>
     *     <li>Allows public access to OAuth2 and OpenID configuration endpoints.</li>
     *     <li>Requires authentication for all other requests.</li>
     *     <li>Disables CSRF protection (commonly done for APIs or development tools).</li>
     *     <li>Enables CORS with default configuration.</li>
     *     <li>Disables frame options to allow the H2 console to render in a browser.</li>
     *     <li>Enables default form-based login.</li>
     * </ul>
     *
     * @param http the HttpSecurity configuration object
     * @return the configured SecurityFilterChain
     * @throws Exception if security configuration fails
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Configure authorization rules for incoming HTTP requests
                .authorizeHttpRequests(auth -> auth

                        // Allow access to H2 database console without authentication
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        // Allow access to OAuth2 and OpenID discovery endpoints
                        .requestMatchers(
                                "/.well-known/**",
                                "/oauth2/**"
                        ).permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Disable CSRF protection (often disabled for APIs or development tools)
                .csrf(csrf -> csrf.disable())

                // Enable Cross-Origin Resource Sharing with default configuration
                .cors(Customizer.withDefaults())

                // Disable frame options to allow H2 console to be displayed in browser frames
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )

                // Enable default Spring Security login form
                .formLogin(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder bcrypt) {

        RegisteredClient registeredClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("angular-client")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("https://127.0.0.1:4200")
                        .scope("openid")
                        .scope("profile")
                        .scope("read")
                        .scope("write")
                        .scope("offline_access")
                        .clientSettings(
                                ClientSettings.builder()
                                        .requireProofKey(true)   // ✅ PKCE here
                                        .requireAuthorizationConsent(false)
                                        .build()
                        )
                        .tokenSettings(
                                TokenSettings.builder()
                                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                                        .refreshTokenTimeToLive(Duration.ofMinutes(30))
                                        .reuseRefreshTokens(false)
                                        .build()
                        )
                        .build();

        RegisteredClient tradeProcessorClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("trade-processor")
                        .clientSecret(bcrypt.encode("secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("refdata.read")
                        .tokenSettings(
                                TokenSettings.builder()
                                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                                        .build()
                        )
                        .build();

        return new InMemoryRegisteredClientRepository(registeredClient, tradeProcessorClient);
    }

}
