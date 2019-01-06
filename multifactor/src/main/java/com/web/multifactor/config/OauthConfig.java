package com.web.multifactor.config;

import com.web.multifactor.oauth.CustomOAuth2Provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.web.multifactor.oauth.SocialType.*;

@Configuration
@EnableWebSecurity
public class OauthConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
            .authorizeRequests()
                .antMatchers("/", "/oauth2/**", "/login/**",  "/css/**", "/images/**", "/js/**", "/console/**").permitAll()
                .antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())//기본권한
                .antMatchers("/kakao_auth").hasAuthority(KAKAO_AUTH.getRoleType())//관리자승인권한
                .anyRequest().authenticated()
                
            .and()
                .oauth2Login()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
            .and()
                .headers().frameOptions().disable()
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and()
                .formLogin()
                .successForwardUrl("/home")
            .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/logoutCallback")
//                .logoutSuccessUrl("/login")
                .permitAll()
            .and()
//                .addFilterBefore(filter, CsrfFilter.class)
                .csrf().disable();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties, @Value("${custom.oauth2.kakao.client-id}") String kakaoClientId) {
        List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(kakaoClientId)
                .clientSecret("test") //필요없는 값인데 null이면 실행이 안되도록 설정되어 있음
                .jwkSetUri("test") //필요없는 값인데 null이면 실행이 안되도록 설정되어 있음
                .build());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
         return null;
    }
}