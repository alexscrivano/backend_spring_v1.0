package com.example.utils;
import com.example.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final String principalAttributeName = "preferred_username";

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var authorities = Stream.concat(jwtGrantedAuthoritiesConverter.convert(source).stream(),extractRoles(source).stream()).collect(Collectors.toList());
        return new JwtAuthenticationToken(source,authorities,getPrincipalClaim(source));
    }

    private String getPrincipalClaim(Jwt token) {
        String claimName = JwtClaimNames.SUB;
        claimName = principalAttributeName;
        return token.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractRoles(Jwt source) {
        Map<String,Object> resAccess;
        Map<String,Object> resource;

        Collection<String> roles;

        if (source.getClaim("resource_access") == null) {
            return Set.of();
        }
        resAccess = source.getClaim("resource_access");
        if(resAccess.get("test-client") == null) {
            return Set.of();
        }
        resource = (Map<String,Object>) resAccess.get("client_1");
        roles = (Collection<String>) resource.get("roles");
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }

}
