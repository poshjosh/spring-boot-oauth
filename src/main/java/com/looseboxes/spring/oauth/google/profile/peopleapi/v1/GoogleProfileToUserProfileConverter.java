package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.looseboxes.spring.oauth.profile.UserProfile;
import com.looseboxes.spring.oauth.profile.UserProfileImpl;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public class GoogleProfileToUserProfileConverter implements Converter<GoogleProfilePeopleApi, UserProfile>{

    @Override
    public UserProfile convert(GoogleProfilePeopleApi profile) {
        UserProfileImpl result = new UserProfileImpl();
        result.setProfile(profile);
//        result.setDateOfBirth(?);
        result.setEmailAddresses(profile.getEmailAddresses().stream()
                .map((email) -> email.getValue())
                .filter((value) -> value != null && ! value.isEmpty())
                .collect(Collectors.toList()));
        result.setFamilyName(profile.getNames().stream()
                .map((name) -> name.getFamilyName())
                .filter((value) -> value != null && ! value.isEmpty())
                .findFirst().orElse(null));
        result.setFirstName(profile.getNames().stream()
                .map((name) -> name.getGivenName())
                .filter((value) -> value != null && ! value.isEmpty())
                .findFirst().orElse(null));
        result.setGender(profile.getGenders().stream()
                .map((gender) -> gender.getValue())
                .filter((value) -> value != null && ! value.isEmpty())
                .findFirst().map((value) -> com.looseboxes.spring.oauth.profile.Gender.from(value)).orElse(null));
        result.setId(profile.getMetadata().getSources().stream()
                .map((source) -> source.getId())
                .filter((value) -> value != null && ! value.isEmpty())
                .findFirst().orElseThrow(() -> new RuntimeException("Required attribute id not found")));
        result.setImageUrls(profile.getPhotos().stream()
                .map((photo) -> photo.getUrl())
                .filter((value) -> value != null && ! value.isEmpty())
                .collect(Collectors.toList()));
//        result.setLink(?);
        result.setLocales(profile.getLocales().stream()
                .map((locale) -> locale.getValue())
                .filter((value) -> value != null && ! value.isEmpty())
                .collect(Collectors.toList()));
//        result.setMiddleName(?);
//        result.setName(?);
        return result;
    }
}
