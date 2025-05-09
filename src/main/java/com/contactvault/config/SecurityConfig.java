package com.contactvault.config;


import com.contactvault.services.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {

   // this inMemory password encoding is not for production it is only used for testing purposes.
    // Password encoder bean
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //user creation and login using java code within memory service.
//    @Bean
//    public UserDetailsService userDetailService(){
//
//        UserDetails userDetails1 = User
//                .withUsername("admin123")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN","USER")
//                .build();
//
//        UserDetails userDetails2 = User.withUsername("user123")
//                .password(passwordEncoder().encode("password"))
//                .build();
//
//        var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(userDetails1, userDetails2);
//        return inMemoryUserDetailsManager;
//}

    private final SecurityCustomUserDetailService userDetailService;
    private final OAuthAuthenticationSuccessHandler handler;
    @Autowired
    public SecurityConfig(SecurityCustomUserDetailService userDetailService, OAuthAuthenticationSuccessHandler handler){
        this.handler=handler;
        this.userDetailService=userDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider  authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //user detail service object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        //password encoder to object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //configured

        //Configure URLs which are public and which are private
        httpSecurity.authorizeHttpRequests(authorize->{
            //authorize.requestMatchers("/home","/register").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            //public URLs.
            authorize.anyRequest().permitAll();
        });

        //form login default
        //form related changes to be made over here.
        httpSecurity.formLogin(httpSecurityFormLoginConfigurer ->{

            httpSecurityFormLoginConfigurer.loginPage("/login");
            httpSecurityFormLoginConfigurer.loginProcessingUrl("/authenticate");
            httpSecurityFormLoginConfigurer.defaultSuccessUrl("/user/profile");
            //httpSecurityFormLoginConfigurer.failureForwardUrl("/login?error=true");
            httpSecurityFormLoginConfigurer.usernameParameter("email");
            httpSecurityFormLoginConfigurer.passwordParameter("password");


            //if the authentication fails
//            httpSecurityFormLoginConfigurer.failureHandler(new AuthenticationFailureHandler() {
//                        @Override
//                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//                        }
//                    });
//
//            //if the authentication succeeds.
//            httpSecurityFormLoginConfigurer.successHandler(new AuthenticationSuccessHandler() {
//                @Override
//                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//                }
//            })

        });

        //Oauth Configurations
        httpSecurity.oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
            httpSecurityOAuth2LoginConfigurer.loginPage("/login");
            httpSecurityOAuth2LoginConfigurer.successHandler(handler);

        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });



        return httpSecurity.build();

    }
}
