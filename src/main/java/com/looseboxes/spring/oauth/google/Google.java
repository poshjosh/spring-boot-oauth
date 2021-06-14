package com.looseboxes.spring.oauth.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.ApiBinding;
import com.looseboxes.spring.oauth.google.profile.peopleapi.v1.GoogleProfilePeopleApi;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author hp
 * @see https://developers.google.com/people/api/rest/v1/people
 * @see https://people.googleapis.com/v1/people/me?personFields=emailAddresses%2Cgenders%2Cmetadata%2Cnames%2CphoneNumbers%2Cphotos%2Curls&key=[YOUR_API_KEY]
 * @see https://developers.google.com/oauthplayground/
 */
public class Google extends ApiBinding<GoogleProfilePeopleApi> {

    public static final String CLIENT_ID = "google";

    public Google(String accessToken, String userInfoUri, RestTemplate restTemplate, 
            ObjectMapper objectMapper, ProfileConverterFactory converterFactory) {
        super(CLIENT_ID, 
                accessToken, 
                userInfoUri, 
                GoogleProfilePeopleApi.class, 
                restTemplate, 
                objectMapper, 
                converterFactory);
    }
}