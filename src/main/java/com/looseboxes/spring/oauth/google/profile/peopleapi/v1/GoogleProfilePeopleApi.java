package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author hp
 * @see https://developers.google.com/oauthplayground/
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleProfilePeopleApi implements Serializable{

    private List<Photo> photos = Collections.EMPTY_LIST;
    private String etag;
    private List<Name> names = Collections.EMPTY_LIST;
    private String resourceName;
    private List<EmailAddress> emailAddresses = Collections.EMPTY_LIST;
    private List<Photo> coverPhotos = Collections.EMPTY_LIST;
    private List<Locale> locales = Collections.EMPTY_LIST;
    private List<Gender> genders = Collections.EMPTY_LIST;
    private Metadata metadata;

    public GoogleProfilePeopleApi() { }

    public GoogleProfilePeopleApi(GoogleOAuth2UserAttributes user) {
        if(user.getEmail() != null) {
            EmailAddress email = new EmailAddress();
            email.setValue(user.getEmail());
            setEmailAddresses(Collections.singletonList(email));
        }
        if(user.getPicture() != null) {
            Photo photo = new Photo();
            photo.setUrl(user.getPicture());
            setPhotos(Collections.singletonList(photo));
        }
        if(user.getLocale() != null) {
            Locale locale = new Locale();
            locale.setValue(user.getLocale());
            setLocales(Collections.singletonList(locale));
        }
        String givenName = user.getGivenName();
        String familyName = user.getFamilyName();
        if(givenName != null || familyName != null) {
            Name name = new Name();
            name.setFamilyName(familyName);
            name.setGivenName(givenName);
            setNames(Collections.singletonList(name));
        }
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public List<Photo> getCoverPhotos() {
        return coverPhotos;
    }

    public void setCoverPhotos(List<Photo> coverPhotos) {
        this.coverPhotos = coverPhotos;
    }

    public List<Locale> getLocales() {
        return locales;
    }

    public void setLocales(List<Locale> locales) {
        this.locales = locales;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "GoogleProfilePeopleApi_v1{" + "photos=" + photos + ", etag=" + etag + 
                ", names=" + names + ", resourceName=" + resourceName + ", emailAddresses=" + 
                emailAddresses + ", coverPhotos=" + coverPhotos + ", locales=" + locales + 
                ", genders=" + genders + ", metadata=" + metadata + '}';
    }
}
