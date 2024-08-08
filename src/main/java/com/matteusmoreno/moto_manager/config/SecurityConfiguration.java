package com.matteusmoreno.moto_manager.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/customers/create").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/customers/find-all").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/customers/update").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/customers/disable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/customers/enable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/customers/add-motorcycle").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/customers/remove-motorcycle").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")

                        .requestMatchers("/employees/create").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")
                        .requestMatchers("/employees/find-all").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/employees/update").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")
                        .requestMatchers("/employees/disable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")
                        .requestMatchers("/employees/enable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")

                        .requestMatchers("/motorcycles/create").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/motorcycles/find-all").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/motorcycles/update").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/motorcycles/disable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/motorcycles/enable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")

                        .requestMatchers("/products/create").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/products/find-all").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/products/update").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/products/enable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/products/disable/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/products/add-product").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")
                        .requestMatchers("/products/remove-product").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER")

                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
