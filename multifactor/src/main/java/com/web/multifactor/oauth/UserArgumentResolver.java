package com.web.multifactor.oauth;

import com.web.multifactor.oauth.annotation.SocialUser;
import com.web.multifactor.repository.jpa.UserRepository;
import com.web.multifactor.model.User;
import com.web.multifactor.oauth.SocialType;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import static com.web.multifactor.oauth.SocialType.KAKAO;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private UserRepository userRepository;

    public UserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class) != null 
        	&& parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        return getUser(user, session);
    }

    private User getUser(User user, HttpSession session) {
        if(user == null) {
            try {
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                User convertUser = getKaKaoUser(map);

                user = userRepository.findByEmail(convertUser.getEmail());
                if (user == null) { user = userRepository.save(convertUser); }

                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
                return user;
            }
        }
        return user;
    }

    private User getKaKaoUser(Map<String, Object> map) {
        Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(String.valueOf(map.get("kaccount_email")))
                .pincipal(String.valueOf(map.get("id")))
                .socialtype(KAKAO)
                .createddate(LocalDateTime.now())
                .build();
    }
    
    private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
        if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialtype().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getSocialtype().getRoleType())));
        }
    }
    
    private User convertUser(String authority, Map<String, Object> map) {
//        if(FACEBOOK.isEquals(authority)) return getModernUser(FACEBOOK, map);
//        else if(GOOGLE.isEquals(authority)) return getModernUser(GOOGLE, map);
//        else if(KAKAO.isEquals(authority)) return getKaKaoUser(map);
        return getKaKaoUser(map);
    }

//    private User getModernUser(SocialType socialType, Map<String, Object> map) {
//        return User.builder()
//                .name(String.valueOf(map.get("name")))
//                .email(String.valueOf(map.get("email")))
//                .pincipal(String.valueOf(map.get("id")))
//                .socialtype(socialType)
//                .createddate(LocalDateTime.now())
//                .build();
//    }
    
}