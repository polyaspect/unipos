package unipos.auth.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import unipos.auth.components.authentication.NullUserDetailsService;
import unipos.auth.components.authentication.SessionIdentifierGenerator;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinAuthenticationFilter;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinAuthenticationProvider;
import unipos.auth.components.user.usernamePassword.CustomUsernamePasswordAuthenticationFilter;
import unipos.auth.components.user.usernamePassword.UsernamePasswordAuthenticationProvider;
import unipos.auth.components.user.usernamePassword.UsernamePasswordService;
import unipos.auth.config.web.security.MySavedRequestAwareAuthenticationSuccessHandler;
import unipos.auth.config.web.security.UniposLogoutHandler;

/**
* @author Dominik
*/
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UsernamePasswordService usernamePasswordService;

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    MitarbeiteridPinAuthenticationProvider mitarbeiteridPinAuthenticationProvider;

    @Autowired
    UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;





    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new NullUserDetailsService());//.passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authenticationProvider(mitarbeiteridPinAuthenticationProvider)
            .authenticationProvider(usernamePasswordAuthenticationProvider)
            .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
            .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
            .addFilterBefore(mitarbeiteridPinAuthenticationFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(customUsernamePasswordAuthenticationFilter(), MitarbeiteridPinAuthenticationFilter.class)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .sessionFixation().none()
                .and()
            .logout()
                .logoutUrl("/auth/logout")
                .deleteCookies("AuthToken")
                .logoutSuccessUrl("/auth/account_login")
                .logoutSuccessHandler(uniposLogoutHandler())
                .and()
            .csrf().disable();
    }


    @Bean
    public MySavedRequestAwareAuthenticationSuccessHandler mySavedRequestAwareAuthenticationSuccessHandler() {
        return new MySavedRequestAwareAuthenticationSuccessHandler();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public SessionIdentifierGenerator sessionIdentifierGenerator() {
        return new SessionIdentifierGenerator();
    }

    @Bean UniposLogoutHandler uniposLogoutHandler() {
        return new UniposLogoutHandler();
    }

    @Bean(name="myAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public MitarbeiteridPinAuthenticationFilter mitarbeiteridPinAuthenticationFilter() throws Exception {
        MitarbeiteridPinAuthenticationFilter mitarbeiteridPinAuthenticationFilter = new MitarbeiteridPinAuthenticationFilter();
        mitarbeiteridPinAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        mitarbeiteridPinAuthenticationFilter.setAuthenticationSuccessHandler(mySavedRequestAwareAuthenticationSuccessHandler());
        mitarbeiteridPinAuthenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());

        return mitarbeiteridPinAuthenticationFilter;
    }

    @Bean
    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(mySavedRequestAwareAuthenticationSuccessHandler());
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());

        return usernamePasswordAuthenticationFilter;
    }
}
