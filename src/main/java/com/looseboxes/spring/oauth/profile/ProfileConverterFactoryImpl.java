package com.looseboxes.spring.oauth.profile;

import com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6.FacebookProfileGraphApi;
import com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6.FacebookProfileToUserProfileConverter;
import com.looseboxes.spring.oauth.google.profile.peopleapi.v1.GoogleProfilePeopleApi;
import com.looseboxes.spring.oauth.google.profile.peopleapi.v1.GoogleProfileToUserProfileConverter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public class ProfileConverterFactoryImpl extends ProfileConverterFactoryReadOnly{
    
    private static Map<Class<?>, Converter<?, UserProfile>> converters() {
        Map<Class<?>, Converter<?, UserProfile>> result = new HashMap<>(4, 0.75f);
        result.put(FacebookProfileGraphApi.class, new FacebookProfileToUserProfileConverter());
        result.put(GoogleProfilePeopleApi.class, new GoogleProfileToUserProfileConverter());
        return result;
    }

    public ProfileConverterFactoryImpl() {
        this(ProfileConverterFactory.NO_OP);
    }
    
    public ProfileConverterFactoryImpl(ProfileConverterFactory parent) {
        super(parent, converters());
    }
}
