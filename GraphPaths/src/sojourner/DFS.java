package sojourner;

import java.util.ArrayList;

public class DFS {
	Graph g;
	Vertex s;
	ArrayList<Vertex> Q;
	String DFSPath = "";
	ArrayList<Edge> BFSPathEdgeFormat; // Used only in FORD_FULKERSON.java, otherwise redundant.
	Boolean DFSPathFound = true;
	public DFS(Graph g, int s){
		this.g = g;
		this.s = this.g.getVertexFromId(s);
		Q = new ArrayList<Vertex>();
		BFSPathEdgeFormat = new ArrayList<Edge>();
		this.g.initializeState(this.s);
	}
	/*
	 * Actual Implementation of DFS
	 */
	public void performSearch(int n){
		//Vertex v = this.g.getVertexFromId(n);
		this.g.getVertexFromId(n).setVisitedTrue();
		ArrayList<Vertex> adj = this.g.getAdjacencyList(n);
		if(adj.size() == 0){
			return;
		}
		else{
			for(Vertex v : adj){
				//int child = v.getId();
				if(!v.wasVisited()){
					performSearch(v.getId());
					v.setPredecessor(this.g.getVertexFromId(n));
				}
			}
		}
	}

	public String getPath(int n){
		try{
			Vertex v = this.g.getVertexFromId(n);
			if(v.getId() == this.s.getId()){
				//this.DFSPath += v.getId().toString() + ",";
				return v.getId().toString() + ",";
			}
			else{
				if(v.getPredecessor() == null){
					this.DFSPathFound = false;
					//this.DFSPath = "Path Not found";
					return "Path not Found";
				}
				else{
					//getPath(v.getPredecessor().getId());
					this.DFSPath += getPath(v.getPredecessor().getId()) + v.getId().toString() + ",";
					return this.DFSPath;
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
}
