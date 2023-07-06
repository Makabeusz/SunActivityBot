package com.sojka.sunactivity.security.user;

public enum Role {

    USER,
    ADMIN;

    public String roleName() {
        return "ROLE_" + this.name();
    }
}
