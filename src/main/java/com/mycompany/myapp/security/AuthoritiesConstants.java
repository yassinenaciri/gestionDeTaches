package com.mycompany.myapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String CADRE = "ROLE_CADRE";
    public static final String CHEFPOLE = "ROLE_CHEFPOLE";
    public static final String CHEFDIVISION = "ROLE_CHEFDIVISION";
    public static final String CHEFSERVICE = "ROLE_CHEFSERVICE";
    public static final String DIRECTEUR = "ROLE_DIRECTEUR";

    private AuthoritiesConstants() {}
}
