package com;

public class PathNode {
	public int x, y;
	public double score;
	public int distanceFromStart;
	public double distanceFromDestination;
	public boolean passable;
	public PathNode parent;
	
	public PathNode(int x, int y, boolean passable, PathNode parent) {
		this.x = x;
		this.y = y;
		this.passable = passable;
		this.parent = parent;
		if (parent == null) {
			distanceFromStart = 0;
		} else {
			distanceFromStart = parent.distanceFromStart + 1;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		
		if (!(o instanceof PathNode)) {
			return false;
		}
		
		PathNode p = (PathNode)o;
		return p.x == this.x && p.y == this.y && p.passable == this.passable;
	}

	public void calculateScore(int ex, int ey) {
		int dx = ex - x;
		int dy = ey - y;
		distanceFromDestination = Math.round(Math.sqrt(dx * dx + dy * dy));
		score = distanceFromStart + distanceFromDestination;
	}
}
