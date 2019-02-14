/**
 *  Bhavya Kashetty 
 */

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    enum color{
        WHITE,BLACK,GREY;
    }
    boolean isCyclic;
    int topNum;
    LinkedList<Vertex> finishList;
    public static class DFSVertex implements Factory {
        int cno;
        Vertex parent;
        color nodeColor;
        int top;
        DFSVertex(){

        }
        public DFSVertex(Vertex u) {
            this.cno = 0;
            this.parent = null;
            this.nodeColor = color.WHITE;
            this.top = 0;
        }
        public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }

    DFSVertex d = new DFSVertex();
    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        this.finishList = new LinkedList<>();
        this.isCyclic = false;
        this.topNum = g.size();
    }

    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        for (Vertex u: g) {
            if(d.get(u).nodeColor == color.WHITE){
                d.dfsVisit(u);
            }
        }
        return d;
    }

    // Member function to find topological order
    public List<Vertex> topologicalOrder1() {
        DFS d = DFS.depthFirstSearch(this.g);
        return d.isCyclic ? null : d.finishList;
    }
    void dfsVisit(Vertex u){
        get(u).nodeColor = color.GREY;
        for(Edge e:g.outEdges(u)) {
            Vertex d2 = e.toVertex();
            if(get(d2).nodeColor == color.GREY) {
                this.isCyclic = true;
                return;
            }
            else if(get(d2).nodeColor == color.WHITE) {
                get(d2).parent = u;
                dfsVisit(e.toVertex());
                if(this.isCyclic)return;
            }
        }
        get(u).nodeColor = color.BLACK;
        get(u).top = this.topNum--;
        this.finishList.addFirst(u);
    }



    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        return 0;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        if(!g.isDirected()) return null;
        DFS d = new DFS(g);
        return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

//    public static void main(String[] args) throws Exception {
////        String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";
////	     String string = "2 2   1 2 2   2 1 2 0";
////        String string = "2 0 0";
//        String string = "5 5   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1 0";
//        Scanner in;
//        // If there is a command line argument, use it as file from which
//        // input is read, otherwise use input from string.
//        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
//
//        // Read graph from input
//        Graph g = Graph.readGraph(in,true);
//        g.printGraph(true);
//
//        List <Vertex> result = DFS.topologicalOrder1(g);
//        if(result == null)
//            System.out.println("Graph is not DAG");
//        else
//            System.out.println(result);
//        /*int numcc = d.connectedComponents();
//        System.out.println("Number of components: " + numcc + "\nu\tcno");
//        for(Vertex u: g) {
//            System.out.println(u + "\t" + d.cno(u));
//        }*/
//    }
}
