package com.looseboxes.spring.oauth.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.ApiBinding;
import com.looseboxes.spring.oauth.google.profile.peopleapi.v1.GoogleOAuth2UserAttributes;
import com.looseboxes.spring.oauth.google.profile.peopleapi.v1.GoogleProfilePeopleApi;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

/**
 * @author hp
 * https://developers.google.com/people/api/rest/v1/people
 * https://people.googleapis.com/v1/people/me?personFields=emailAddresses%2Cgenders%2Cmetadata%2Cnames%2CphoneNumbers%2Cphotos%2Curls&key=[YOUR_API_KEY]
 * https://developers.google.com/oauthplayground/
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

    @Override
    public OAuth2Profile<GoogleProfilePeopleApi> fetchOAuthProfile(OAuth2User user) throws JsonProcessingException {
        try {
            return super.fetchOAuthProfile(user);
        }catch(RuntimeException e) {
            try {
                GoogleOAuth2UserAttributes googleOAuth2UserAttributes = new GoogleOAuth2UserAttributes(user);
                GoogleProfilePeopleApi googleProfilePeopleApi = new GoogleProfilePeopleApi(googleOAuth2UserAttributes);
                String json = toJsonString(googleOAuth2UserAttributes);
                return convertToOAuth2Profile(user, googleProfilePeopleApi, json);
            }catch(RuntimeException inner) {
                e.addSuppressed(inner);
                throw e;
            }
        }
    }

    private String toJsonString(GoogleOAuth2UserAttributes googleOAuth2UserAttributes) {
        try {
            return getObjectMapper().writeValueAsString(googleOAuth2UserAttributes);
        }catch(JsonProcessingException e) {
            return "{}";
        }
    }
}