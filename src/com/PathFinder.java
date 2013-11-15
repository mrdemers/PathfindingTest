package com;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

public class PathFinder {
	private static List<PathNode> openList = new ArrayList<PathNode>();
	private static List<PathNode> closedList = new ArrayList<PathNode>();

	public static void findPath(Tile[][] tiles, int sx, int sy, int ex, int ey) {
		PathNode currentNode = new PathNode(sx, sy, tiles[sy][sx].passable, null);
		openList.add(currentNode);
		while (!openList.isEmpty()) {
			currentNode = lowestCost();
			List<PathNode> los = tryFindLOS(tiles, currentNode, ex, ey); 
			if (los != null) {
				openList.addAll(los);
			}
			if (currentNode.x == ex && currentNode.y == ey) {
				for (PathNode p : openList) {
					tiles[p.y][p.x].setColor(Color.red);
					tiles[p.y][p.x].score = p.score;
				}
				for (PathNode p : closedList) {
					tiles[p.y][p.x].setColor(Color.red);
					tiles[p.y][p.x].score = p.score;
				}
				PathNode node = currentNode;
				while (node.parent != null) {
					tiles[node.y][node.x].setColor(Color.green);
					tiles[node.y][node.x].score = node.score;
					node = node.parent;
				}
				tiles[sy][sx].setColor(Color.green);
				openList.clear();
				closedList.clear();
				return;
			} else {
				closedList.add(currentNode);
				openList.remove(currentNode);
				PathNode[] adjacentNodes = new PathNode[8];
				{
					int x = currentNode.x;
					int y = currentNode.y;
					if (x > 0 && y > 0) {
						boolean passable = tiles[y-1][x-1].passable && tiles[y-1][x].passable && tiles[y][x-1].passable;
						adjacentNodes[0] = new PathNode(x - 1, y - 1, passable, currentNode);
					}
					if (y > 0)
						adjacentNodes[1] = new PathNode(x, y - 1, tiles[y - 1][x].passable, currentNode);
					if (x < tiles[y].length - 1 && y > 0) {
						boolean passable = tiles[y-1][x+1].passable && tiles[y-1][x].passable && tiles[y][x+1].passable;
						adjacentNodes[2] = new PathNode(x + 1, y - 1, passable, currentNode);
					}
					if (x < tiles[y].length - 1)
						adjacentNodes[3] = new PathNode(x + 1, y, tiles[y][x + 1].passable, currentNode);
					if (x < tiles[y].length - 1 && y < tiles.length - 1) {
						boolean passable = tiles[y+1][x+1].passable && tiles[y+1][x].passable && tiles[y][x+1].passable;
						adjacentNodes[4] = new PathNode(x + 1, y + 1, passable, currentNode);
					}
					if (y < tiles.length - 1)
						adjacentNodes[5] = new PathNode(x, y + 1, tiles[y + 1][x].passable, currentNode);
					if (x > 0 && y < tiles.length - 1) {
						boolean passable = tiles[y+1][x-1].passable && tiles[y+1][x].passable && tiles[y][x-1].passable;
						adjacentNodes[6] = new PathNode(x - 1, y + 1, passable, currentNode);
					}
					if (x > 0)
						adjacentNodes[7] = new PathNode(x - 1, y, tiles[y][x - 1].passable, currentNode);
				}
				for (PathNode p : adjacentNodes) {
					if (p == null)
						continue;
					if (!openList.contains(p) && !closedList.contains(p) && p.passable) {
						p.calculateScore(ex, ey);
						openList.add(p);
					}
				}
			}
		}
		
		for (PathNode p : closedList) {
			tiles[p.y][p.x].setColor(Color.red);
		}
		openList.clear();
		closedList.clear();
	}

	private static List<PathNode> tryFindLOS(Tile[][] tiles, PathNode current, int ex, int ey) {
		int sx = current.x;
		int sy = current.y;
		double x = sx;
		double y = sy;
		int xx = (int)x, yy = (int)y;
		List<PathNode> myList = new ArrayList<PathNode>();
		PathNode curr = new PathNode((int)x, (int) y, tiles[sy][sx].passable, current);
		myList.add(curr);
		
		while (true) {
			int dx = ex - xx;
			int dy = ey - yy;
			double dir = Math.atan2(dy, dx);
			int oldX = (int)x;
			int oldY = (int)y;
			x+=Math.cos(dir);
			y+=Math.sin(dir);
			xx = (int)x;
			yy = (int)y;
			if (xx < 0 || yy < 0 || xx >= tiles[0].length || yy >= tiles.length) { 
				return null;
			}
			if (!tiles[yy][xx].passable) {
				return null;
			}
			
			if (oldX - xx < 0 && oldY - yy > 0) { // Moving to down left
				if (!(tiles[oldY-1][oldX].passable() && tiles[oldY][oldX+1].passable())) {
					return null;
				}
			}
			if (oldX - xx < 0 && oldY - yy < 0) { // Moving to down right
				if (!(tiles[oldY+1][oldX].passable() && tiles[oldY][oldX+1].passable()))
					return null;
			}
			if (oldX - xx > 0 && oldY - yy < 0) { // Moving to down left
				if (!(tiles[oldY+1][oldX].passable() && tiles[oldY][oldX-1].passable())) 
					return null;
			}
			if (oldX - xx > 0 && oldY - yy > 0) { // Moving to up left
				if (!(tiles[oldY-1][oldX].passable() && tiles[oldY][oldX-1].passable())) 
					return null;
			}
			
			curr = new PathNode(xx, yy, true, curr);
			myList.add(curr);
			if (xx == ex && yy == ey) {
				break;
			}
		}
		return myList;
	}

	public static PathNode lowestCost() {
		double lowest = Integer.MAX_VALUE;
		PathNode curr = null;
		for (PathNode p : openList) {
			if (p.score < lowest) {
				lowest = p.score;
				curr = p;
			}
		}
		return curr;
	}
}
