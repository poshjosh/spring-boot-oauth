package com.looseboxes.spring.oauth;

import com.looseboxes.spring.oauth.OAuth2Parameters;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * https://www.baeldung.com/spring-security-custom-oauth-requests
 * @author hp
 */
public class CustomTokenResponseConverter implements 
        Converter<Map<String, String>, OAuth2AccessTokenResponse> {
    
    private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = Stream.of(
        OAuth2ParameterNames.ACCESS_TOKEN, 
        OAuth2ParameterNames.TOKEN_TYPE, 
        OAuth2ParameterNames.EXPIRES_IN, 
        OAuth2ParameterNames.REFRESH_TOKEN, 
        OAuth2ParameterNames.SCOPE).collect(Collectors.toSet());
 
    @Override
    public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
        
        System.out.println(this.getClass().getName());
        System.out.println(tokenResponseParameters);
//        Set<String> scopes = Collections.emptySet();
//        if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
//            String scope = tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
//            scopes = Arrays.stream(StringUtils.delimitedListToStringArray(scope, ","))
//                .collect(Collectors.toSet());
//        }
 
        OAuth2AccessTokenResponse response = OAuth2AccessTokenResponse
            .withToken(tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN))
            .tokenType(OAuth2Parameters.tokenType(tokenResponseParameters.get(OAuth2ParameterNames.TOKEN_TYPE)).orElse(null))
            .expiresIn(OAuth2Parameters.expiresIn(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN), 0))
            .scopes(OAuth2Parameters.scopes(tokenResponseParameters.get(OAuth2ParameterNames.SCOPE)))
            .refreshToken(tokenResponseParameters.get(OAuth2ParameterNames.REFRESH_TOKEN))
            .build();
        
        return response;
    }
}