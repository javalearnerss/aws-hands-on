package com.learn.microservices.trading.auth.server.config;

import com.learn.microservices.trading.auth.server.entity.TradingUserDetails;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

@Configuration
public class JwtCustomizerConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {

        return context -> {

            // Only customize ACCESS TOKEN
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                return;
            }

            Authentication authentication = context.getPrincipal();

            // 🔥 CASE 1: CLIENT CREDENTIALS (service-to-service)
            if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {

                String clientId = context.getRegisteredClient().getClientId();

                context.getClaims().subject(clientId);
                context.getClaims().claim("client_id", clientId);

                // scopes come from registered client
                context.getClaims().claim("scope", context.getAuthorizedScopes());

                // Check which client is requesting token
                if ("trade-processor".equals(context.getRegisteredClient().getClientId())) {
                    context.getClaims().audience(List.of("refdata-provider"));
                }

                return;
            }

            // 🔥 CASE 2: USER FLOW (authorization_code, password, etc.)
            Object principal = authentication.getPrincipal();

            if (principal instanceof TradingUserDetails user) {

                context.getClaims().subject(user.getUsername());
                context.getClaims().claim("username", user.getUsername());
                context.getClaims().claim("email", user.getEmail());
                context.getClaims().claim("roles", user.getRoles());
                context.getClaims().claim("tenant_id", user.getTenantId());

                // audience
                context.getClaims().audience(user.getAudience());

                // scope
                context.getClaims().claim("scope", user.getScope());
            }
        };
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}