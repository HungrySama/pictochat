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
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                	// output messages to console
                    String next_message = "";
                    synchronized(messagesToSend){
                    	next_message = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(username + " > " + next_message);
                    serverOut.flush();
                    // output messages to window
                    parent_state.sent_messages.add(next_message);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}