package com.example.demo.config;

import com.example.demo.config.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.config.jwt.JwtRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * exemplo de uso: curl http://localhost:8080/api -u ben:benspassword curl -X
 * POST localhost:8080/api -H "Content-type:application/json" -d
 * {\"name\":\"ABC\",\"author\":\"mkyong\",\"price\":\"8.88\"} -u
 * ben:benspassword
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  // @Autowired
  // private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  // @Autowired
  // private JwtRequestFilter jwtRequestFilter;

  // @Override
  // protected void configure(HttpSecurity httpSecurity) throws Exception {
  //   httpSecurity.csrf().disable()
  //       // Não cheque essas requisições
  //       .authorizeRequests().antMatchers("/authenticate", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
  //           "/configuration/**", "/swagger-ui.html", "/webjars/**")
  //       .permitAll().
  //       // Qualquer outra requisição deve ser checada
  //       anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
  //       .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  //   httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  // }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().fullyAuthenticated()
        // .and()
        // .authorizeRequests()
        // .antMatchers(HttpMethod.GET, "/api").hasRole("developers")
        // .antMatchers(HttpMethod.POST, "/api").hasRole("ADMIN")
        .and().
        formLogin().disable()
        ;

  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource()
        .url("ldap://localhost:8389/dc=springframework,dc=org")
        // .url("ldap://localhost:8389/dc=springframework,dc=org")
        .and().passwordCompare()
        // .passwordEncoder(new BCryptPasswordEncoder())
        .passwordAttribute("userPassword");
  }

  // @Override
  // protected void configure(AuthenticationManagerBuilder auth) throws Exception
  // {

  // auth.inMemoryAuthentication()
  // .withUser("ben").password("{noop}benspassword").roles("USER")
  // .and()
  // .withUser("adm").password("{noop}adm").roles("USER", "ADMIN");

  // }

  // @Override
  // protected void configure(HttpSecurity http) throws Exception {

  // http
  // //HTTP Basic authentication
  // .httpBasic()
  // .and()
  // .authorizeRequests()
  // .antMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
  // .antMatchers(HttpMethod.POST, "/api").hasRole("ADMIN")
  // // .antMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
  // // .antMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
  // // .antMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
  // .and()
  // .csrf().disable()
  // .formLogin().disable();
  // }
}