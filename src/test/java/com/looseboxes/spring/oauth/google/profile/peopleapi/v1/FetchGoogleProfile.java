package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import org.springframework.web.client.RestTemplate;

/**
 * @author hp
 */
public class FetchGoogleProfile {

    private final String baseUrl = "https://people.googleapis.com/v1/people/me?personFields=emailAddresses,genders,metadata,names,phoneNumbers,photos,urls";
    
    public void test(String accessToken, String apiKey) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.getInterceptors().add((request, bytes, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, bytes);
        });
        
        System.out.println("fetching google profile");
        
        GoogleProfilePeopleApi profile = restTemplate.getForObject(
                baseUrl + "&key=" + apiKey, GoogleProfilePeopleApi.class);
        
        System.out.println(profile);
    }
}
