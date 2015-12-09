package sojourner;
import java.util.ArrayList;

/*
 * Implementation as in CLRS book
 */
public class Vertex {
	Integer id;
	Integer color; // 0 = white, 1 = gray, 2 = black
	boolean visited;
	double d;
	Vertex p;
	Vertex leader; // Used for UNION-FIND operations
	boolean shortestDist;
	boolean inMST;
	ArrayList<Vertex> children;
	int rootedTreeHeight;
	public Vertex(){}
	public Vertex(int n){
		this.id = n;
		this.color = 0;
		this.d = 999999;
		this.p = null;
		this.visited = false;
		this.shortestDist = false;
		this.inMST = false;
		children = new ArrayList<Vertex>();
		rootedTreeHeight = 0;
	}
	public void initialize(){
		this.color = 0;
		this.d = 999999;
		this.p = null;
		this.visited = false;
		this.shortestDist = false;
	}
	public boolean wasVisited(){
		return this.visited;
	}
	public void setVisitedTrue(){
		this.visited = true;
	}
	public Integer getId(){
		return this.id;
	}
	public Double getDistance(){
		return this.d;
	}
	public void setDistance(double d){
		this.d = d;
	}
	public boolean shortestDistFound(){
		return this.shortestDist;
	}
	public void setShortestDistTrue(){
		this.shortestDist = true;
	}
	public Integer getColor(){
		return this.color;
	}
	public Vertex getPredecessor(){
		return this.p;
	}
	public void resetDistance(){
		this.d = 0;
	}
	public void setColor(Integer c){
		this.color = c;
	}
	public void setPredecessor(Vertex p){
		this.p = p;
	}
	public boolean isInMst(){
		return this.inMST;
	}
	public void setMstTrue(){
		this.inMST = true;
	}
	public void setLeader(Vertex u){
		this.leader = u;
	}
	public Vertex getLeader(){
		return this.leader;
	}
	public ArrayList<Vertex> getChildren(){
		return this.children;
	}
	public void addChild(Vertex v){
		this.children.add(v);
	}
	public void setRootedTreeHeight(int n){
		this.rootedTreeHeight = n;
	}
	public int getRootedTreeHeight(){
		return this.rootedTreeHeight;
	}
}
