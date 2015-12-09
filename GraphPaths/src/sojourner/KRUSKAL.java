package sojourner;
import java.util.ArrayList;

public class KRUSKAL {
	Graph g;
	ArrayList<Edge> sortedEdges;
	boolean disjoint;
	Vertex root;
	ArrayList<Edge> spanningEdges;
	public KRUSKAL(Graph g){
		this.g = g;
		this.sortedEdges = new ArrayList<Edge>();
		this.disjoint = false;
		this.spanningEdges = new ArrayList<Edge>();
	}
	public void extractMinWeightSpanningTree(){
		this.sortedEdges = this.sortWeightedEdges(this.g.getEdges());
		createCuts(this.g.getAllVertices());
		Algorithm(this.sortedEdges);
		//Algorithm(0, this.sortedEdges);
	}
	/*
	 * SORT the edges in G, in non-decreasing order
	 * Merge Sort Implementation
	 */
	public ArrayList<Edge> sortWeightedEdges(ArrayList<Edge> S){
		if(S.size() <= 1){
			return S;
		}
		int pivot = S.size()/2;
		ArrayList<Edge> left = new ArrayList<Edge>();
		ArrayList<Edge> right = new ArrayList<Edge>();
		for(int i=0; i<pivot; i++){
			left.add(S.get(i));
		}
		for(int i=pivot; i<S.size(); i++){
			right.add(S.get(i));
		}
		//-------------------------------------------------
		ArrayList<Edge> A = sortWeightedEdges(left);
		ArrayList<Edge> B = sortWeightedEdges(right);
		//-------------------------------------------------
		ArrayList<Edge> C = new ArrayList<Edge>();
		while(A.size() > 0 && B.size() > 0){
			if(A.get(0).getCapacity() < B.get(0).getCapacity()){
				C.add(A.get(0));
				A.remove(0);
			}
			else{
				C.add(B.get(0));
				if(A.get(0) == B.get(0)){
					A.remove(0);
					B.remove(0);
				}
				else{
					B.remove(0);
				}
			}
		}
		if(B.size() > 0){
			C.addAll(B);			
		}
		if(A.size() > 0){
			C.addAll(A);			
		}
		return C;
	}
	/*
	 * Implementation of Kruskal's Algorithm
	 * Reference - Graphs: Theory and Algorithms, Thulasiraman, Swamy.
	 * S = { Edges which cross the cut (S, V-S), and do not form a cycle }
	 */
	public void Algorithm(ArrayList<Edge> S){
		if(S.size() < 1){
			return;
		}
		Edge e = S.get(0);
		S.remove(0);
		if(FindSet(e.getFrom()).getId() != FindSet(e.getTo()).getId()){
			Union(e.getFrom(), e.getTo());
			this.spanningEdges.add(e);
		}
		Algorithm(S);
		/*
		if(S.size() < 1){
			if(vertices == this.g.getTotalNumberVertices()){
				this.disjoint = true;
			}
			return;
		}
		Edge e = S.get(0);
		e.setSpanningTrue();
		e.getFrom().setMstTrue();
		e.getTo().setMstTrue();
		Algorithm(++vertices, this.g.getUnSpanningEdges());
		*/
	}
	/*
	 * Returns the MST found in the method Algorithm()
	 */
	public String getMST(){
		String mst = "";
		double minW = 0;
		for(Edge temp : this.spanningEdges){
			mst += temp.getEdgeString();
			minW += temp.getCapacity();
		}
		mst += " Minimum Weight = " + minW;
		/*
		if(this.disjoint){
			mst = "Disjoint Graph";
		}
		else{
			double minW = 0;
			for(Edge e : this.g.edg){
				if(e.isInSpanningTree()){
					mst += e.getEdgeString();
					minW += e.getCapacity();
				}
			}
			mst += " Minimum Weight = " + minW;
		}
		*/
		return mst;
	}
	private void createCuts(ArrayList<Vertex> vList){
		for(Vertex v : vList){
			v.setLeader(v);
		}
	}
	private Vertex FindSet(Vertex v){
		return v.getLeader();
	}
	private void Union(Vertex u, Vertex v){
		if(v.getRootedTreeHeight() < u.getRootedTreeHeight()){
			for(Vertex k : v.getLeader().getChildren()){
				k.setLeader(u.getLeader());
				u.getLeader().addChild(k);
			}
			u.getLeader().addChild(v.getLeader());
			v.getLeader().setLeader(u.getLeader());
			this.root = u.getLeader();
		}
		else{
			for(Vertex k : u.getLeader().getChildren()){
				k.setLeader(v.getLeader());
				v.getLeader().addChild(k);
			}
			v.getLeader().addChild(u.getLeader());
			u.getLeader().setLeader(v.getLeader());
			this.root = v.getLeader();
		}
	}
}
