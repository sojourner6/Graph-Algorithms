package sojourner;
import java.util.ArrayList;

public class BELLMAN_FORD_MOORE {
	Graph g;
	Vertex s;
	String bellmanPath = "";
	boolean negativeWeightCycle = false;
	public BELLMAN_FORD_MOORE(Graph g, int s){
		this.g = g;
		this.s = this.g.getVertexFromId(s);
	}
	public boolean hasNegtiveWeightCycle(int n){
		this.s = this.g.getVertexFromId(n);
		this.s.resetDistance();
		ArrayList<Vertex> S = new ArrayList<Vertex>();
		S.add(this.s);
		Algorithm(S, 0);
		/*
		 * if d(v) > d(u) + w(uv), then d(u) + w(uv) should have been the minimum weight, not d(v), and Bellman-Ford should have found it.
		 * it only indicates a negative cycle, which reduced the distance of the child node, and not the predecessor.
		 */
		for(Edge e : this.g.getEdges()){
			if(e.getTo().getDistance() > (e.getFrom().getDistance() + e.getCapacity())){
				this.negativeWeightCycle = true;
			}
		}
		return this.negativeWeightCycle;
	}
	/*
	 * Implementation of Bellman-Ford-Moore Algorithm for finding a Negative-weight Cycle in G
	 * Primary Reference - Graphs: Theory and Algorithms, Thulasiraman, Swamy.
	 * Secondary Reference - Introduction to Algorithms, CLRS
	 * S  = { vertices for which shortest path has NOT been determined }
	 */
	private void Algorithm(ArrayList<Vertex> S, int iterations){
		if(S.size() == 0){
			return;
		}
		if(iterations > (this.g.getTotalNumberVertices() - 1)){
			//this.negativeWeightCycle = true;
			return;
		}
		ArrayList<Vertex> S1 = new ArrayList<Vertex>();
		for(Vertex u : S){
			ArrayList<Vertex> adj = this.g.getAdjacencyList(u.getId());
			for(Vertex v : adj){
				Edge e = this.g.getEdgeObject(u.getId(), v.getId());
				double tempDist = u.getDistance() + e.getCapacity();
				if(tempDist < v.getDistance()){
					v.setDistance(tempDist);
					v.setPredecessor(u);
					if(!S1.contains(v)){
						S1.add(v);
					}
				}
			}
		}
		iterations++;
		Algorithm(S1, iterations);
	}
	public String getPath(int n){
		try{
			Vertex v = this.g.getVertexFromId(n);
			if(v.getId() == this.s.getId()){
				return v.getId().toString() + ",";
			}
			else{
				if(v.getPredecessor() == null){
					return "Path not Found";
				}
				else{
					this.bellmanPath += getPath(v.getPredecessor().getId()) + v.getId().toString()
							+ "(" + v.getDistance().toString() + "),";
					return this.bellmanPath;
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
