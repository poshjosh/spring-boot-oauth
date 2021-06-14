package com.looseboxes.spring.oauth.google.profile.plus.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Name {
    
    private String givenName;
    private String familyName;

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
    public String toString() {
        return "Name{" + "givenName=" + givenName + ", familyName=" + familyName + '}';
    }
}
