package com.looseboxes.spring.oauth;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * Build an OAuth2AccessToken from the request sent by OAuth2 providers such 
 * as facebook, google, github etc
 * Sample response (from google oauth)
 * <code>
 * http://localhost:8888/Callback#state=
 * &access_token=[]
 * &token_type=Bearer
 * &expires_in=3599
 * &scope=profile%20https://www.googleapis.com/auth/userinfo.profile
 * &id_token=[]
 * </code>
 * @see https://developers.facebook.com/docs/facebook-login/web/
 * @see https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/
 * @author hp
 */
public class OAuth2AccessTokenFromHttpRequestParameters extends OAuth2AccessToken{

    private static Set<String> requireValues(HttpServletRequest request, String name) {
        final String [] result = request.getParameterValues(name);
        if(result == null || result.length == 0) {
            return Collections.EMPTY_SET;
        }else{
            return Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(result)));
        }
    }

    private static String requireValue(HttpServletRequest request, String name) {
        final String result = request.getParameter(name);
        if(Objects.requireNonNull(result).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }
    
    private static TokenType getTokenType(HttpServletRequest request, String paramName) {
        if(paramName == null || paramName.isEmpty()) {
            return TokenType.BEARER;
        }else{
            final String result = requireValue(request, paramName);
            if(TokenType.BEARER.getValue().equals(result)) {
                return TokenType.BEARER;
            }
            throw new IllegalArgumentException();
        }
    }

    private static String getTokenValue(HttpServletRequest request, String paramName) {
        final String result = requireValue(request, paramName);
        return result;
    }

    private static Instant getExpiresAt(HttpServletRequest request, String paramName) {
        final String result = requireValue(request, paramName);
        return Instant.now().plusSeconds(Integer.parseInt(result));
    }

    private static Set<String> getScopes(HttpServletRequest request, String paramName) {
        final Set<String> result = requireValues(request, paramName);
        return result;
    }
    
    public OAuth2AccessTokenFromHttpRequestParameters(HttpServletRequest request, String... paramNames) {
        super(getTokenType(request, paramNames[0]), 
                getTokenValue(request, paramNames[1]), 
                Instant.now(), 
                getExpiresAt(request, paramNames[2]), 
                paramNames.length == 4 ? getScopes(request, paramNames[3]) : Collections.EMPTY_SET);
    }
    
}
