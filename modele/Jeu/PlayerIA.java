package modele.Jeu;

import modele.GestionnnaireThread;

import java.util.*;

public class PlayerIA implements Observer {
    private int player;
    public int depth;
    public float var1;
    public float var2;
    public HashMap<String, ArrayList<Board>> sauvegarde;

    public PlayerIA(int player, int depth, float var1, float var2){
        this.player=player;
        this.depth=depth;
        this.var1=var1;
        this.var2=var2;
        this.sauvegarde = new HashMap<>();
    }

    public Move getBestMove(Board b, int maxDepth){
        return b.getMoves().get(minmaxAB(b, maxDepth));
    }

    public int minmaxAB(Board b, int maxDepth){
        int res=0;
        float score;
        float max = Float.NEGATIVE_INFINITY;

        ArrayList<Board> boards = b.successeurs();

        for(int i=0; i<boards.size(); i++){
            score = evaluationAB(maxDepth,boards.get(i),Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY);
            System.out.println("score " + score);
            if(score>=max){
                res=i;
                max=score;
            }
        }
        if(max == Float.POSITIVE_INFINITY && this.depth>2){
            this.depth-=2;
        }
        return res;
    }

    public float evaluationAB(int c, Board e, float a, float b){
        if(e.isGameOver() || e.isTieGame() || c==0){
            return e.evaluate(player,var1, var2);
        }
        ArrayList<Board> s =e.successeurs();

        if(e.getCurrentPlayer()==player){
            float max = Float.NEGATIVE_INFINITY;
            for(Board bo :s){
                max = Float.max(max, evaluationAB(c-1, bo, a,b));
                if(max>=b){
                    return max;
                }
                a = Float.max(a,max);
            }
            return max;
        }
        else{
            float min = Float.POSITIVE_INFINITY;
            for(Board bo :s){
                min = Float.min(min, evaluationAB(c-1, bo, a,b));
                if(min<=a){
                    return min;
                }
                b = Float.max(b,min);
            }
            return min;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass().equals(Board.class)) {
            Board b = (Board) o;
            if ((b.currentPlayer == player) && (!b.isGameOver()) && (!b.isTieGame())) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Move m;
                        if(b.pionRestants[1]>0){
                            m = getBestMove(b, depth-2);
                        }else{
                            m = getBestMove(b, depth);
                        }
                        b.makeMove(m);
                        Thread.currentThread().interrupt();
                    }
                });
                GestionnnaireThread.getInstance().ajouter(t);
            }
        }
    }
}
