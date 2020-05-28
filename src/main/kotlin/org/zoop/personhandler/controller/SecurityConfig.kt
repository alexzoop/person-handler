package org.zoop.personhandler.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index", "/personList", "/css/**").permitAll()
                .antMatchers(
                        "/addPerson", "/addHobby", "/hobbyList", "/delete", "/deleteHobby", "/upload").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
    }

    override fun configure(@Autowired auth: AuthenticationManagerBuilder?) {
        auth!!.inMemoryAuthentication()
                .withUser("zoop").password("{noop}admin").roles("ADMIN")
    }
}