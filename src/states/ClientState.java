package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import networking.ServerThread;
import window.PictoWindow;
import window.Style;

public class ClientState extends State {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties
	
	private static enum ChatState { PROMPT_NAME, CHATTING }
	private static final String host = "localhost";
	
	private ServerThread server_thread;
	private Thread server_access_thread;
	private ChatState current_chat_state = ChatState.PROMPT_NAME;
	private StringBuilder username_sb = new StringBuilder();
	private StringBuilder message_sb = new StringBuilder();
	private boolean message_completed = false;
	public ArrayList<String> sent_messages = new ArrayList<String>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Construction
	
	ClientState(PictoWindow parent) {
		this.parent = parent;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Client Functions
		
	public void establishConnection() {
        try{
            Socket socket = new Socket(host, HostState.PORT);
            Thread.sleep(1000);
            server_thread = new ServerThread(this, socket, username_sb.toString());
            server_access_thread = new Thread(server_thread);
            server_access_thread.start();
        } catch(IOException e) {
            System.err.println("Fatal Connection error!");
            e.printStackTrace();
        } catch(InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// State Functions

	@Override
	public void update() {
		switch (current_chat_state) {
		case PROMPT_NAME:
			// complete message
			if (message_completed) {
				if (username_sb.length() > 1) {
					establishConnection();
					current_chat_state = ChatState.CHATTING;
				}
				// reset flag
				message_completed = false;
			}
			break;
		case CHATTING:
			// complete message
			if (message_completed) {
				if (server_access_thread.isAlive()) {
					// send message
					server_thread.addNextMessage(message_sb.toString());
					// empty string builder
					message_sb.setLength(0);
					// reset flag
					message_completed = false;
				}
			}
			break;
		}
	}

	@Override
	public void render(Graphics2D g) {
		switch (current_chat_state) {
		case PROMPT_NAME:
			int drawX = 100;
			int drawY = 150;
			g.setFont(Style.ACCENT_FONT);
			g.setColor(Style.ACCENT_COLOR);
			g.drawString("Enter username: ", drawX, drawY);
			g.drawString("> " + username_sb.toString(), drawX, drawY + Style.ACCENT_SIZE);
			break;
		case CHATTING:
			// render the feed
			int box_height = (int) (Style.ACCENT_SIZE * 1.75);
			for (int i = 0; i < sent_messages.size(); i++) {
				int variable_offset = (sent_messages.size() - 1 - i) * box_height;
				g.setColor(Style.GREY_COLOR_1);
				g.setFont(Style.ACCENT_FONT);
				g.drawString(sent_messages.get(i), 25, (int) (PictoWindow.WINDOW_HEIGHT - box_height * 1.5) - variable_offset);
			}
			// render the typing box
			g.setColor(Style.GREY_COLOR_2);
			g.fillRect(0, PictoWindow.WINDOW_HEIGHT - box_height, PictoWindow.WINDOW_WIDTH, box_height);
			g.setFont(Style.ACCENT_FONT);
			g.setColor(Style.BACKGROUND_COLOR);
			g.drawString("> " + message_sb.toString(), 25, PictoWindow.WINDOW_HEIGHT - (int) (Style.ACCENT_SIZE * 0.5));
			break;
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// triggers a flag that sends whatever was being typed
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			message_completed = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (current_chat_state) {
		case PROMPT_NAME:
			if (e.getKeyChar() != '\n') {
				username_sb.append(e.getKeyChar());
			}
			break;
		case CHATTING:
			if (e.getKeyChar() != '\n') {
				message_sb.append(e.getKeyChar());
			}
			break;
		}
	}
	
}
