package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAddress implements Serializable{
    
    private String value;
    
    private EmailAddress.Metadata metadata;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata extends com.looseboxes.spring.oauth.google.profile.peopleapi.v1.ItemMetadata{

        private boolean verified;

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        @Override
        public String toString() {
            return "Metadata{" + "verified=" + verified + "primary=" + isPrimary() + '}';
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "EmailAddress{" + "value=" + value + ", metadata=" + metadata + '}';
    }
}
