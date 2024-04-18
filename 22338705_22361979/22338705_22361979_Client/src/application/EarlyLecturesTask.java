package application;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class EarlyLecturesTask extends Task<String> {

    private final String moduleCode;

    public EarlyLecturesTask(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Override
    protected String call() throws Exception {
        try {
            return sendModuleData("EARLY_LECTURE", moduleCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String sendModuleData(String operation, String moduleCode) throws Exception {
        try (Socket link = new Socket(InetAddress.getLocalHost(), Main.PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
             PrintWriter out = new PrintWriter(link.getOutputStream(), true)) {

            out.println(operation + " " + moduleCode);
            String response = in.readLine();

            System.out.println("\n* Closing connection... *");
            return response;
        }
    }
}

