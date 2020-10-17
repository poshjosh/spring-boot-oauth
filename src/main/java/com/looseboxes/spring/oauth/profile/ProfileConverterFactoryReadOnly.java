package com.looseboxes.spring.oauth.profile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public class ProfileConverterFactoryReadOnly implements ProfileConverterFactory{
    
    private final Set<String> classNames;
    
    private final ProfileConverterFactory parent;
    
    private final Map<Class<?>, Converter<?, UserProfile>> converters;

    public ProfileConverterFactoryReadOnly(Map<Class<?>, Converter<?, UserProfile>> converters) {
        this(ProfileConverterFactory.NO_OP, converters);
    }
    
    public ProfileConverterFactoryReadOnly(ProfileConverterFactory parent, Map<Class<?>, Converter<?, UserProfile>> converters) {
        this.parent = parent;
        this.classNames = converters.keySet().stream()
                .filter((cls) -> converters.get(cls) != null)
                .map((cls) -> cls.getName()).collect(Collectors.toSet());
        this.converters = Collections.unmodifiableMap(new HashMap(converters));
    }

    @Override
    public boolean isSupported(Class type) {
        return classNames.contains(type.getName()) || parent.isSupported(type);
    }

    @Override
    public <P> Converter<P, UserProfile> getConverter(Class<P> type) {
        Converter converter = converters.get(type);
        if(converter == null) {
            converter = parent.getConverter(type);
            if(converter == null) {
                throw new IllegalArgumentException("Unsupported type: " + type + ", use the isSupported(Class) method to confirm if a type is supported before attempting to get a Converter for that type.");
            }
        }
        return converter;
    }
}
