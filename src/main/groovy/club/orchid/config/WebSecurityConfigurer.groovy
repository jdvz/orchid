package club.orchid.config

import club.orchid.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:26 PM
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AutoConfigureAfter(JdbcConfigurer.class)
class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    IUserService userService
    @Autowired
    SessionRegistry sessionRegistry
    @Autowired
    PasswordEncoder passwordEncoder
    @Autowired(required = false)
    AuthenticationEntryPoint authenticationEntryPoint
    @Autowired(required = false)
    AccessDeniedHandler accessDeniedHandler

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers('/resources/**').permitAll()
//                .anyRequest().anonymous()
                .antMatchers('/admin/**').hasRole('ADMIN')
                .antMatchers('/club/**').hasAnyRole('ADMIN', 'CUSTOMER', 'MANAGER')
                .and()
            .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl('/login?expired')
                .sessionRegistry(sessionRegistry)
                .and().and()
//            .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler)
//                .and()
            .formLogin()
                .loginPage('/login')
                .failureUrl('/login?error')
                .usernameParameter('email')
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .deleteCookies('remember-me')
                .logoutSuccessUrl('/')
                .and()
            .headers()
                .frameOptions().disable()
                .and()
//                .csrf().disable()
            .rememberMe()
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder()
    }

    @Bean(name = 'sessionRegistry')
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
//                .getDefaultUserDetailsService()
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
    }
}