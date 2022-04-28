package window;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import states.State;
import states.WelcomeState;

@SuppressWarnings("serial")
public class PictoWindow extends JFrame implements KeyListener {

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	
	public boolean should_close = false;
	
	private State current_state;
	
	public PictoWindow() {
		// Listener setup
		addKeyListener(this);

		// Window setup
		setTitle("Pictochat");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(0, 0);
		setUndecorated(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// assign first state
		this.current_state = new WelcomeState(this);
		
		// Begin gameloop
		run();
	}
	
	public void run() {
		while (!should_close) {
			render();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Engine -- the background workings

	private void render() {
		// Creates 2-buffer buffer strategy
		BufferStrategy bs = getBufferStrategy();
		if (getBufferStrategy() == null) {
			createBufferStrategy(2);
			return;
		}

		// Draws to hidden buffer, clears previous image
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setBackground(Color.getHSBColor(0.0f, 0.0f, 0.0f));
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.clearRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		
		// Application related rendering
		current_state.render(g);
		
		// Disposes graphics object and shows hidden buffer
		g.dispose();
		bs.show();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Main

	public static void main(String[] args) {
		new PictoWindow();
		System.exit(0);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Controls
	
	@Override
	public void keyPressed(KeyEvent e) {
		current_state.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		current_state.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		current_state.keyTyped(e);
	}

}

