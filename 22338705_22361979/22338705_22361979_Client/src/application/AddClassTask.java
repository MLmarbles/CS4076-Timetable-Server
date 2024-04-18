package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.concurrent.Task;

public class AddClassTask extends Task<String>{
	
    private final String moduleCode;
    private final String classData;

    public AddClassTask(String moduleCode, String classData) {
        this.moduleCode = moduleCode;
        this.classData = classData;
    }

    @Override
    protected String call() throws Exception {
        try {
            return sendClassData("ADD_CLASS", moduleCode, classData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String sendClassData(String operation, String moduleCode, String classData) throws IncorrectActionException {
        try {
            InetAddress host = InetAddress.getLocalHost();
            try (Socket link = new Socket(host, Main.PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
                 PrintWriter out = new PrintWriter(link.getOutputStream(), true)) {

                out.println(operation + " " + moduleCode + " " + classData);
                String response = in.readLine();
                return response;
            }
        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
            return null; 
        } catch (IOException e) {
            return null;
        }
    }


}
