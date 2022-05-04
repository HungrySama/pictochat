package networking;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import states.ClientState;

public class ServerThread implements Runnable {
	
    private Socket socket;
    private String username;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;
    private ClientState parent_state;

    public ServerThread(ClientState parent_state, Socket socket, String userName) {
    	this.parent_state = parent_state;
        this.socket = socket;
        this.username = userName;
        messagesToSend = new LinkedList<String>();
    }

    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    public void run(){
        System.out.println("Welcome :" + username);
        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                    	String msg = serverIn.nextLine();
                    	System.out.println(msg);
                    	// output messages as drawing to window
                        if (msg.length() > 0) {
                        	if (msg.contains("> /")) {
                        		int prefix_index = msg.indexOf('/');
                        		parent_state.sent_drawings.add(msg.substring(prefix_index + 1));
                        	} else {
                        		parent_state.sent_messages.add(msg);
                        	}
                        }
                    }
                }
                if(hasMessages){
                	// output messages to console
                    String raw_message = "";
                    synchronized(messagesToSend){
                    	raw_message = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    String final_message = username + " > " + raw_message;
                    serverOut.println(final_message);
                    serverOut.flush();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        
    }
}