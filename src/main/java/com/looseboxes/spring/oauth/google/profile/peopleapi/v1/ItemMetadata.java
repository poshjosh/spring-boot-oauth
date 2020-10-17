package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemMetadata implements Serializable{
    
    private boolean primary;
    
    private Source source;

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source implements Serializable{
    
        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Source{" + "id=" + id + ", type=" + type + '}';
        }
    }

    @Override
    public String toString() {
        return "ItemMetadata{" + "primary=" + primary + '}';
    }
}
