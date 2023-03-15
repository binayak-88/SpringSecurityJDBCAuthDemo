/**
 * 
 */
package edu.binayak.spring.springsecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author HP
 *
 */
@EnableWebSecurity
@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
		@Autowired
		private BCryptPasswordEncoder passwordEncoder;
		
		@Autowired
		private DataSource dataSource;
		
		private static final String QUERY_1 = "SELECT UNAME,PASSWORD,UENABLED FROM USERS_TABLE WHERE UNAME=?";
		private static final String QUERY_2 = "SELECT UNAME,ROLE FROM USERS_TABLE WHERE UNAME=?";
	// Authorization
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery(QUERY_1)
			.authoritiesByUsernameQuery(QUERY_2)
			.passwordEncoder(passwordEncoder);
		}
		
		// Authentication
		protected void configure(HttpSecurity http) throws Exception {
			// access
			http.authorizeHttpRequests()
			.antMatchers("/home")
			.permitAll()
			.antMatchers("/welcome")
			.authenticated()
			.antMatchers("/admin")
			.hasAnyAuthority("ADMIN")
			.antMatchers("/emp")
			.hasAnyAuthority("EMPOYEE")
			.antMatchers("/student").hasAnyAuthority("STUDENT")
			
			//Login Forms
			.and()
			.formLogin().defaultSuccessUrl("/welcome", true)
			
			//logout
			
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			
			// Exception Handling
			
			.and()
			.exceptionHandling().accessDeniedPage("/denied");
			
		}
}