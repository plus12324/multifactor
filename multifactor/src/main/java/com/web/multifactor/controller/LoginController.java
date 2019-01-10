package com.web.multifactor.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.multifactor.model.User;
import com.web.multifactor.oauth.annotation.*;
/**
 * Created by KimYJ on 2017-09-13.
 */
@Controller
public class LoginController {
	
	@Autowired
	private OAuth2AuthorizedClientService clientService;

    @GetMapping("/login")
    public String login() {
//    	System.out.println("■ 로그인페이지 이동");
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginComplete(@SocialUser User user) {//@SocialUser User user
    	System.out.println("■ 로그인 성공 : " + user.toString());
        return "home";
    }
    
    @GetMapping("/unlink")
    public String unlink() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(!(auth instanceof AnonymousAuthenticationToken)) {
        	String RequestUrl = "https://kapi.kakao.com/v1/user/unlink";    	 
        	
            final HttpClient client = HttpClientBuilder.create().build();        
            final HttpPost post = new HttpPost(RequestUrl);
            
	        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
	        
	        OAuth2AuthorizedClient OAuth2AuthorizedClient = clientService.loadAuthorizedClient(
	        	    		authentication.getAuthorizedClientRegistrationId(),
	        	    		authentication.getName());
	        	
	        String accessToken = OAuth2AuthorizedClient.getAccessToken().getTokenValue();
	     
	        post.addHeader("Authorization", "Bearer " + accessToken);
	        JsonNode returnNode = null;
	 
	        try {
	            final HttpResponse response = client.execute(post); 
	            ObjectMapper mapper = new ObjectMapper(); 
	            returnNode = mapper.readTree(response.getEntity().getContent()); 
	            System.out.println("■■■■■■ returnNode : " + returnNode);
	        } catch (UnsupportedEncodingException e) { 
	            e.printStackTrace(); 
	        } catch (ClientProtocolException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        }
	        
	        return "redirect:/logout";
        }
        
        return "login";
    }
}

