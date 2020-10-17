package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata implements Serializable{
    
    private String objectType;
    private List<Source> sources = Collections.EMPTY_LIST;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source{
        
        private String id;
        private String etag;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Source{" + "id=" + id + ", etag=" + etag + ", type=" + type + '}';
        }
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "Metadata{" + "objectType=" + objectType + ", sources=" + sources + '}';
    }
}
