package com.mycompany.myapp.domain;

public class Stats {

    long id;
    private int NonCommence;
    private int Encours;
    private int Termine;
    private int Abondonne;
    private int Valide;
    private int Refuse;

    public Stats(int nonCommence, int encours, int termine, int abondonne, int valide, int refuse) {
        NonCommence = nonCommence;
        Encours = encours;
        Termine = termine;
        Abondonne = abondonne;
        this.Valide = valide;
        this.Refuse = refuse;
    }

    public void setNonCommence(int nonCommence) {
        NonCommence = nonCommence;
    }

    public void setEncours(int encours) {
        Encours = encours;
    }

    public void setTermine(int termine) {
        Termine = termine;
    }

    public void setAbondonne(int abondonne) {
        Abondonne = abondonne;
    }

    public void setValide(int valide) {
        this.Valide = valide;
    }

    public void setRefuse(int refuse) {
        this.Refuse = refuse;
    }

    public int getNonCommence() {
        return NonCommence;
    }

    public int getEncours() {
        return Encours;
    }

    public int getTermine() {
        return Termine;
    }

    public int getAbondonne() {
        return Abondonne;
    }

    public int getValide() {
        return Valide;
    }

    public int getRefuse() {
        return Refuse;
    }
}
