/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looseboxes.spring.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author hp
 */
public interface OAuth2ApiBinding<P> {

    OAuth2Profile<P> fetchOAuthProfile(OAuth2User user) throws JsonProcessingException;

    String getClientId();

    RestTemplate getRestTemplate();
    
}
