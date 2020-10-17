package com.looseboxes.spring.oauth.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.ApiBinding;
import com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6.FacebookProfileGraphApi;
import com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6.Feed;
import com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6.Post;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import java.util.List;
import org.springframework.web.client.RestTemplate;

/**
 * @author hp
 * @see https://spring.io/blog/2018/03/06/using-spring-security-5-to-integrate-with-oauth-2-secured-services-such-as-facebook-and-github
 * @see https://developers.facebook.com/docs/graph-api/reference/v8.0
 */
public class Facebook extends ApiBinding<FacebookProfileGraphApi> {

    public static final String CLIENT_ID = "facebook";

    public Facebook(String accessToken, String userInfoUri, RestTemplate restTemplate, 
            ObjectMapper objectMapper, ProfileConverterFactory converterFactory) {
        
        super(CLIENT_ID, accessToken, userInfoUri, FacebookProfileGraphApi.class, 
                restTemplate, objectMapper, converterFactory);
    }
    
    public List<Post> getFeed(String url) {
        return getRestTemplate().getForObject(url, Feed.class).getData();
    }
}