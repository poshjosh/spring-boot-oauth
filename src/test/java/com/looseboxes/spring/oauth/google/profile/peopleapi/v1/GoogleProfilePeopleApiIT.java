package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.profile.UserProfile;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public class GoogleProfilePeopleApiIT {
    
    private final boolean debug = true;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private final Converter<GoogleProfilePeopleApi, UserProfile> converter = new GoogleProfileToUserProfileConverter();
    
    @Test
    public void test() {
        
        try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("google-profile.json")) {
        
            GoogleProfilePeopleApi google = objectMapper.readValue(in, GoogleProfilePeopleApi.class);
            
            if(debug) System.out.println(google);
            
            UserProfile user = converter.convert(google);
            if(debug) System.out.println(user);

        }catch(IOException e) {
            fail(e.getMessage(), e);
        }
    }
}
