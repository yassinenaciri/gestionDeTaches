package com.mycompany.myapp.domain;

public class Classement {

    private Employe employe;
    private int nbreTachesTermine;

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public int getNbreTachesTermine() {
        return nbreTachesTermine;
    }

    public void setNbreTachesTermine(int nbreTachesTermine) {
        this.nbreTachesTermine = nbreTachesTermine;
    }

    public Classement(Employe employe, int nbreTachesTermine) {
        this.employe = employe;
        this.nbreTachesTermine = nbreTachesTermine;
    }
}
