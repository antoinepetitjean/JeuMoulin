package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import modele.Graph.Node;
import modele.Jeu.Board;
import modele.Jeu.Case;
import modele.Jeu.Move;
import modele.Jeu.PlayerIA;


import java.util.*;

public class Controller extends Observable implements Observer {
    @FXML
    private Pane grid;
    @FXML
    private Button btn_a;
    @FXML
    private Button btn_b;
    @FXML
    private Button btn_c;
    @FXML
    private Button btn_d;
    @FXML
    private Button btn_e;
    @FXML
    private Button btn_f;
    @FXML
    private Button btn_g;
    @FXML
    private Button btn_h;
    @FXML
    private Button btn_i;
    @FXML
    private Button btn_j;
    @FXML
    private Button btn_k;
    @FXML
    private Button btn_l;
    @FXML
    private Button btn_m;
    @FXML
    private Button btn_n;
    @FXML
    private Button btn_o;
    @FXML
    private Button btn_p;
    @FXML
    private Button btn_q;
    @FXML
    private Button btn_r;
    @FXML
    private Button btn_s;
    @FXML
    private Button btn_t;
    @FXML
    private Button btn_u;
    @FXML
    private Button btn_v;
    @FXML
    private Button btn_w;
    @FXML
    private Button btn_x;
    @FXML
    private Label lbl_info;

    private Board board;

    private int state;
    private Move temp;


    public Controller(){
        board = new Board(0);
        board.addObserver(new PlayerIA(1,5,0.3f,0.0001f));
        board.addObserver(this);
        state=0;

    }

    @FXML
    public void initialize() throws Exception {
        board.begin();
    }

    public Board getBoard(){
        return board;
    }

    @FXML
    public void move(ActionEvent event) throws Exception {
        String s = ((Button)event.getSource()).getText();
        ArrayList<Move> moves = new ArrayList<>();

        if(board.getPionRestants()[board.getCurrentPlayer()]>0) {
            if(state==0) {
                for (Move m : board.getMoves()) {
                    if (m.getTo().getIdf() == s.charAt(0)) {
                        moves.add(m);
                    }
                }
                if (moves.size() == 1) {
                    board.makeMove(moves.get(0));

                } else if (moves.size() > 1) {
                    state = 1;
                    lbl_info.setText("Choisir le pion a supprimer");
                    temp = moves.get(0);
                } else {
                    lbl_info.setText("impossible");
                }
            }else if(state==1){
                for (Move m : board.getMoves()) {
                    if (m.getTo() == temp.getTo() && m.getOut().getIdf()==s.charAt(0)) {
                        moves.add(m);
                    }
                }
                if (moves.size() == 1) {
                    board.makeMove(moves.get(0));

                    state=0;
                } else {
                    lbl_info.setText("impossible");
                }
            }
        }else{
            if(state==0) {
                for (Move m : board.getMoves()) {
                    if (m.getFrom().getIdf() == s.charAt(0)) {
                        moves.add(m);
                    }
                }
                if (moves.size() >= 1) {
                    state = 1;
                    lbl_info.setText("Choisir la destination pion a supprimer");
                    temp = moves.get(0);
                } else {
                    lbl_info.setText("impossible");
                }
            }else if(state==1){
                for (Move m : board.getMoves()) {
                    if (m.getFrom() == temp.getFrom() && m.getTo().getIdf()==s.charAt(0)) {
                        moves.add(m);
                    }
                }
                if (moves.size() == 1) {
                    board.makeMove(moves.get(0));
                    state=0;
                } else if (moves.size()>1){
                    state = 2;
                    lbl_info.setText("Choisir le pion a supprimer");
                    temp = moves.get(0);
                } else{
                    lbl_info.setText("impossible");
                }
            }
            else if(state==2){
                for (Move m : board.getMoves()) {
                    if (m.getFrom() == temp.getFrom() && m.getTo()==temp.getTo() && m.getOut().getIdf()==s.charAt(0)) {
                        moves.add(m);
                    }
                }
                if (moves.size() == 1) {
                    board.makeMove(moves.get(0));
                    state=0;
                } else{
                    lbl_info.setText("impossible");
                }
            }
        }

}





    @Override
    public void update(Observable o, Object arg) {
        if(o.getClass().getSimpleName().equals("Board")){
            Board b = (Board)o;
            for(Node c : b.getBoard().getNodes()){
                if(((Case)c).getPions()==0){
                    for(javafx.scene.Node child : grid.getChildren()){
                        if(child.getClass().equals(Button.class)){
                            if(((Button)child).getText().charAt(0)==((Case)c).getIdf()){
                                child.setStyle("-fx-background-color: blue;");
                            }
                        }
                    }
                }
                else if(((Case)c).getPions()==1){
                    for(javafx.scene.Node child : grid.getChildren()){
                        if(child.getClass().equals(Button.class)){
                            if(((Button)child).getText().charAt(0)==((Case)c).getIdf()){
                                child.setStyle("-fx-background-color: red;");
                            }
                        }
                    }
                }
                else{
                    for(javafx.scene.Node child : grid.getChildren()){
                        if(child.getClass().equals(Button.class)){
                            if(((Button)child).getText().charAt(0)==((Case)c).getIdf()){
                                child.setStyle("-fx-background-color: lightgrey;");
                            }
                        }
                    }
                }
            }
            if(b.isGameOver()){
                int gagnant = (b.getCurrentPlayer()+1)%2;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Le gagnant est le joueur "+ gagnant);
                    }
                });
                t.start();
            }
            if(b.isTieGame()){
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Egalité!");
                    }
                });
                t.start();
            }
        }
    }
}
