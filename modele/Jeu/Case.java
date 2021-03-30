package modele.Jeu;

import modele.Graph.Node;

public class Case extends Node{
    public static Case CASE_VIDE = new Case('Z');
    protected int pions;//-1 si vide sinon valeur du joueur qui le posséde

    public Case(char idf) {
        super(idf);
        this.pions=-1;
    }

    public int getPions() {
        return pions;
    }

    public void setPions(int pions) {
        this.pions = pions+0;
    }
}
