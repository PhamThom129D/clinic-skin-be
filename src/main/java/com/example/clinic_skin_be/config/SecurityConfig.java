
package com.example.clinic_skin_be.config;

import com.example.clinic_skin_be.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                // Các API public
                                .requestMatchers(
                                        "/api/auth/**",
//                                "/api/chat/**",
                                        "/api/contacts/**",
                                        "/api/appointments/**",
                                        "/api/doctors/**",
                                        "/api/screen-dashboard/**",
                                        "/api/ai-suggest/**",
                                        "/ws-chat/**",
                                        "/api/medical-records/**",
                                        "/api/visit-sessions/**",
                                        "/api/accounts/**"
                                ).permitAll()

                                .requestMatchers("/api/chat/**").permitAll()
                                .requestMatchers("/api/chat/reply/**").hasAnyRole("ADMIN", "CONSULTANT", "PATIENT")

                                // Các API cho bác sĩ
//                                .requestMatchers("/api/medical-records/**").hasRole("DOCTOR")
//                                .requestMatchers("/api/visit-sessions/**").hasRole("DOCTOR")

                                // Các API public khác
                                .requestMatchers(
                                        "/api/treatment-items/**",
                                        "/api/treatment-templates/**",
                                        "/api/treatment-plans/**"
                                ).permitAll()

                                // Các request còn lại phải authenticated
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .cors(withDefaults())
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**", "/api/chat/**", "/api/contacts/**", "/api/appointments/**",
//                                "/api/doctors/**", "/api/screen-dashboard/**", "/api/ai-suggest/**").permitAll()
//
//
//                        .requestMatchers("/api/admin/**").hasAuthority("MANAGE_ROLES") // dynamic
//        //                .requestMatchers("/api/medical-records/**").permitAll()
//         //               .requestMatchers("/api/visit-sessions/**").permitAll()
//                        .requestMatchers("/api/medical-records/**").hasRole("DOCTOR")
//                        .requestMatchers("/api/visit-sessions/**").hasRole("DOCTOR")
//                        .requestMatchers("/api/treatment-items/**", "/api/treatment-templates/**", "/api/treatment-plans/**").permitAll()
//
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
