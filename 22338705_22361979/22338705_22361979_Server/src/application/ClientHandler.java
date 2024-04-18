package application;

import java.net.Socket;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	
	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
	}

	@Override
	public void run() 
    { 
	    boolean shouldRun = true;
	    while (shouldRun) {
	        shouldRun = Server.handleClientRequest();
	    }
    }
}
