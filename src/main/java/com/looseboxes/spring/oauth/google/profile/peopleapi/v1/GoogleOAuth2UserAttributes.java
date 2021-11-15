package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;

public class GoogleOAuth2UserAttributes {

    private String email;
    private String picture;
    private String locale;
    private String givenName;
    private String familyName;

    public GoogleOAuth2UserAttributes() {}

    public GoogleOAuth2UserAttributes(OAuth2User user) {
        //User Attributes: [{sub=100963511008223346244, name=jane doe, given_name=jane, family_name=doe, picture=https://lh3.googleusercontent.com/a/picutre-of-jane-doe, email=jane.doe@gmail.com, email_verified=true, locale=en}]
        setEmail(user.getAttribute("email"));
        setPicture(user.getAttribute("picture"));
        setLocale(user.getAttribute("locale"));
        setGivenName(user.getAttribute("given_name"));
        setFamilyName(user.getAttribute("family_name"));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoogleOAuth2UserAttributes that = (GoogleOAuth2UserAttributes) o;
        return Objects.equals(email, that.email) && Objects.equals(picture, that.picture) && Objects.equals(locale, that.locale) && Objects.equals(givenName, that.givenName) && Objects.equals(familyName, that.familyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, picture, locale, givenName, familyName);
    }

    @Override
    public String toString() {
        return "GoogleOAuth2UserAttributes{" +
                "email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                ", locale='" + locale + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                '}';
    }
}
