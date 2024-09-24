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

    private static final String ADMIN = "SCOPE_ADMIN";
    private static final String MANAGER = "SCOPE_MANAGER";
    private static final String SELLER = "SCOPE_SELLER";
    private static final String MECHANIC = "SCOPE_MECHANIC";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/customers/create").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/customers/find-all").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/customers/update").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/customers/disable/**").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/customers/enable/**").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/customers/add-motorcycle").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/customers/remove-motorcycle").hasAnyAuthority(ADMIN, MANAGER, SELLER)

                        .requestMatchers("/employees/create").permitAll()      //hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")
                        .requestMatchers("/employees/find-all").permitAll()       //.hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER", "SCOPE_SELLER", "SCOPE_MECHANIC")
                        .requestMatchers("/employees/update").permitAll()        //.hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")
                        .requestMatchers("/employees/disable/**").permitAll()    //.hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")
                        .requestMatchers("/employees/enable/**").permitAll()      //.hasAnyAuthority("SCOPE_ADMIN", "SCOPE_MANAGER")

                        .requestMatchers("/motorcycles/create").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/motorcycles/find-all").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/motorcycles/update").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/motorcycles/disable/**").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/motorcycles/enable/**").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)

                        .requestMatchers("/products/create").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/products/find-all").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/products/update").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/products/enable/**").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/products/disable/**").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/products/add-product").hasAnyAuthority(ADMIN, MANAGER, SELLER)
                        .requestMatchers("/products/remove-product").hasAnyAuthority(ADMIN, MANAGER, SELLER)

                        .requestMatchers("/service-orders/create").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/service-orders/start/**").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/service-orders/complete/**").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/service-orders/cancel/**").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)
                        .requestMatchers("/service-orders/update").hasAnyAuthority(ADMIN, MANAGER, SELLER, MECHANIC)

                        .requestMatchers("/suppliers/create").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/suppliers/find-all").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/suppliers/update").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/suppliers/disable/**").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/suppliers/enable/**").hasAnyAuthority(ADMIN, MANAGER)

                        .requestMatchers("/payables/create").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/payables/find-all").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/payables/pay/**").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/payables/cancel/**").hasAnyAuthority(ADMIN, MANAGER)

                        .requestMatchers("/receivables/find-all").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/receivables/pay/**").hasAnyAuthority(ADMIN, MANAGER)

                        .requestMatchers("/finance/weekly-report/download").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/finance/monthly-report").hasAnyAuthority(ADMIN, MANAGER)
                        .requestMatchers("/finance/yearly-report").hasAnyAuthority(ADMIN, MANAGER)

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
