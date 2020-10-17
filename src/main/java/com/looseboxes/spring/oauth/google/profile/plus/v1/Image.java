package com.looseboxes.spring.oauth.google.profile.plus.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Serializable{
    
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Image{" + "url=" + url + '}';
    }
}
