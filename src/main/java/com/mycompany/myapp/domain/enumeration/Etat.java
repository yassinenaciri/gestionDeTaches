package com.mycompany.myapp.domain.enumeration;

/**
 * The Etat enumeration.
 */
public enum Etat {
    NonCommence("Non commencé"),
    Encours("En cours"),
    Termine("Terminé"),
    Abondonne("Abondonné"),
    Valide("Validé"),
    Refuse("Refusé");

    private final String value;

    Etat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
