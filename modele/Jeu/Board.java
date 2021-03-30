package modele.Jeu;

import modele.Graph.Connection;
import modele.Graph.Graph;
import modele.Graph.Node;

import java.util.ArrayList;
import java.util.Observable;

public class Board extends Observable {
    protected int currentPlayer;
    protected int pionRestants[];
    protected int pionVivant[];
    protected Graph board;
    protected int compteur;


    public Board(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.pionRestants=new int[2];
        this.pionRestants[0]=9;
        this.pionRestants[1]=9;
        this.pionVivant=new int[2];
        this.pionVivant[0]=9;
        this.pionVivant[1]=9;
        this.board = new Graph();
        for (char c = 'A'; c<='X'; c++){
            this.board.addNode(new Case(c));
        }
        this.board.connect(0,1,0);
        this.board.connect(0,9,1);
        this.board.connect(1,2,0);
        this.board.connect(1,4,1);
        this.board.connect(2,14,1);
        this.board.connect(3,4,0);
        this.board.connect(3,10,1);
        this.board.connect(4,5,0);
        this.board.connect(4,7,1);
        this.board.connect(5,13,1);
        this.board.connect(6,7,0);
        this.board.connect(6,11,1);
        this.board.connect(7,8,0);
        this.board.connect(8,12,1);
        this.board.connect(9,21,1);
        this.board.connect(9,10,0);
        this.board.connect(10,11,0);
        this.board.connect(10,18,1);
        this.board.connect(11,15,1);
        this.board.connect(12,13,0);
        this.board.connect(12,17,1);
        this.board.connect(13,14,0);
        this.board.connect(13,20,1);
        this.board.connect(14,23,1);
        this.board.connect(15,16,0);
        this.board.connect(16,17,0);
        this.board.connect(16,19,1);
        this.board.connect(18,19,0);
        this.board.connect(19,20,0);
        this.board.connect(19,22,1);
        this.board.connect(21,22,0);
        this.board.connect(22,23,0);
    }



    public Board(int currentPlayer, int[] pionRestants, int[] pionVivant, Graph b){
        this(currentPlayer);
        this.pionRestants[0]=pionRestants[0];
        this.pionRestants[1]=pionRestants[1];
        this.pionVivant[0]=pionVivant[0];
        this.pionVivant[1]=pionVivant[1];
        for(int i = 0; i<24; i++){
            ((Case)this.board.getNodes().get(i)).setPions((((Case)b.getNodes().get(i)).getPions())+0);
        }
    }

    public void begin(){
        setChanged();
        notifyObservers();
        notifyObservers();
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int[] getPionRestants() {
        return pionRestants;
    }

    public Graph getBoard() {
        return board;
    }

    public ArrayList<Move> getMoves(){
        ArrayList<Move> res = new ArrayList<>();
        if(pionRestants[currentPlayer]>0){
            for(Node node: board.getNodes()){
                Case c = (Case)node;
                if(c.getPions()==-1) {
                    Move m = new Move(Case.CASE_VIDE, c, Case.CASE_VIDE);
                    if(moulin(m, currentPlayer)){
                        for (Node c2 : getBoard().getNodes()){
                            if(((Case)c2).getPions()==(currentPlayer+1)%2){
                                res.add(new Move(Case.CASE_VIDE,c,(Case)c2));
                            }
                        }
                    }else {
                        res.add(m);
                    }
                }
            }
        }
        else if(pionVivant[currentPlayer]>3){
            for(Node node: board.getNodes()){
                Case c = (Case)node;
                int i = board.getNodes().indexOf(c);
                if(c.getPions()==currentPlayer) {
                    for(Connection con : board.getConnectionsFrom(i)){
                        Case to = (Case)con.getTo();
                        if(to.getPions()==-1){
                            Move m =new Move(c, to, Case.CASE_VIDE);
                            if(moulin(m, currentPlayer)){
                                for (Node c2 : getBoard().getNodes()){
                                    if(((Case)c2).getPions()==(currentPlayer+1)%2){
                                        res.add(new Move(c,to,(Case)c2));
                                    }
                                }
                            }else {
                                res.add(m);
                            }
                        }
                    }
                }
            }
        }
        else if(pionVivant[currentPlayer]==3){
            for(Node node: board.getNodes()){
                Case c = (Case)node;
                int i = board.getNodes().indexOf(c);
                if(c.getPions()==currentPlayer) {
                    for(Node ca : board.getNodes()){
                        Case to = (Case)ca;
                        if(to.getPions()==-1){
                            Move m =new Move(c, to, Case.CASE_VIDE);
                            if(moulin(m, currentPlayer)){
                                for (Node c2 : getBoard().getNodes()){
                                    if(((Case)c2).getPions()==(currentPlayer+1)%2){
                                        res.add(new Move(c,to,(Case)c2));
                                    }
                                }
                            }else {
                                res.add(m);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    public ArrayList<Board> successeurs(){
        ArrayList<Board> res = new ArrayList();
        int i=0;
        for(Move m : getMoves()){
            Board b = new Board(currentPlayer,pionRestants,pionVivant,board);
            b.makeMove(b.getMoves().get(i));
            i++;
            res.add(b);
        }
        return res;
    }

    public void makeMove(String move){
        if(move.length()==3){
            for(Move m : getMoves()){
                if(m.toString()==move){
                    makeMove(m);
                }
            }
        }
    }


    public void makeMove(Move move){
        if(move.getFrom().equals(Case.CASE_VIDE)){
            pionRestants[currentPlayer]--;
        }else{
            move.getFrom().setPions(-1);
        }
        move.getTo().setPions(currentPlayer);
        compteur++;
        if(!move.getOut().equals(Case.CASE_VIDE)){
            move.getOut().setPions(-1);
            pionVivant[(currentPlayer+1)%2]--;
            compteur=0;
        }
        setCurrentPlayer((currentPlayer+1)%2);
        setChanged();
        notifyObservers();
    }


    public float evaluate(int player, float a, float b){
        float eval = (pionVivant[player]-pionVivant[(player+1)%2]) + a*(nbMoulin(player)-nbMoulin((player+1)%2)) + b*(nbMove(player)-nbMove((player+1)%2));
        if(isGameOver()){
            if(currentPlayer==player) eval = Float.NEGATIVE_INFINITY;
            else eval = Float.POSITIVE_INFINITY;
        }
        if(isTieGame()){
            return 0;
        }
        return eval;
    }

    public boolean isGameOver(){
        return (pionVivant[currentPlayer]<3 || getMoves().isEmpty());
    }
    public boolean isTieGame(){
        return compteur>=50;
    }

    public boolean moulin(Move m, int currentPlayer){
        switch(m.getTo().getIdf()){
            case 'A':
                return (((Case)board.getNodes().get(1)).getPions()==currentPlayer && board.getNodes().get(1)!=m.getFrom() && ((Case)board.getNodes().get(2)).getPions()==currentPlayer && board.getNodes().get(2)!=m.getFrom())
                        || (((Case)board.getNodes().get(9)).getPions()==currentPlayer && board.getNodes().get(9)!=m.getFrom() && ((Case)board.getNodes().get(21)).getPions()==currentPlayer && board.getNodes().get(21)!=m.getFrom());

            case 'B':
                return (((Case)board.getNodes().get(0)).getPions()==currentPlayer && board.getNodes().get(0)!=m.getFrom() && ((Case)board.getNodes().get(2)).getPions()==currentPlayer && board.getNodes().get(2)!=m.getFrom())
                        || (((Case)board.getNodes().get(4)).getPions()==currentPlayer && board.getNodes().get(4)!=m.getFrom() && ((Case)board.getNodes().get(7)).getPions()==currentPlayer && board.getNodes().get(7)!=m.getFrom());

            case 'C':
                return (((Case)board.getNodes().get(0)).getPions()==currentPlayer && board.getNodes().get(0)!=m.getFrom() && ((Case)board.getNodes().get(1)).getPions()==currentPlayer && board.getNodes().get(1)!=m.getFrom())
                        || (((Case)board.getNodes().get(14)).getPions()==currentPlayer && board.getNodes().get(14)!=m.getFrom() && ((Case)board.getNodes().get(23)).getPions()==currentPlayer && board.getNodes().get(23)!=m.getFrom());

            case 'D':
                return (((Case)board.getNodes().get(4)).getPions()==currentPlayer && board.getNodes().get(4)!=m.getFrom() && ((Case)board.getNodes().get(5)).getPions()==currentPlayer && board.getNodes().get(5)!=m.getFrom())
                        || (((Case)board.getNodes().get(10)).getPions()==currentPlayer && board.getNodes().get(10)!=m.getFrom() && ((Case)board.getNodes().get(18)).getPions()==currentPlayer && board.getNodes().get(18)!=m.getFrom());

            case 'E':
                return (((Case)board.getNodes().get(3)).getPions()==currentPlayer && board.getNodes().get(3)!=m.getFrom() && ((Case)board.getNodes().get(5)).getPions()==currentPlayer && board.getNodes().get(5)!=m.getFrom())
                        || (((Case)board.getNodes().get(1)).getPions()==currentPlayer && board.getNodes().get(1)!=m.getFrom() && ((Case)board.getNodes().get(7)).getPions()==currentPlayer && board.getNodes().get(7)!=m.getFrom());

            case 'F':
                return (((Case)board.getNodes().get(3)).getPions()==currentPlayer && board.getNodes().get(3)!=m.getFrom() && ((Case)board.getNodes().get(4)).getPions()==currentPlayer && board.getNodes().get(4)!=m.getFrom())
                        || (((Case)board.getNodes().get(13)).getPions()==currentPlayer && board.getNodes().get(13)!=m.getFrom() && ((Case)board.getNodes().get(20)).getPions()==currentPlayer && board.getNodes().get(20)!=m.getFrom());

            case 'G':
                return (((Case)board.getNodes().get(7)).getPions()==currentPlayer && board.getNodes().get(7)!=m.getFrom() && ((Case)board.getNodes().get(8)).getPions()==currentPlayer && board.getNodes().get(8)!=m.getFrom())
                        || (((Case)board.getNodes().get(11)).getPions()==currentPlayer && board.getNodes().get(11)!=m.getFrom() && ((Case)board.getNodes().get(15)).getPions()==currentPlayer && board.getNodes().get(15)!=m.getFrom());

            case 'H':
                return (((Case)board.getNodes().get(6)).getPions()==currentPlayer && board.getNodes().get(6)!=m.getFrom() && ((Case)board.getNodes().get(8)).getPions()==currentPlayer && board.getNodes().get(8)!=m.getFrom())
                        || (((Case)board.getNodes().get(1)).getPions()==currentPlayer && board.getNodes().get(1)!=m.getFrom() && ((Case)board.getNodes().get(4)).getPions()==currentPlayer && board.getNodes().get(4)!=m.getFrom());

            case 'I':
                return (((Case)board.getNodes().get(6)).getPions()==currentPlayer && board.getNodes().get(6)!=m.getFrom() && ((Case)board.getNodes().get(7)).getPions()==currentPlayer && board.getNodes().get(7)!=m.getFrom())
                        || (((Case)board.getNodes().get(12)).getPions()==currentPlayer && board.getNodes().get(12)!=m.getFrom() && ((Case)board.getNodes().get(17)).getPions()==currentPlayer && board.getNodes().get(17)!=m.getFrom());

            case 'J':
                return (((Case)board.getNodes().get(10)).getPions()==currentPlayer && board.getNodes().get(10)!=m.getFrom() && ((Case)board.getNodes().get(11)).getPions()==currentPlayer && board.getNodes().get(11)!=m.getFrom())
                        || (((Case)board.getNodes().get(0)).getPions()==currentPlayer && board.getNodes().get(0)!=m.getFrom() && ((Case)board.getNodes().get(21)).getPions()==currentPlayer && board.getNodes().get(21)!=m.getFrom());

            case 'K':
                return (((Case)board.getNodes().get(9)).getPions()==currentPlayer && board.getNodes().get(9)!=m.getFrom() && ((Case)board.getNodes().get(11)).getPions()==currentPlayer && board.getNodes().get(11)!=m.getFrom())
                        || (((Case)board.getNodes().get(3)).getPions()==currentPlayer && board.getNodes().get(3)!=m.getFrom() && ((Case)board.getNodes().get(18)).getPions()==currentPlayer && board.getNodes().get(18)!=m.getFrom());

            case 'L':
                return (((Case)board.getNodes().get(9)).getPions()==currentPlayer && board.getNodes().get(9)!=m.getFrom() && ((Case)board.getNodes().get(10)).getPions()==currentPlayer && board.getNodes().get(10)!=m.getFrom())
                        || (((Case)board.getNodes().get(6)).getPions()==currentPlayer && board.getNodes().get(6)!=m.getFrom() && ((Case)board.getNodes().get(15)).getPions()==currentPlayer && board.getNodes().get(15)!=m.getFrom());

            case 'M':
                return (((Case)board.getNodes().get(13)).getPions()==currentPlayer && board.getNodes().get(13)!=m.getFrom() && ((Case)board.getNodes().get(14)).getPions()==currentPlayer && board.getNodes().get(14)!=m.getFrom())
                        || (((Case)board.getNodes().get(8)).getPions()==currentPlayer && board.getNodes().get(8)!=m.getFrom() && ((Case)board.getNodes().get(17)).getPions()==currentPlayer && board.getNodes().get(17)!=m.getFrom());

            case 'N':
                return (((Case)board.getNodes().get(12)).getPions()==currentPlayer && board.getNodes().get(12)!=m.getFrom() && ((Case)board.getNodes().get(14)).getPions()==currentPlayer && board.getNodes().get(14)!=m.getFrom())
                        || (((Case)board.getNodes().get(5)).getPions()==currentPlayer && board.getNodes().get(5)!=m.getFrom() && ((Case)board.getNodes().get(20)).getPions()==currentPlayer && board.getNodes().get(20)!=m.getFrom());

            case 'O':
                return (((Case)board.getNodes().get(12)).getPions()==currentPlayer && board.getNodes().get(12)!=m.getFrom() && ((Case)board.getNodes().get(13)).getPions()==currentPlayer && board.getNodes().get(13)!=m.getFrom())
                        || (((Case)board.getNodes().get(2)).getPions()==currentPlayer && board.getNodes().get(2)!=m.getFrom() && ((Case)board.getNodes().get(23)).getPions()==currentPlayer && board.getNodes().get(23)!=m.getFrom());

            case 'P':
                return (((Case)board.getNodes().get(16)).getPions()==currentPlayer && board.getNodes().get(16)!=m.getFrom() && ((Case)board.getNodes().get(17)).getPions()==currentPlayer && board.getNodes().get(17)!=m.getFrom())
                        || (((Case)board.getNodes().get(6)).getPions()==currentPlayer && board.getNodes().get(6)!=m.getFrom() && ((Case)board.getNodes().get(11)).getPions()==currentPlayer && board.getNodes().get(11)!=m.getFrom());

            case 'Q':
                return (((Case)board.getNodes().get(15)).getPions()==currentPlayer && board.getNodes().get(15)!=m.getFrom() && ((Case)board.getNodes().get(17)).getPions()==currentPlayer && board.getNodes().get(17)!=m.getFrom())
                        || (((Case)board.getNodes().get(19)).getPions()==currentPlayer && board.getNodes().get(19)!=m.getFrom() && ((Case)board.getNodes().get(22)).getPions()==currentPlayer && board.getNodes().get(22)!=m.getFrom());

            case 'R':
                return (((Case)board.getNodes().get(15)).getPions()==currentPlayer && board.getNodes().get(15)!=m.getFrom() && ((Case)board.getNodes().get(16)).getPions()==currentPlayer && board.getNodes().get(16)!=m.getFrom())
                        || (((Case)board.getNodes().get(8)).getPions()==currentPlayer && board.getNodes().get(8)!=m.getFrom() && ((Case)board.getNodes().get(12)).getPions()==currentPlayer && board.getNodes().get(12)!=m.getFrom());

            case 'S':
                return (((Case)board.getNodes().get(19)).getPions()==currentPlayer && board.getNodes().get(19)!=m.getFrom() && ((Case)board.getNodes().get(20)).getPions()==currentPlayer && board.getNodes().get(20)!=m.getFrom())
                        || (((Case)board.getNodes().get(3)).getPions()==currentPlayer && board.getNodes().get(3)!=m.getFrom() && ((Case)board.getNodes().get(10)).getPions()==currentPlayer && board.getNodes().get(10)!=m.getFrom());

            case 'T':
                return (((Case)board.getNodes().get(18)).getPions()==currentPlayer && board.getNodes().get(18)!=m.getFrom() && ((Case)board.getNodes().get(20)).getPions()==currentPlayer && board.getNodes().get(20)!=m.getFrom())
                        || (((Case)board.getNodes().get(16)).getPions()==currentPlayer && board.getNodes().get(16)!=m.getFrom() && ((Case)board.getNodes().get(22)).getPions()==currentPlayer && board.getNodes().get(22)!=m.getFrom());

            case 'U':
                return (((Case)board.getNodes().get(18)).getPions()==currentPlayer && board.getNodes().get(18)!=m.getFrom() && ((Case)board.getNodes().get(19)).getPions()==currentPlayer && board.getNodes().get(19)!=m.getFrom())
                        || (((Case)board.getNodes().get(5)).getPions()==currentPlayer && board.getNodes().get(5)!=m.getFrom() && ((Case)board.getNodes().get(13)).getPions()==currentPlayer && board.getNodes().get(13)!=m.getFrom());

            case 'V':
                return (((Case)board.getNodes().get(22)).getPions()==currentPlayer && board.getNodes().get(22)!=m.getFrom() && ((Case)board.getNodes().get(23)).getPions()==currentPlayer && board.getNodes().get(23)!=m.getFrom())
                        || (((Case)board.getNodes().get(0)).getPions()==currentPlayer && board.getNodes().get(0)!=m.getFrom() && ((Case)board.getNodes().get(9)).getPions()==currentPlayer && board.getNodes().get(9)!=m.getFrom());

            case 'W':
                return (((Case)board.getNodes().get(21)).getPions()==currentPlayer && board.getNodes().get(21)!=m.getFrom() && ((Case)board.getNodes().get(23)).getPions()==currentPlayer && board.getNodes().get(23)!=m.getFrom())
                        || (((Case)board.getNodes().get(16)).getPions()==currentPlayer && board.getNodes().get(16)!=m.getFrom() && ((Case)board.getNodes().get(19)).getPions()==currentPlayer && board.getNodes().get(19)!=m.getFrom());

            case 'X':
                return (((Case)board.getNodes().get(21)).getPions()==currentPlayer && board.getNodes().get(21)!=m.getFrom() && ((Case)board.getNodes().get(22)).getPions()==currentPlayer && board.getNodes().get(22)!=m.getFrom())
                        || (((Case)board.getNodes().get(2)).getPions()==currentPlayer && board.getNodes().get(2)!=m.getFrom() && ((Case)board.getNodes().get(14)).getPions()==currentPlayer && board.getNodes().get(14)!=m.getFrom());

            default:
                return false;
        }
    }

    public int nbMoulin(int player){
        int res = 0;
        for(int i=0; i<23; i++){
            if(((Case)board.getNodes().get(i)).getPions()==player){
                Move m = new Move((Case)board.getNodes().get(i),(Case)board.getNodes().get(i),Case.CASE_VIDE);
                if(moulin(m, player))res++;
            }
        }
        return res/3;
    }

    public int nbMove(int player){
        Board b = new Board(player,pionRestants,pionVivant,board);
        b.pionRestants[0]=0;
        b.pionRestants[1]=0;
        b.pionVivant[0]=9;
        b.pionVivant[1]=9;
        return b.getMoves().size();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Node n : getBoard().getNodes()){
            sb.append(((Case)n).pions+ " ");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass()!=Board.class){
            return false;
        }else{
            Board b = (Board)obj;
            if(currentPlayer!=b.currentPlayer)return false;
            if(pionVivant[0]!=b.pionVivant[0])return false;
            if(pionVivant[1]!=b.pionVivant[1])return false;
            if(pionRestants[0]!=b.pionRestants[0])return false;
            if(pionRestants[1]!=b.pionRestants[1])return false;
            if(compteur!=b.compteur)return false;
            for(int i=0; i<=23; i++){
                if(((Case)board.getNodes().get(i)).getPions()!=((Case)b.board.getNodes().get(i)).getPions());
            }
        }
        return true;
    }

    public String code(){
        StringBuilder sb = new StringBuilder();
        sb.append(""+currentPlayer + pionRestants[0]+pionRestants[1]);
        for(Node c : board.getNodes()){
            sb.append(((Case)c).pions);
        }
        return sb.toString();
    }
}
