package application;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class DisplayScheduleTask extends Task<String> {

    private final String moduleCode;

    public DisplayScheduleTask(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Override
    protected String call() throws Exception {
        try {
            return sendModuleDataToDisplay("DISPLAY_SCHEDULE", moduleCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String sendModuleDataToDisplay(String operation, String moduleCode) throws IncorrectActionException {
        try {
            InetAddress host = InetAddress.getLocalHost();
            String response;
            
            try (Socket link = new Socket(host, Main.PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
                 PrintWriter out = new PrintWriter(link.getOutputStream(), true)) {
                 
                out.println(operation + " " + moduleCode);
                response = in.readLine();
                
                if (response.startsWith("Error: ")) {
                    throw new IncorrectActionException(response);
                }
            }

            System.out.println("\n* Closing connection... *");
            return response;
        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }


}