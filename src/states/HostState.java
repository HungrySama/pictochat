package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.ClientThread;
import window.PictoWindow;
import window.Style;

public class HostState extends State {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties
	
	public static final int PORT = 4444;
	
	private ServerSocket serverSocket;
    private List<ClientThread> clients; 
    private boolean ready_for_clients = false;

	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Construction
	
	HostState(PictoWindow parent) {
		this.parent = parent;
		this.clients = new ArrayList<ClientThread>();
        this.serverSocket = null;
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
            ready_for_clients = true;
        } catch (IOException e){
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Host Functions
	
	public List<ClientThread> getClients(){
        return clients;
    }

	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// State Functions
	
	@Override
	public void update() {
		if (ready_for_clients) {
			try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException e){
                System.out.println("Accept failed on : " + PORT);
            }
		}
	}

	@Override
	public void render(Graphics2D g) {
		int drawX = 100;
		int drawY = 150;
		g.setFont(Style.ACCENT_FONT);
		g.setColor(Style.GREY_COLOR_1);
		g.drawString("Hosting...", drawX, drawY);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
