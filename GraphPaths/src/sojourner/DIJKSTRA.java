package sojourner;
import java.util.ArrayList;

public class DIJKSTRA {
	Graph g;
	Vertex s;
	String dijkstraPath = "";
	public DIJKSTRA(Graph g, int s){
		this.g = g;
		this.s = this.g.getVertexFromId(s);
	}
	public void getShortestDistances(int n){
		this.s = this.g.getVertexFromId(n);
		Algorithm(new ArrayList<Vertex>(), this.g.getAllVertices());
	}
	/*
	 * Implementation of Dijkstra's Algorithm for finding the shortest s,t-path
	 * Reference - Graphs: Theory and Algorithms, Thulasiraman, Swamy.
	 * S  = { vertices for which shortest path has been determined }
	 * Si = { vertices for which shortest path has NOT been determined }
	 */
	private void Algorithm(ArrayList<Vertex> S0, ArrayList<Vertex> Si){
		/*
		 * when shortest paths of all vertices have been determined 
		 */
		if(Si.size() == 0){
			return;
		}
		/*
		 * when S0 = {s}
		 */
		if(S0.size() == 0){
			Vertex ref = this.g.getVertexFromId(this.s.getId());
			this.g.getVertexFromId(ref.getId()).resetDistance();
			this.g.getVertexFromId(ref.getId()).setShortestDistTrue();
			Algorithm(this.g.getMeasuredVertices(), this.g.getUnMeasuredVertices());
		}
		/*
		 * when S0 = {s, u1, u2, .... }
		 */
		else{
			
			for(Vertex v2 : S0){
				System.out.print(v2.getId() + ", ");
			}
			System.out.print("\n");
			
			double lightEdge = 999999.99;
			Vertex vMin = null, vPredecessor = null;
			/*
			 * Overall Loop in the order of O(|V|)
			 */
			for(Vertex v : S0){
				ArrayList<Vertex> adj = this.g.getAdjacentVerticesAcrossCut(v, S0);
				if(adj.size() == 0){ 	// vertex v has no outgoing edges => its current distance from s is its shortest distance
					v.setShortestDistTrue();
				}
				else{
					/*
					 * Overall Loop in the order of O(|E|)
					 */
					for(Vertex v1 : adj){
						Edge e = this.g.getEdgeObject(v.getId(), v1.getId());
						double d_uv1 = v.getDistance() + e.getCapacity();
						if(d_uv1 < v1.getDistance()){
							v1.setDistance(d_uv1);
						}
						if(d_uv1 < lightEdge){
							vMin = v1;
							vPredecessor = v;
							lightEdge = d_uv1;
						}
					}
					
				}
			}
			if(vMin != null){
				vMin.setPredecessor(vPredecessor);
				vMin.setDistance(lightEdge);
				this.g.getVertexFromId(vMin.getId()).setShortestDistTrue();
				Algorithm(this.g.getMeasuredVertices(), this.g.getUnMeasuredVertices());
			}
		}
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
					this.dijkstraPath += getPath(v.getPredecessor().getId()) + v.getId().toString()
							+ "(" + v.getDistance().toString() + "),";
					return this.dijkstraPath;
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
	public String getAllVerticesDistances(){
		String ret = "";
		for(Vertex v : this.g.getAllVertices()){
			ret += "'" + v.getId() + "'" + ": " + v.getDistance() + "\n";
		}
		return ret;
	}
}
