package dev.rr.moduleselectorapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/secured/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
            }
            .formLogin { form ->
                form
                    .defaultSuccessUrl("/secured/dashboard")
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/")
                    .permitAll()
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val userDetailsManager = InMemoryUserDetailsManager()

        val admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("" +
                    ""))
            .roles("ADMIN")
            .build()

        val aboba = User.builder()
            .username("aboba")
            .password(passwordEncoder().encode("aboba-sec"))
            .roles("ADMIN")
            .build()


        userDetailsManager.createUser(admin)
        userDetailsManager.createUser(aboba)

        return userDetailsManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}