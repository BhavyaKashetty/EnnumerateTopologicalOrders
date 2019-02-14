/* 
 * @author - Bhavya Kashetty
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import rbk.Graph;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
    boolean print;  // Set to true to print array in visit
    long count;      // Number of permutations or combinations visited
    Selector sel;
    ArrayList<String> vistedPaths = new ArrayList<>();
    HashSet<String> h = new HashSet<String>(); 
    public EnumerateTopological(Graph g) {
	super(g, new EnumVertex());
	print = false;
	count = 0;
	sel = new Selector();
    }

    static class EnumVertex implements Factory {
    	int indegree;
    	boolean visited;
	EnumVertex() { }
	EnumVertex(Vertex u){
		indegree = 0;
		visited = false;
	}
	public EnumVertex make(Vertex u) { return new EnumVertex();	}
    }

    class Selector extends Enumerate.Approver<Vertex> {
	@Override
	public boolean select(Vertex u) {
	    return true;
	}

	@Override
	public void unselect(Vertex u) {
	}

	@Override
	public void visit(Vertex[] arr, int k) {
	    count++;
	    if(print) {
		for(Vertex u: arr) {
		    System.out.print(u + " ");
		}
		System.out.println();
	    }
	}
    } 
    
  
    void enumerationPath(Vertex v,int i,Vertex[] arr) {
    	arr[i] = v;
		//if the number of nodes visited is equal to the size of array,
		//calling the visit method
    	if(i == g.size()-1) {
    		String path = Arrays.toString(arr);
    		if (h.contains(path) == false) {
    			h.add(path);
    			sel.visit(arr, g.size());
    		}
    	} else {
    	   	//Copying the visited nodes into another array
        	Vertex[] visitedNodes = new Vertex[arr.length];		
    		System.arraycopy(arr, 0, visitedNodes, 0, arr.length);
    		//For the node that is visited, 
    		//decreasing the indegree of the nodes to the other edges
    		for(Edge e:g.outEdges(v)) {
    			Vertex w = e.toVertex();
    			if(get(w).indegree > 0) get(w).indegree = get(w).indegree - 1;
    		}
    		int[] indegress = new int[arr.length];
    		int a=0;
    		//Nodes which have indegree as zero, making them as next nodes visited that have to visited
    		ArrayDeque<Vertex> zeroq = new ArrayDeque<>();
    		for(Vertex x:g) {
    			if(get(x).indegree == 0) {
    				if(Arrays.asList(arr).contains(x) == false) zeroq.add(x);
    			}
    			indegress[a] = get(x).indegree ;
    			a++;
    		}
    		int runWhileloop = zeroq.size();
    		int p = i;
    		while(runWhileloop>0) {
    			for(Vertex j:zeroq) enumerationPath(j, ++i, arr);
    			System.arraycopy(visitedNodes, 0, arr, 0, arr.length);
    			a = 0;
    			for(Vertex q:g) {
    				if(Arrays.asList(arr).contains(q) == false) get(q).indegree = indegress[a];					 
    				a++;
    			}
    			//once a path is visited, restoring the original values to visit the next path
    			i = p;
    			Vertex y = zeroq.removeFirst();
    			zeroq.addLast(y);
    			runWhileloop--;
    		}
    	}
    }
    
       
    
    // To do: LP4; return the number of topological orders of g
    public long enumerateTopological(boolean flag) {
    	print = flag;
     	for(Vertex u:g) {
    		get(u).indegree = u.inDegree();
    	}
    	//for all the vertices which have indegree as zero making them as first visiting node
    	//Continues for each node which has indegree as zero
    	for(Vertex v:g) {
    		if(v.inDegree() == 0) {
    			Vertex[] arr = new Vertex[g.size()];
    			enumerationPath(v, 0, arr);
    	   		//Once all the paths are visited with one starting node
        		//Restoring the indegrees of all the nodes for the visits on the node 
    			for(Vertex w:g) {
    	    		get(w).indegree = w.inDegree();
    	    	}
      		}
    	}
    	return count;
    }

    //-------------------static methods----------------------

    public static long countTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
	return et.enumerateTopological(false);
    }

    public static long enumerateTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
	return et.enumerateTopological(true);
    }

    public static void main(String[] args) {
	int VERBOSE = 0;
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
        Graph.Timer t = new Graph.Timer();
        long result;
	if(VERBOSE > 0) {
	    result = enumerateTopologicalOrders(g);
	} else {
	    result = countTopologicalOrders(g);
	}
        System.out.println("\n" + result + "\n" + t.end());
    }

}
