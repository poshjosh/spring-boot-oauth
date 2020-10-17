package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo implements Serializable{
    
    private String url;
    
    @JsonProperty("default")
    private boolean defaultPhoto;
    
    private ItemMetadata metadata;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDefaultPhoto() {
        return defaultPhoto;
    }

    public void setDefaultPhoto(boolean defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public ItemMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ItemMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "Photo{" + "url=" + url + ", defaultPhoto=" + defaultPhoto + ", metadata=" + metadata + '}';
    }
}
