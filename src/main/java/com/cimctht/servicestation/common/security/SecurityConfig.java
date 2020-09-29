package com.cimctht.servicestation.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig   extends WebSecurityConfigurerAdapter {
    /**
     * 重写PasswordEncoder  接口中的方法，实例化加密策略
     * @return 返回 BCrypt 加密策略
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private WebSecurityUserService webSecurityUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.webSecurityUserService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and().authorizeRequests() // 通过authorizeRequests()定义哪些URL需要被保护、哪些不需要被保护。
                .antMatchers("/layui/**","/assets/**","/login","/error").permitAll() // /login不需要任何认证就可以访问。
                .anyRequest().authenticated() // 其他的路径都必须通过身份验证
                .and().formLogin() // 当需要用户登录时候
                .loginPage("/login")// 转到的登录页面
                .loginProcessingUrl("/authentication")//身份认证的url
                .defaultSuccessUrl("/index")
                .failureUrl("/login?error=1")
                .permitAll()
                .and().logout().permitAll();
        http.csrf().disable();//同源限制取消
        http.sessionManagement().maximumSessions(1).expiredUrl("/login");// 防止多处登录
        http.headers().frameOptions().disable();//禁止iframe嵌套,关闭
    }

}
