package states;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
	
	// network related
	private static final String host = "localhost";
	private ServerThread server_thread;
	private Thread server_access_thread;
	
	// chat related
	private static enum ChatState { PROMPT_NAME, CHATTING }
	private ChatState current_chat_state = ChatState.PROMPT_NAME;
	private StringBuilder username_sb = new StringBuilder();
	private StringBuilder message_sb = new StringBuilder();
	private boolean message_completed = false;
	public ArrayList<String> sent_messages = new ArrayList<String>();
	
	// drawing related
	private boolean mouse_down = false;
	private Point mouse_position = new Point();
	private boolean canvas[][] = new boolean[PictoWindow.CANVAS_WIDTH][PictoWindow.CANVAS_HEIGHT];
	public ArrayList<String> sent_drawings = new ArrayList<String>();
	
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
	
	public String generateDrawingString() {
		StringBuilder result = new StringBuilder();
		result.append("/");
		for (int j = 0; j < PictoWindow.CANVAS_HEIGHT; j++) {
			for (int i = 0; i < PictoWindow.CANVAS_WIDTH; i++) {
				char c = (canvas[i][j]) ? '1' : '0';
				result.append(c);
			}
		}
		return result.toString();
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
					// empty message string builder
					message_sb.setLength(0);
					// send drawing
					server_thread.addNextMessage(generateDrawingString());
					// empty drawing buffer
					canvas = new boolean[PictoWindow.CANVAS_WIDTH][PictoWindow.CANVAS_HEIGHT];
					// reset flag
					message_completed = false;
				}
			}
			// draw picture when mouse down
			if (mouse_down) {
				// update mouse position
				int mouseX = MouseInfo.getPointerInfo().getLocation().x - parent.getX();
				int mouseY = MouseInfo.getPointerInfo().getLocation().y - parent.getY();
				mouse_position = new Point(mouseX, mouseY);
				// update canvas
				int gridX = mouse_position.x / PictoWindow.CANVAS_SQUARE_RES;
				int gridY = mouse_position.y / PictoWindow.CANVAS_SQUARE_RES;
				boolean within_index_range = (0 < gridX && gridX < PictoWindow.CANVAS_WIDTH) && (0 < gridY && gridY < PictoWindow.CANVAS_HEIGHT);
				if (within_index_range) canvas[gridX][gridY] = true;
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
			// render the message feed
			int box_height = (int) (Style.ACCENT_SIZE * 1.75);
			for (int i = 0; i < sent_messages.size(); i++) {
				int variable_offset = (sent_messages.size() - 1 - i) * box_height;
				g.setColor(Style.GREY_COLOR_1);
				g.setFont(Style.ACCENT_FONT);
				g.drawString(sent_messages.get(i), 25, (int) (PictoWindow.WINDOW_HEIGHT - box_height * 1.5) - variable_offset);
			}
			// render the drawing feed
			int drawing_height = PictoWindow.FEED_DRAWING_RES * PictoWindow.CANVAS_HEIGHT;
			int drawing_width = PictoWindow.FEED_DRAWING_RES * PictoWindow.CANVAS_WIDTH;
			for (int d = 0; d < sent_drawings.size(); d++) {
				String drawing = sent_drawings.get(d);
				int drawing_origin_x = PictoWindow.WINDOW_WIDTH - drawing_width;
				int drawing_origin_y = drawing_height - (sent_drawings.size() - 1 - d) * drawing_height;
				for (int n = 0; n < PictoWindow.CANVAS_WIDTH * PictoWindow.CANVAS_HEIGHT; n++) {
					char c = drawing.charAt(n);
					int x = n % PictoWindow.CANVAS_WIDTH;
					int y = n / PictoWindow.CANVAS_WIDTH;
					if (c == '1') {
						int draw_x = drawing_origin_x + x * PictoWindow.FEED_DRAWING_RES;
						int draw_y = drawing_origin_y + y * PictoWindow.FEED_DRAWING_RES;
						g.setColor(Style.GREY_COLOR_2);
						g.fillRect(draw_x, draw_y, PictoWindow.FEED_DRAWING_RES, PictoWindow.FEED_DRAWING_RES);
					}
				}
			}
			// render the typing box
			g.setColor(Style.GREY_COLOR_2);
			g.fillRect(0, PictoWindow.WINDOW_HEIGHT - box_height, PictoWindow.WINDOW_WIDTH, box_height);
			g.setFont(Style.ACCENT_FONT);
			g.setColor(Style.BACKGROUND_COLOR);
			g.drawString("> " + message_sb.toString(), 25, PictoWindow.WINDOW_HEIGHT - (int) (Style.ACCENT_SIZE * 0.5));
			// render the drawing grid
			int grid_res = PictoWindow.CANVAS_SQUARE_RES;
			for (int i = 0; i < PictoWindow.CANVAS_WIDTH; i++) {
				for (int j = 0; j < PictoWindow.CANVAS_HEIGHT; j++) {
					if (canvas[i][j]) {
						g.setColor(Style.ACCENT_COLOR);
						g.fillRect(i * grid_res, j * grid_res, grid_res, grid_res);
					}
				}
			}
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

	@Override
	public void mousePressed(MouseEvent e) {
		this.mouse_down = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mouse_down = false;
	}
	
}
