package states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import window.PictoWindow;
import window.Style;

public class WelcomeState extends State {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties
	
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
		int offX = PictoWindow.WINDOW_WIDTH / 2;
		int offY = 3 * PictoWindow.WINDOW_HEIGHT / 5;
		// draw title
		g.setFont(Style.TITLE_FONT);
		g.setColor(Style.ACCENT_COLOR);
		String title = "> PictoChat";
		int drawX = offX - g.getFontMetrics().stringWidth(title) / 2;
		int drawY = 2 * PictoWindow.WINDOW_HEIGHT / 5;
		g.drawString(title, drawX, drawY);
		// draw menu options
		for (int i = 0; i < welcomeOptions.length; i++) {
			g.setFont(Style.ACCENT_FONT);
			g.setColor((selectedOption == i) ? Style.ACCENT_COLOR : Style.GREY_COLOR_1 );
			String text = welcomeOptions[i];
			drawX = offX - g.getFontMetrics().stringWidth(text) / 2;
			drawY = offY + i * Style.ACCENT_SIZE;
			g.drawString(text, drawX, drawY);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			selectedOption += 1;
			if (selectedOption >= welcomeOptions.length) selectedOption = 0;
			break;
		case KeyEvent.VK_UP:
			selectedOption -= 1;
			if (selectedOption < 0) selectedOption = welcomeOptions.length - 1;
			break;
		case KeyEvent.VK_ENTER:
			if (welcomeOptions[selectedOption].compareTo("HOST") == 0) {
				parent.current_state = new HostState(this.parent);
			}
			if (welcomeOptions[selectedOption].compareTo("JOIN") == 0) {
				parent.current_state = new ClientState(this.parent);
			}
			if (welcomeOptions[selectedOption].compareTo("QUIT") == 0) {
				parent.should_close = true;
			}
			break;
		}
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
