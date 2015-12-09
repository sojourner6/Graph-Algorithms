package sojourner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Runner {
	static int SOURCE = 1;
	static int DESTINATION = 8;
	static int FACTOR = 2;
	static ArrayList<String> readIOFile(String path){
		ArrayList<String> lines = new ArrayList<String>();
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){	
			String line = null;
			while((line = reader.readLine())!=null){
				lines.add(line);
			}
			reader.close();
			return lines;
			//----------------------------------
		}
		catch(Exception e){
			System.out.println("Error: "+e.toString());
			e.printStackTrace();
			return lines;
		}
	}

	public static void main(String[] args) {
		Graph g = new Graph();
		ArrayList<String> fileData = readIOFile("graph.txt");
		// Adding Vertices
		String[] v = fileData.get(0).split(",");
		for(String s : v){
			int n = Integer.parseInt(s);
			g.addVertex(n);
		}
		//Adding Edges in the form of adjacency list
		String[] e = fileData.get(1).split(";");
		String[] c = fileData.get(2).split(",");
		@SuppressWarnings("unused")
		String currentFlow = "";
		if(fileData.size() > 3){
			currentFlow = fileData.get(3);
		}
		int flag = 0;
		for(String s : e){
			String[] ss = s.split(",");
			int a = Integer.parseInt(ss[0]);
			int b = Integer.parseInt(ss[1]);
			g.addEdge(a, b, Integer.parseInt(c[flag++]));
		}
		System.out.println(g.getAllVerticesString());
		System.out.println(g.getAllEdges());
		/*
		 * Below are the generic functions for Path exploration, Network flows.
		 * Call/Comment functions as you wish
		 */
		Paths(g);
		//NetworkFlow(g);
	}
	private static void Paths(Graph g){
		/*
		DFS dfs = new DFS(g, Runner.SOURCE);
		dfs.performSearch(Runner.SOURCE);
		System.out.println(dfs.getPath(Runner.DESTINATION));
		*/
		/*
		BFS bfs = new BFS(g, Runner.SOURCE);
		bfs.performSearchNew(Runner.SOURCE);
		System.out.println(bfs.getPathNew(Runner.DESTINATION));
		*/
		/*
		DIJKSTRA djk = new DIJKSTRA(g, Runner.SOURCE);
		djk.getShortestDistances(Runner.SOURCE);
		System.out.println(djk.getPath(Runner.DESTINATION));
		System.out.println(djk.getAllVerticesDistances());
		*/
		/*
		BELLMAN_FORD_MOORE bfm = new BELLMAN_FORD_MOORE(g, Runner.SOURCE);
		System.out.println("Is there a Negative Weight Cycle in G?: " + bfm.hasNegtiveWeightCycle(Runner.SOURCE));
		if(!bfm.hasNegtiveWeightCycle(Runner.SOURCE)){
			System.out.println(bfm.getPath(Runner.DESTINATION));
		}
		*/
		KRUSKAL krl = new KRUSKAL(g);
		krl.extractMinWeightSpanningTree();
		System.out.println("Minimum Spanning Tree: " + krl.getMST());
	}
	@SuppressWarnings("unused")
	private static void NetworkFlow(Graph g){
		FORD_FULKERSON ff = new FORD_FULKERSON(g, Runner.SOURCE, Runner.DESTINATION);
		// ------------- FF run from the beginning
		//ff.MAX_FLOW();
		// ------------- FF run from an intermediate stage
		//ff.MAX_FLOW_INTERMEDIATE(currentFlow);
		// ------------- FF run with Capacity Scaling
		ff.MAX_FLOW_CAPACITY_SCALING(Runner.FACTOR);
		System.out.println(g.getAllEdges());
		System.out.println("Minimum Cut: ");
		System.out.println(ff.MIN_CUT());
	}
}
