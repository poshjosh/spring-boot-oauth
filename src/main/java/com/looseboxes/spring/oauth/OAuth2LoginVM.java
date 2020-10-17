package com.looseboxes.spring.oauth;

import java.util.List;
import javax.validation.constraints.NotEmpty;

/**
 * @author hp
 */
public class OAuth2LoginVM {

    @NotEmpty
    private String client_id;
    
    @NotEmpty
    private String access_token;
    
    private Integer expires_in;
    
    private List<String> scopes;
    
    private Boolean remember_me = Boolean.TRUE;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public Boolean isRememberMe() {
        return remember_me;
    }

    public void setRemember_me(Boolean remember_me) {
        this.remember_me = remember_me;
    }

    @Override
    public String toString() {
        return "OAuth2LoginVM{clientId=" + client_id + 
                ",accessToken=*******, expiresIn=" + expires_in + 
                ", scopes=" + scopes + ", rememberMe=" + remember_me +  '}';
    }
}
