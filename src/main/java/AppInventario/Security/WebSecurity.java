package AppInventario.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsServiceImp userDetails;
	
	BCryptPasswordEncoder passwordEncoder;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		passwordEncoder = new BCryptPasswordEncoder(4);
		return passwordEncoder;
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/auth/**","/css/**","/js/**").permitAll().anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/auth/login").defaultSuccessUrl("/user/index",true).failureUrl("/auth/login?error=true").usernameParameter("correo").passwordParameter("password").loginProcessingUrl("/auth/login-post").permitAll()
		.and()
			.logout().logoutUrl("/logout").logoutSuccessUrl("/public/index");
	}

	
	
}