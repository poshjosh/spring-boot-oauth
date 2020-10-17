package com.looseboxes.spring.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.ValidationException;

/**
 * @author hp
 */
public class JsonNodeToOAuth2LoginVMConverter{
    
    public OAuth2LoginVM convert(JsonNode node) throws JsonProcessingException {
    
        OAuth2LoginVM result = new OAuth2LoginVM();

        first(node, "access_token", "accessToken", "accesstoken")
                .map((e) -> {
                    result.setAccess_token(e.requireNonNull().asText());
                    return e;
                }).orElseThrow(() -> new ValidationException("Required value, access_token is missing"));
        
        first(node, "client_id", "clientId", "clientid")
                .map((e) -> {
                    result.setClient_id(e.requireNonNull().asText());
                    return e;
                }).orElseThrow(() -> new ValidationException("Required value, client_id is missing"));
        
        first(node, "expires_in", "expiresIn", "expiresin")
                .ifPresent(e -> result.setExpires_in(e.asInt(0)));
        
        first(node, "remember_me", "rememberMe", "rememberme")
                .ifPresent(e -> result.setRemember_me(e.asBoolean(true)));

        first(node, "scopes", "scope").ifPresent((scopesNode) -> {
        
            List<String> scopes;
            if(scopesNode.isArray()) {
                scopes = StreamSupport.stream(scopesNode.spliterator(), false)
                        .map(e -> e.asText(null))
                        .filter(e -> e != null)
                        .collect(Collectors.toList());
            }else{
                String text = scopesNode.asText(null);
                scopes = new ArrayList(OAuth2Parameters.scopes(text));
            }

            result.setScopes(scopes);
        });
        
        
        return result;
    }

    private Optional<JsonNode> first(JsonNode node, String... names) {
        JsonNode result = null;
        for(String name : names) {
            result = node.get(name);
            if(result != null && ! result.isNull() && ! result.isMissingNode()) {
                break;
            }
        }
        return Optional.ofNullable(result);
    }
}
