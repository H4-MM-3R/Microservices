package com.aromablossom.CloudGateway.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurity {
    
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http){
        http.authorizeExchange((exchanges) -> exchanges.anyExchange().authenticated())
            .oauth2Login(Customizer.withDefaults())
            .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
