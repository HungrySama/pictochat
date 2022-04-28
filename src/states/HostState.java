package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HostState extends State {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties

	public static final int PORT = 25565;
	
	private ServerSocket serverSocket;
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Construction
	
	HostState() {
		try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Functions
	
	@Override
	public void update() {
		// accept clients
		try {
			Socket socket = serverSocket.accept();
		} catch (IOException e) {
	        System.out.println(e.getMessage());
	    }
		
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
