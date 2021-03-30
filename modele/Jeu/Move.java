package modele.Jeu;

public class Move {
    protected Case from;
    protected Case to;
    protected Case out;

    public Move(Case from, Case to, Case out) {
        this.from = from;
        this.to = to;
        this.out = out;
    }

    public Case getFrom() {
        return from;
    }

    public void setFrom(Case from) {
        this.from = from;
    }

    public Case getTo() {
        return to;
    }

    public void setTo(Case to) {
        this.to = to;
    }

    public Case getOut() {
        return out;
    }

    public void setOut(Case out) {
        this.out = out;
    }

    public String toString(){
        return ""+to.getIdf()+from.getIdf()+out.getIdf();
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() == Move.class){
            return ((Move)o).toString()==toString();
        }
        return false;
    }
}
