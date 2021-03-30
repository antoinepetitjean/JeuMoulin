package modele.Graph;

import java.util.ArrayList;

public class Graph {
    protected ArrayList<Node> nodes;
    protected ArrayList<Connection> connections;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    public ArrayList<Node> getNodes(){
        return nodes;
    }

    public ArrayList<Connection> getConnections(){
        return connections;
    }

    public ArrayList<Connection> getConnectionsFrom(int index){
        ArrayList<Connection> res = new ArrayList<>();
        for(Connection con : connections){
            if(con.from==getNodes().get(index))
                res.add(con);
        }
        return res;
    }

    public void addNode(Node n){
        nodes.add(n);
    }

    public void connect(int from, int to, int cost){
        connections.add(new Connection(nodes.get(from),nodes.get(to),cost));
        connections.add(new Connection(nodes.get(to),nodes.get(from),cost));
    }

    public int getIndex(Node n){
        return nodes.indexOf(n);
    }
}
