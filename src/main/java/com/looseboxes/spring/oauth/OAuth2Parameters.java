package com.looseboxes.spring.oauth;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

/**
 * @author hp
 */
public class OAuth2Parameters {
    
    public static long expiresIn(String text, long resultIfNone) {
        return text == null || text.isEmpty() ? resultIfNone : Long.parseLong(text);
    }
    
    public static Optional<TokenType> tokenType(String text) {
        return Optional.ofNullable(text == null || text.isEmpty() ? null : TokenType.BEARER.getValue().equals(text) ? TokenType.BEARER : null);
    }
 
    public static Set<String> scopes(String text) {
        final List<String> scopes;
        // The test for , comes first as it with match '   , ' which 
        // also contains space
        if(text != null && text.contains(",")) { 
            scopes = split(text, ",");
        }else if(text != null && Pattern.compile("\\s").matcher(text).find()){
            scopes = split(text, "\\s");
        }else{
            scopes = text == null ? Collections.EMPTY_LIST : Collections.singletonList(text);
        }
        return Collections.unmodifiableSet(new LinkedHashSet(scopes));
    }

    private static List<String> split(String text, String s) {
        String [] parts = text.split(s); 
//        System.out.println("Parts: " + Arrays.toString(parts));
        List<String> split = Arrays.asList(parts).stream()
                .filter((e) -> e != null && ! e.isEmpty())
                .map((e) -> e.trim())
                .filter((e) -> ! e.isEmpty())
                .collect(Collectors.toList());
//        System.out.println("Split: " + split);
        return split;
    }
}
