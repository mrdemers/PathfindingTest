package com;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Component extends BasicGame {
	public Tile[][] tiles;
	int startX = 1;
	int startY = 1;
	int endX = 20;
	int endY = 20;
	public static final int boardWidth = 30;
	public Component(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				tiles[y][x].render(gc, g);
			}
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		tiles = new Tile[boardWidth][boardWidth];
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				tiles[y][x] = new Tile(x, y, true); 
			}
		}
		PathFinder.findPath(tiles, startX, startY, endX, endY);
	}
	boolean hasStart, hasEnd;
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = gc.getHeight() - Mouse.getY();
			int xx = x/Tile.WIDTH;
			int yy = y/Tile.WIDTH;
			if (xx == startX && yy == startY) {
				hasStart = true;
			}
			if (xx == endX && yy == endY) {
				hasEnd = true;
			}
			if (hasStart) {
				System.out.println("HI");
				startX = xx;
				startY = yy;
				clearBoard();
				PathFinder.findPath(tiles, startX, startY, endX, endY);
			} else if (hasEnd) {
				endX = xx;
				endY = yy;
				clearBoard();
				PathFinder.findPath(tiles, startX, startY, endX, endY);
			}
			if (!hasStart && !hasEnd && tiles[yy][xx].setPassable(false)) {
				clearBoard();
				PathFinder.findPath(tiles, startX, startY, endX, endY);
			}
//			if (xx >= 0 && yy >= 0 && xx < tiles[0].length && yy < tiles.length) {
//				startX = xx;
//				startY = yy;
//				clearBoard();
//				PathFinder.findPath(tiles, startX, startY, endX, endY);
//			}
		}
		if (!Mouse.isButtonDown(0)) {
			hasStart = false;
			hasEnd = false;
		}
		if (Mouse.isButtonDown(1)) {
			int x = Mouse.getX();
			int y = gc.getHeight() - Mouse.getY();
			int xx = x/Tile.WIDTH;
			int yy = y/Tile.WIDTH;
			if (tiles[yy][xx].setPassable(true)) {
				clearBoard();
				PathFinder.findPath(tiles, startX, startY, endX, endY);
			}
		}
	}

	private void clearBoard() {
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				if (tiles[y][x].passable)
					tiles[y][x].setColor(Color.white);
				else
					tiles[y][x].setColor(Color.black);
				tiles[y][x].score = 0;
			}
		}
	}

	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(new Component("A Particle test"));
			agc.setDisplayMode(Tile.WIDTH*boardWidth, Tile.WIDTH*boardWidth, false);
			agc.setShowFPS(false);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
