/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looseboxes.spring.oauth.google.profile.plus.v1;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;

/**
 * https://www.googleapis.com/plus/v1/people
 * @author hp
 */
public class GoogleProfilePlus implements Serializable{

    private String kind;
    private String displayName;
    private Name name;
    private String language;
    private Image image;
    private List<Email> emails;
    private String etag;

    @NotBlank
    private String id;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GoogleProfilePlusv1{" + "kind=" + kind + ", displayName=" + displayName + ", name=" + name + ", language=" + language + ", image=" + image + ", emails=" + emails + ", etag=" + etag + ", id=" + id + '}';
    }
}

