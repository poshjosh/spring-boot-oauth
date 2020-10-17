package com.looseboxes.spring.oauth.google.profile.peopleapi.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.looseboxes.spring.oauth.profile.UserProfile;
import com.looseboxes.spring.oauth.profile.UserProfileImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author hp
 */
public class JsonNodeToUserProfileConverterForGooglePeopleApi_v1{
    
    public UserProfile convert(JsonNode node) throws JsonProcessingException {
    
        UserProfileImpl result = new UserProfileImpl();
        
        String id = node.path("metadata").path("sources").path(0).path("id").asText(null);
        
        JsonNode emailNodes = node.path("emailAddresses");
        
        if(id == null || id.isEmpty()) {
            id = emailNodes.path(0).path("metadata").path("id").asText(null);
        }
        
        result.setId(Objects.requireNonNull(id));
        
        result.setEmailAddresses(Collections.unmodifiableList(
                stream(emailNodes, "value").collect(Collectors.toList())));
        
        List<String> imageUrls = new ArrayList();
        imageUrls.addAll(stream(node, "photos", "url").collect(Collectors.toList()));
        imageUrls.addAll(stream(node, "coverPhotos", "url").collect(Collectors.toList()));
        result.setImageUrls(Collections.unmodifiableList(imageUrls));
        
        result.setFamilyName(stream(node, "names", "familyName").findFirst().orElse(null));
        result.setMiddleName(stream(node, "names", "middleName").findFirst().orElse(null));
        result.setFirstName(stream(node, "names", "givenName").findFirst().orElse(null));
        
        result.setLocales(Collections.unmodifiableList(
                stream(node, "locales", "value").collect(Collectors.toList())));
        
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

    private Stream<String> stream(JsonNode root, String name0, String name1) {
        final JsonNode array = root.get(name0);
        return this.stream(array, name1);
    }

    private Stream<String> stream(JsonNode array, String name) {
        if(array != null && ! array.isEmpty()) {
            return StreamSupport.stream(array.spliterator(), false)
                    .map((object) -> object.get(name))
                    .filter((value) -> value != null)
                    .map((value) -> value.asText(null))
                    .filter((valueText) -> valueText != null);
        }else{
            return Stream.empty();
        }
    }
}
