package com.looseboxes.spring.oauth.profile;

/**
 * @author hp
 */
public enum Gender {
    
    Male, Female, Other;
    
    public static Gender from(String value) {
        if(value == null || value.isEmpty()) {
            return null;
        }else{
            if(value.equalsIgnoreCase(Male.name())) {
                return Male;
            }else if(value.equalsIgnoreCase(Female.name())) {
                return Female;
            }else{
                return Other;
            }
        }
    }
}
