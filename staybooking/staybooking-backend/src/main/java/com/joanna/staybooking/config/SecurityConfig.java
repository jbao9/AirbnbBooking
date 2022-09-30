package com.joanna.staybooking.config;

import com.joanna.staybooking.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;


@EnableWebSecurity  //让Spring create security框架
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtFilter jwtFilter;

    //让spring 帮我们创建object. 程序启动时，就开始自动运行创建object
    // 需要让Spring创建object,是因为BCryptPasswordEncoder class是Spring的class
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //config成需要用户登陆后才能访问
        http
                .authorizeRequests() //说明我对权限有控制
                .antMatchers(HttpMethod.POST, "/register/*").permitAll()  //permitAll():不需要验证用户登陆状态
                .antMatchers(HttpMethod.POST, "/authenticate/*").permitAll()
                .antMatchers("/stays").hasAuthority("ROLE_HOST")
                .antMatchers("/stays/*").hasAuthority("ROLE_HOST")
                .antMatchers("/search").hasAuthority("ROLE_GUEST")
                .antMatchers("/reservations").hasAuthority("ROLE_GUEST")
                .antMatchers("/reservations/*").hasAuthority("ROLE_GUEST")
//                .antMatchers("/stays").permitAll()  //为了测试，后期要删掉
//                .antMatchers("/stays/*").permitAll() //为了测试，后期要删掉
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable();
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //先通过jwtFilter验证，再做username和pw验证 (add jwtFilter before Spring frame filter)
    }

    //验证用户名、密码是否正确
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //config成验证authentication（验证用户名、密码）,告诉框架在哪里找
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?") //？是占位符 placeholder
                .authoritiesByUsernameQuery("SELECT username, authority FROM authority WHERE username = ?");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {  //做用户名和密码验证的对象
        return super.authenticationManagerBean();
    }
}

//Authentication vs. Authorization    验证 vs. 授权
// 先 Authentication 再 Authorization