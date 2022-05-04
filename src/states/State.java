package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import window.PictoWindow;

public abstract class State {
	
	public PictoWindow parent;
	
	public abstract void update();

	public abstract void render(Graphics2D g);
	
	public abstract void keyPressed(KeyEvent e);

	public abstract void keyTyped(KeyEvent e);
	
	public abstract void mousePressed(MouseEvent e);
	
	public abstract void mouseReleased(MouseEvent e);
	
}