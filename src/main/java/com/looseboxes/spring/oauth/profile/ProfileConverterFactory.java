package com.looseboxes.spring.oauth.profile;

import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public interface ProfileConverterFactory{
    
    ProfileConverterFactory NO_OP = new ProfileConverterFactory(){
        @Override
        public boolean isSupported(Class type) { return false; }

        @Override
        public Converter<Object, UserProfile> getConverter(Class type) {
            throw new UnsupportedOperationException("Converting to UserProfile not supported from: " + type); 
        }
    };
    
    boolean isSupported(Class type);
    
    <P> Converter<P, UserProfile> getConverter(Class<P> type);
}
