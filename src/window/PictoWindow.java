package window;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import states.State;
import states.WelcomeState;

@SuppressWarnings("serial")
public class PictoWindow extends JFrame implements KeyListener, MouseListener {

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int CANVAS_SQUARE_RES = 50;
	public static final int CANVAS_WIDTH = WINDOW_WIDTH / CANVAS_SQUARE_RES; // 50
	public static final int CANVAS_HEIGHT = WINDOW_HEIGHT / CANVAS_SQUARE_RES; // 37
	public static final int FEED_DRAWING_RES = 20;
	
	
	public boolean should_close = false;
	public State current_state;
	
	public PictoWindow() {
		// Listener setup
		addKeyListener(this);
		addMouseListener(this);

		// Window setup
		setTitle("Pictochat");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(0, 0);
		setUndecorated(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// Assign first state
		this.current_state = new WelcomeState(this);
		
		// Begin gameloop
		run();
	}
	
	public void run() {
		while (!should_close) {
			current_state.update();
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
		g.setBackground(Style.BACKGROUND_COLOR);
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
	public void keyTyped(KeyEvent e) {
		current_state.keyTyped(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		current_state.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		current_state.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}

