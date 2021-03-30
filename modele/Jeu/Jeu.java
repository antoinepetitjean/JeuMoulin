package modele.Jeu;

import java.util.ArrayList;

public class Jeu {
    private Board board;
    private ArrayList<Jeu> next;

    public Jeu(Board board){
        this.board=board;
        this.next = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Jeu> getNext() {
        return next;
    }

    public ArrayList<Board> getNextBoard(){
        ArrayList<Board> res = new ArrayList<>();
        for (Jeu j:next){
            res.add(j.getBoard());
        }
        return res;
    }

    public void setNext(ArrayList<Board> next) {
        for(Board b : next){
            this.next.add(new Jeu(b));
        }
    }

    public void addNext(Jeu j){
        next.add(j);
    }

    public boolean isSet(){
        return !this.next.isEmpty();
    }

    public void update(Board b){
        for(Jeu j : next){
            if(j.getBoard().equals(b)){
                board = b;
                next = j.next;
                break;
            }
        }
    }
}
