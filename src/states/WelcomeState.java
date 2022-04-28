package states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import window.PictoWindow;

public class WelcomeState extends State {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties
	
	private PictoWindow parent;
	private String[] welcomeOptions = { "HOST", "JOIN", "QUIT" };
	private int selectedOption = 0;
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Construction
	public WelcomeState(PictoWindow parent) {
		this.parent = parent;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Functions

	@Override
	public void render(Graphics2D g) {
		// display setup
		int offX = 100;
		int offY = 150;
		Dimension boxSize = new Dimension(100, 40);
		int boxSpacing = 30;
		// draw
		for (int i = 0; i < welcomeOptions.length; i++) {
			int drawX = offX + (boxSize.width + boxSpacing) * i;
			g.setColor((selectedOption == i) ? Color.blue : Color.white);
			g.drawString(welcomeOptions[i], drawX, offY);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			selectedOption += 1;
			if (selectedOption >= welcomeOptions.length) selectedOption = 0;
			break;
		case KeyEvent.VK_LEFT:
			selectedOption -= 1;
			if (selectedOption < 0) selectedOption = welcomeOptions.length - 1;
			break;
		case KeyEvent.VK_ENTER:
			if (welcomeOptions[selectedOption].compareTo("QUIT") == 0) {
				parent.should_close = true;
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
