package com.looseboxes.spring.oauth.profile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
public interface UserProfile<P> {
    P getProfile();
    String getId();
    String getName();
    String getLink();
    String getFamilyName();
    String getMiddleName();
    String getFirstName();
    default Optional<String> getEmailAddress() {
        if(getEmailAddresses().isEmpty()) {
            return Optional.empty();
        }else{
            return Optional.ofNullable(getEmailAddresses().get(0));
        }
    }
    List<String> getEmailAddresses();
    default Optional<String> getImageUrl() {
        if(getImageUrls().isEmpty()) {
            return Optional.empty();
        }else{
            return Optional.ofNullable(getImageUrls().get(0));
        }
    }
    List<String> getImageUrls();
    default Optional<String> getLocale() {
        if(getLocales().isEmpty()) {
            return Optional.empty();
        }else{
            return Optional.ofNullable(getLocales().get(0));
        }
    }
    List<String> getLocales();
    Instant getDateOfBirth();
    Gender getGender();
}
