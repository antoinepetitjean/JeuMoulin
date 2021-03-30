package modele;

import java.util.ArrayList;

public class GestionnnaireThread {
    protected ArrayList<Thread> threads;
    public static GestionnnaireThread instance = new GestionnnaireThread();

    private GestionnnaireThread(){
        this.threads=new ArrayList<>();
    }

    public static GestionnnaireThread getInstance(){
        return instance;
    }

    public void ajouter(Thread t){
        threads.add(t);
        t.start();
    }

    public void maj(){
        for(Thread t: threads){
            if(!t.isInterrupted()){
                threads.remove(t);
            }
        }
    }
}
