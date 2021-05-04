package lk.wasity_institute.configuration;

import lk.wasity_institute.asset.user_management.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final String[] ALL_PERMIT_URL = {"/favicon.ico", "/img/**", "/css/**", "/js/**", "/webjars/**",
      "/login", "/select/**", "/", "/index"};

  @Bean
  public UserDetailsServiceImpl userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /*Session management - bean start*/
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
  /*Session management - bean end*/

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return new CustomAuthenticationSuccessHandler();
  }

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//   http.csrf().disable();
//    http.authorizeRequests().antMatchers("/").permitAll();


    http
        .authorizeRequests(
            authorizeRequests ->
                authorizeRequests
                    //Anytime users can access without login
                    //to see actuator details
                    .antMatchers(ALL_PERMIT_URL).permitAll()
                    //this is used the normal admin to give access every url mapping
                    .antMatchers("/batch/**").hasAnyRole( "MANAGER","TEACHER")
                        .antMatchers("/batchExam/**").hasAnyRole( "MANAGER","TEACHER","STUDENT")
                        .antMatchers("/batchStudent/**").hasAnyRole( "MANAGER","TEACHER","RECEPTIONIST")
                        .antMatchers("/batchStudentExamResult/**").hasAnyRole( "MANAGER","TEACHER")
                        .antMatchers("/employee/**").hasAnyRole("ADMIN")
                        .antMatchers("/hall/**").hasAnyRole( "MANAGER")
                        .antMatchers("/payment/**").hasAnyRole( "MANAGER","RECEPTIONIST","STUDENT")
                        .antMatchers("/report/**").hasAnyRole( "MANAGER","RECEPTIONIST")
                        .antMatchers("/role/**").hasAnyRole("ADMIN")
                        .antMatchers("/school/**").hasAnyRole( "MANAGER","RECEPTIONIST")
                        .antMatchers("/student/**").hasAnyRole( "MANAGER","RECEPTIONIST")
                        .antMatchers("/subject/**").hasAnyRole( "MANAGER","RECEPTIONIST")
                        .antMatchers("/teacher/**").hasAnyRole( "MANAGER","RECEPTIONIST")
                        .antMatchers("/timeTable/**").hasAnyRole( "MANAGER","TEACHER","STUDENT","RECEPTIONIST")
                        .antMatchers("/timeTableStudentAttendance/**").hasAnyRole( "MANAGER","TEACHER","STUDENT")
                        .antMatchers("/user/**").hasAnyRole("ADMIN")

//todo: security configuration
                    .anyRequest()
                    .authenticated())
        // Login form
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    //Username and password for validation
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureUrl("/login?error")
                  )
        //Logout controlling
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler())
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true))
        //session management
        .sessionManagement(
            sessionManagement ->
                sessionManagement
                    .sessionFixation().migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry()))
        //Cross site disable
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling();

  }
}

