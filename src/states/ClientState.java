package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

public class ClientState extends State {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties
	private Socket socket;
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Construction
	ClientState() {
		this.socket = null;
		try {
			socket = new Socket("localhost", HostState.PORT);
		} catch (IOException e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Functions

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
