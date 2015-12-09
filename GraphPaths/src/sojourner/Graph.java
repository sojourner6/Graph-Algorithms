package sojourner;
import java.util.ArrayList;
public class Graph {
	static int WHITE = 0;
	static int GRAY = 1;
	static int BLACK = 2;
	ArrayList<Vertex> v;
	ArrayList<ArrayList<Vertex>> e;
	ArrayList<Edge> edg; // Used in FORD_FULKERSON.java, otherwise redundant
	public Graph()
	{
		v = new ArrayList<Vertex>();
		e = new ArrayList<ArrayList<Vertex>>();
		for(int i=0; i<100; i++){
			e.add(new ArrayList<Vertex>());
		}
		edg = new ArrayList<>();
	}
	public Vertex getVertexFromId(int id){
		for(Vertex v : this.v){
			if(v.getId() == id) return v;
		}
		return null;
	}
	public ArrayList<Edge> getEdges(){
		return this.edg;
	}
	public ArrayList<Vertex> getAdjacencyList(int n){
		//Vertex s = getVertexFromId(n);
		return e.get(n-1);
	}
	public ArrayList<Edge> getAdjacentEdges(int uid){
		ArrayList<Edge> adjEdges = new ArrayList<Edge>();
		for(Edge eTemp : this.edg){
			if(eTemp.getFrom().getId() == uid){
				adjEdges.add(eTemp);
			}
		}
		return adjEdges;
	}
	public void addVertex(int n){
		v.add(new Vertex(new Integer(n)));
	}
	public void addEdge(int a, int b, double c){
		Vertex u1 = getVertexFromId(a);
		Vertex u2 = getVertexFromId(b);
		/* ----------------------------------------------------------
		 * Used only in FORD_FULKERSON.java, otherwise redundant.
		 */
		Edge tempEdge = new Edge(u1, u2, c);
		if(!duplicateEdge(tempEdge.getFrom().getId(), tempEdge.getTo().getId())){
			this.edg.add(new Edge(u1, u2, c)); // Used only in FORD_FULKERSON.java, otherwise redundant.
			(this.e.get(a-1)).add(u2); // adds to adjacency list
		}
		else{
			getEdgeObject(a, b).reduceFlow(c);
		}
		//------------------------------------------------------------
	}
	Boolean duplicateEdge(int u, int v){
		if(this.edg.size() == 0){
			return false;
		}
		for(Edge e1 : this.edg){
			if((e1.getFrom().getId() == u)
					&& (e1.getTo().getId() == v)){
				return true;
			}
		}
		return false;
	}
	Boolean duplicateVertex(int n){
		if(this.v.size() == 0){
			return false;
		}
		for(Vertex ver : this.v){
			if(ver.getId() == n){
				return true;
			}
		}
		return false;
	}
	public void removeEdge(int a, int b){
		this.e.get(a-1).remove(this.getVertexFromId(b));
		deleteEdgeFromList(a, b);
	}
	public ArrayList<Vertex> getAllVertices(){
		return this.v;
	}
	public String getAllVerticesString(){
		String v="";
		for(Vertex n : this.v){
			v += n.getId().toString()+", ";
		}
		return v;
	}
	public String getAllEdges(){
		String e="";
		for(Edge temp : this.edg){
			if(temp.isActive()){
				e += "(" + temp.getFrom().getId().toString() + ", " + temp.getTo().getId().toString() + "): "
						+ temp.getResidualCapacity() + "/" + temp.getCapacity() + ", ";
			}
		}
		return e;
	}
	public void initializeState(Vertex n){
		for(Vertex v : this.v){
			if(v.getId() != n.getId()){
				v.initialize();
			}
		}
		n.resetDistance();
		n.setColor(Graph.GRAY);
	}
	//------------------------------------------------
	public Edge getEdgeObject(int u, int v){
		for(Edge e : this.edg){
			if((e.getFrom().getId() == u)
					&& (e.getTo().getId() == v))
				return e;
		}
		return null;
	}
	public void deleteEdgeFromList(int u, int v){
		Edge temp = getEdgeObject(u, v);
		temp.deactivate();
	}
	/*
	 *  Returns the set of vertices reachable from a source (that is defined in the Runner program).
	 *  N.B: BFS has already been run, so I am just searching for vertices that are NOT "WHITE"
	 */
	public String verticesReachable(){
		String reachable = "{";
		for(Vertex vTemp : v){
			if(vTemp.getColor() != Graph.WHITE){
				reachable += vTemp.getId() + ", ";
			}
		}
		reachable += "}";
		return reachable;
	}
	/*
	 *  Returns the set of vertices NOT reachable from a source (that is defined in the Runner program).
	 *  N.B: BFS has already been run, so I am just searching for vertices that are "WHITE"
	 */
	public String verticesUnreachable(){
		String unReachable = "{";
		for(Vertex vTemp : v){
			if(vTemp.getColor() == Graph.WHITE){
				unReachable += vTemp.getId() + ", ";
			}
		}
		unReachable += "}";
		return unReachable;
	}
	public void setCurrentFlow(String f){
		String[] indivFlow = f.split(",");
		int count = 0;
		for(Edge edgeTemp : edg){
			edgeTemp.increaseFlow(Integer.parseInt(indivFlow[count++]));
		}
	}
	public void formScalingSubgraph(int delta){	
		for(Edge eTemp : this.edg){
			if(eTemp.getResidualCapacity() < delta){
				eTemp.deactivate();
			}
			else{
				eTemp.activate();
			}
		}
	}
	public int getTotalNumberVertices(){
		return this.v.size();
	}
	public int getTotalNumberEdges(){
		return this.edg.size();
	}
	/*
	 * Used in Dijkstra's Algorithm.
	 * Returns set of vertices for which
	 * the shortest path has NOT been determined
	 */
	public ArrayList<Vertex> getUnMeasuredVertices(){
		ArrayList<Vertex> temp = new ArrayList<Vertex>();
		for(Vertex vTemp : this.v){
			if(!vTemp.shortestDistFound()){
				temp.add(vTemp);
			}
		}
		return temp;
	}
	/*
	 * Used in Dijkstra's Algorithm.
	 * Returns set of vertices for which
	 * the shortest path has already been determined
	 */
	public ArrayList<Vertex> getMeasuredVertices(){
		ArrayList<Vertex> temp = new ArrayList<Vertex>();
		for(Vertex vTemp : this.v){
			if(vTemp.shortestDistFound()){
				temp.add(vTemp);
			}
		}
		return temp;
	}
	/*
	 * Used in Dijkstra's Algorithm.
	 * Returns the set of vertices adjacent to u, and across the cut
	 */
	public ArrayList<Vertex> getAdjacentVerticesAcrossCut(Vertex u, ArrayList<Vertex> A){
		ArrayList<Vertex> B = new ArrayList<Vertex>();
		ArrayList<Vertex> adj = this.getAdjacencyList(u.getId());
		for(Vertex vTemp : adj){
			if(!A.contains(vTemp)){
				B.add(vTemp);
			}
		}
		return B;
	}
	/*
	 * Used in Kruskal's Algorithm.
	 * Returns set of edges which are already part of the MST
	 */
	public ArrayList<Edge> getSpanningEdges(){
		ArrayList<Edge> temp = new ArrayList<Edge>();
		for(Edge eTemp : this.edg){
			if(eTemp.isInSpanningTree()){
				temp.add(eTemp);
			}
		}
		return temp;
	}
	/*
	 * Used in Kruskal's Algorithm.
	 * Returns set of edges which are NOT yet part of the MST
	 */
	public ArrayList<Edge> getUnSpanningEdges(){
		ArrayList<Edge> temp = new ArrayList<Edge>();
		for(Edge eTemp : this.edg){
			if((eTemp.getFrom().isInMst() && !eTemp.getTo().isInMst())
					|| (!eTemp.getFrom().isInMst() && eTemp.getTo().isInMst())){
				temp.add(eTemp);
				System.out.print(eTemp.getEdgeString());
			}
		}
		System.out.println();
		return temp;
	}
}

