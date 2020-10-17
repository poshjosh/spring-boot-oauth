package com.looseboxes.spring.oauth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.looseboxes.spring.oauth.OAuth2LoginVM;
import java.io.IOException;
import java.util.Objects;

/**
 * @author hp
 */
public class OAuthResponseDeserializer extends StdDeserializer<OAuth2LoginVM> {

    private final JsonNodeToOAuth2LoginVMConverter converter;
    
    public OAuthResponseDeserializer(JsonNodeToOAuth2LoginVMConverter converter) {
        this(null, converter);
    }
    
    public OAuthResponseDeserializer(Class<?> valueClass, JsonNodeToOAuth2LoginVMConverter converter) {
        super(valueClass);
        this.converter = Objects.requireNonNull(converter);
    }

    @Override
    public OAuth2LoginVM deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    
        JsonNode node = parser.getCodec().readTree(parser);
        
        return converter.convert(node);
    }
}
