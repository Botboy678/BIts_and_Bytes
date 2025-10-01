package com.bits.bytes.bits.bytes.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    DEVELOPER(
            Set.of(
                    Permissions.DEVLOPER_CREATE,
                    Permissions.DEVLOPER_READ,
                    Permissions.DEVLOPER_UPDATE,
                    Permissions.DEVLOPER_DELETE
            )
    ),
    USER(Collections.emptySet())

    ;

    @Getter
    private final Set<Permissions> permission;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var Authorities = getPermission()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
        Authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return Authorities;
    }

}
