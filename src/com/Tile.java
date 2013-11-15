package com;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Tile {
	public int x, y;
	public boolean passable;
	public Color color;
	public static final int WIDTH = 20;
	public double score = 0;
	public Tile(int x, int y, boolean passable) {
		this.x = x;
		this.y = y;
		this.passable = passable;
		if (passable)
			color = Color.white;
		else
			color = Color.black;
	}

	public boolean setPassable(boolean passable) {
		boolean orig = this.passable;
		this.passable = passable;
		if (!passable)
			color = Color.black;
		else
			color = Color.white;
		return orig != passable;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void render(GameContainer gc, Graphics g) {
		g.setColor(color);
		g.fillRect(x * WIDTH, y * WIDTH, WIDTH, WIDTH);
		g.setColor(Color.black);
		g.drawRect(x * WIDTH, y * WIDTH, WIDTH, WIDTH);
		if (score != 0) {
			g.drawString("" + (int)score, x * WIDTH + 2, y * WIDTH + 2);
		}
	}
	
	public boolean passable() {
		return passable;
	}
}
