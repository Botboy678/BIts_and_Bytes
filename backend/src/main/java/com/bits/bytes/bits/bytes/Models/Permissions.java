package com.bits.bytes.bits.bytes.Models;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {

    DEVLOPER_CREATE("developer:create"),
    DEVLOPER_READ("developer:read"),
    DEVLOPER_UPDATE("developer:update"),
    DEVLOPER_DELETE("developer:delete");

    @Getter
    private final String name;
}
