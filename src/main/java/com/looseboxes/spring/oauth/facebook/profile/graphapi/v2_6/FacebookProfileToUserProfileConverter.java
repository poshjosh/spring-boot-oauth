package com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6;

import com.looseboxes.spring.oauth.profile.Gender;
import com.looseboxes.spring.oauth.profile.UserProfile;
import com.looseboxes.spring.oauth.profile.UserProfileImpl;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public class FacebookProfileToUserProfileConverter implements Converter<FacebookProfileGraphApi, UserProfile>{

    @Override
    public UserProfile convert(FacebookProfileGraphApi profile) {
        UserProfileImpl result = new UserProfileImpl();
        result.setProfile(profile);
        
        result.setDateOfBirth(this.toInstant(profile.getBirthday()));
        
        String email = profile.getEmail();
        result.setEmailAddresses(email == null || email.isEmpty() ? Collections.EMPTY_LIST : Arrays.asList(email));
        
        result.setFamilyName(profile.getLast_name());
        
        result.setFirstName(profile.getFirst_name());
        
        result.setGender(Gender.from(profile.getGender()));
        
        result.setId(profile.getId());
        
        String pic = profile.getProfile_pic();
        result.setImageUrls(pic == null || pic.isEmpty() ? Collections.EMPTY_LIST : Arrays.asList(pic));
        
        result.setLink(profile.getLink());
        
        result.setLocales(profile.getLanguages().stream()
                .map((locale) -> locale.getName())
                .filter((value) -> value != null && !value.isEmpty())
                .collect(Collectors.toList()));
        
        result.setMiddleName(profile.getMiddle_name());
        
        result.setName(profile.getName());

        return result;
    }
    
    private Instant toInstant(String s) {
        LocalDate localDate = s == null || s.isEmpty() ? null : this.toLocalDate(s);
        return localDate == null ? null : localDate.atTime(0, 0).toInstant(ZoneOffset.UTC);
    }

    /**
     * MM/DD/YYYY, MM/DD, YYYY, YYYY/MM/DD
     * @param s
     * @return 
     */
    private LocalDate toLocalDate(String s) {
        if(s == null || s.isEmpty()) {
            return null;
        }

        LocalDate result = null;
        String [] patterns = {"MM/dd/yyyy", "MM/dd", "yyyy", "yyyy/MM/dd"};
        for(String pattern : patterns) {
            try{
                result = LocalDate.parse(s, DateTimeFormatter.ofPattern(pattern));
                break;
            }catch(DateTimeParseException ignored) {}
        }
        return result;
    }
}
