package com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Experience implements Serializable{

    /**
     * Numeric string
     */
    private String id;
    
    private String name;
    
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Experience{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }
}
