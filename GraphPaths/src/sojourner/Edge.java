package sojourner;

public class Edge {
	Vertex u;
	Vertex v;
	double ce;
	double fe;
	boolean active = true;
	boolean spanningEdge; 
	public Edge(Vertex u, Vertex v, double weight){
		this.u = u;
		this.v = v;
		this.ce = weight;
		this.fe = 0;
		this.active = true;
		this.spanningEdge = false;
	}
	/*
	 * Used for Capacity Scaling
	 */
	public Boolean isActive(){
		return this.active;
	}
	
	public Vertex getFrom(){
		return u;
	}
	public Vertex getTo(){
		return v;
	}
	public double getCapacity(){
		return this.ce;
	}
	public double getFlow(){
		return this.fe;
	}
	public double getResidualCapacity(){
		return (this.ce - this.fe);
	}
	public String getEdgeString(){
		return "(" + this.u.getId().toString() + ", " + this.v.getId().toString() + "), "; 
	}
	public void reduceFlow(double f){
		this.fe = this.ce - f;
	}
	public void increaseFlow(double f){
		this.fe += f;
	}
	public void deactivate(){
		this.active = false;
	}
	public void activate(){
		this.active = true;
	}
	public void setSpanningTrue(){
		this.spanningEdge = true;
	}
	public boolean isInSpanningTree(){
		return this.spanningEdge;
	}
}
