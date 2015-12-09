package sojourner;
import java.util.ArrayList;

public class BFS {
	Graph g;
	Vertex s;
	ArrayList<Vertex> Q;
	String BFSPath = "";
	ArrayList<Edge> BFSPathEdgeFormat; // Used only in FORD_FULKERSON.java, otherwise redundant.
	Boolean BFSPathFound = true;
	public BFS(Graph g, int s){
		this.g = g;
		this.s = this.g.getVertexFromId(s);
		Q = new ArrayList<Vertex>();
		BFSPathEdgeFormat = new ArrayList<Edge>();
	}
	void initializeBFSPath(){
		this.BFSPath = "";
	}
	void initializeBFSPathEdge(){
		this.BFSPathEdgeFormat = new ArrayList<Edge>();
	}
	public String getBFSPath(){
		return this.BFSPath;
	}
	public ArrayList<Edge> getBFSPathEdges(){
		return this.BFSPathEdgeFormat;
	}
	void ENQUEUE(Vertex n){
		this.Q.add(n);
	}
	Vertex DEQUEUE(){
		Vertex r = this.Q.get(0);
		this.Q.remove(r);
		return r;
	}
	/*
	 * Actual Implementation of BFS. Based on implementation of BFS(G, s), CLRS section 22.2
	 * This implementation also finds cycles
	 */
	
	public void performSearch(){
		this.g.initializeState(this.s);
		ENQUEUE(s);
		while(Q.size()>=1){
			Vertex u = DEQUEUE();
			ArrayList<Vertex> adjList = g.getAdjacencyList(u.getId());
			for(Vertex v : adjList){
				if(v.getColor() == Graph.WHITE){
					v.setColor(Graph.GRAY);
					v.setDistance(u.getDistance() + 1);
					v.setPredecessor(u);
					ENQUEUE(v);
				}
			}
			u.setColor(Graph.BLACK);
		}
	}
	
	public void performSearchNew(int n){
		if(n == this.s.getId()){
			this.g.initializeState(this.s);
			this.g.getVertexFromId(n).setVisitedTrue();
		}
		ArrayList<Vertex> descendants = new ArrayList<Vertex>();
		//ENQUEUE(s);
		ArrayList<Vertex> adj = this.g.getAdjacencyList(n);
		if(adj.size() == 0){
			return;
		}
		else{
			for(Vertex v : adj){
				if(!v.wasVisited()){
					v.setVisitedTrue();
					descendants.add(v);
					v.setPredecessor(this.g.getVertexFromId(n));
					v.setDistance(this.g.getVertexFromId(n).getDistance() + 1);
					//performSearchNew(v.getId());
				}
			}
			for(Vertex v : descendants){
				performSearchNew(v.getId());
			}
		}
	}
	/*
	public void performSearchOnActiveEdge(){
		this.g.initializeState(this.s);
		ENQUEUE(s);
		while(Q.size()>=1){
			Vertex u = DEQUEUE();
			ArrayList<Vertex> adjList = g.getAdjacencyList(u.getId());
			for(Vertex v : adjList){
				Edge e = this.g.getEdgeObject(u.getId(), v.getId());
				if(e.isActive() && (v.getColor() == Graph.WHITE)){
					v.setColor(Graph.GRAY);
					v.setDistance(u.getDistance() + 1);
					v.setPredecessor(u);
					ENQUEUE(v);
				}
			}
			u.setColor(Graph.BLACK);
		}
	}
	*/
	public void performSearchOnActiveEdge(int n){
		if(n == this.s.getId()){
			this.g.initializeState(this.s);
			this.g.getVertexFromId(n).setVisitedTrue();
		}
		ArrayList<Vertex> descendants = new ArrayList<Vertex>();
		//ENQUEUE(s);
		ArrayList<Vertex> adj = this.g.getAdjacencyList(n);
		if(adj.size() == 0){
			return;
		}
		else{
			for(Vertex v : adj){
				Edge e = this.g.getEdgeObject(n, v.getId());
				if(e.isActive() && !v.wasVisited()){
					v.setVisitedTrue();
					descendants.add(v);
					v.setPredecessor(this.g.getVertexFromId(n));
					v.setDistance(this.g.getVertexFromId(n).getDistance() + 1);
					//performSearchNew(v.getId());
				}
			}
			for(Vertex v : descendants){
				performSearchNew(v.getId());
			}			
		}
	}
	/*
	 * forms a String representation of an s-t path. Similar to PRINT-PATH(G, s, v), Lemma 22.6: Algorithm in CLRS book.
	 */
	void printPath(Vertex v){
		if(v.getId() == this.s.getId()){
			this.BFSPath += v.getId().toString() + ",";
			
		}
		else{
			if(v.getPredecessor() == null){
				this.BFSPathFound = false;
			}
			else{
				printPath(v.getPredecessor());
				this.BFSPath += v.getId().toString() + ",";
			}
		}
	}
	/*
	 * Returns the BFS path found, in STRING format
	 */
	public Boolean getPath(int n){
		initializeBFSPath();
		printPath(g.getVertexFromId(n));
		return this.BFSPathFound;
	}
	public String getPathNew(int n){
		try{
			Vertex v = this.g.getVertexFromId(n);
			if(v.getId() == this.s.getId()){
				//this.DFSPath += v.getId().toString() + ",";
				return v.getId().toString() + ",";
			}
			else{
				if(v.getPredecessor() == null){
					//this.DFSPath = "Path Not found";
					return "Path not Found";
				}
				else{
					//getPath(v.getPredecessor().getId());
					this.BFSPath += getPathNew(v.getPredecessor().getId()) + v.getId().toString()
							+ "(" + v.getDistance().toString() + "),";
					return this.BFSPath;
				}
			}
		}
		catch(NullPointerException eNull){
			return "Invalid Destination: " + n;
		}
		catch(Exception e){
			return "Unknown Error encountered";
		}
	}
	/*
	 * Returns the BFS path found, in ArrayList<Edge> format
	 */
	public String s_tPath(){
		String path = "";
		initializeBFSPathEdge();
		//System.out.println("BFS.s_tPath:89 Path: " + this.BFSPath + " ----- remove later");
		String[] s = this.BFSPath.split(",");
		for(int i=0; i<s.length-1; i++){
			Edge e = this.g.getEdgeObject(Integer.parseInt(s[i]), Integer.parseInt(s[i+1]));
			this.BFSPathEdgeFormat.add(e);
		}
		for(Edge e : this.BFSPathEdgeFormat){
			path += e.getEdgeString();
			//System.out.println(e.getEdgeString());
		}
		return path;
	}
	/*
	 * Returns the capacity of minimum weight edge in g
	 */
	public double getMinimumWeightEdgeInSTPath(){
		double bottleneck = 999999;
		for(Edge e : this.BFSPathEdgeFormat){
			if(bottleneck > e.getResidualCapacity()){
				bottleneck = e.getResidualCapacity();
			}
		}
		return bottleneck;
	}
}