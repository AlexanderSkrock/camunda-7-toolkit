package dev.skrock.camunda.toolkit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import dev.skrock.camunda.toolkit.ui.LoginView;
import dev.skrock.camunda.toolkit.ui.Roles;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll();
        });

        super.configure(http);

        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password("{noop}user")
                .roles(Roles.ANALYZE_ROLE, Roles.DATA_TRANSFER_ROLE, Roles.REST_ROLE, Roles.DATA_EXPERTS_ROLE)
                .build();
        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}admin")
                .roles(Roles.ADMIN_ROLE, Roles.ANALYZE_ROLE, Roles.DATA_TRANSFER_ROLE, Roles.REST_ROLE, Roles.DATA_EXPERTS_ROLE)
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
