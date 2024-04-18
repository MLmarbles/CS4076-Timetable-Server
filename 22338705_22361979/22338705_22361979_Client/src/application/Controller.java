package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Controller {

    @FXML
    private TextArea serverText;

    @FXML
    private TextField mainTextField1;

    @FXML
    private Button stop1;

    @FXML
    private TextField enterModuleCode;

    @FXML
    private Button addModule;

    @FXML
    private Button removeModule;

    @FXML
    private TextField enterModuleCode1;

    @FXML
    private Button addClass;

    @FXML
    private Button removeClass;

    @FXML
    private Button displaySchedule;

    @FXML
    private Button enterTextAdd;

    @FXML
    private Button enterTextRemove;

    @FXML
    private Button stopServer;

    @FXML
    private Button earlyLectures;

    public void initialize() {
        addModule.setOnAction(event -> {
            String moduleCode = enterModuleCode.getText();

            AddModuleTask task = new AddModuleTask(moduleCode);
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        
        removeModule.setOnAction(event -> {
            String moduleCode = enterModuleCode.getText();

            RemoveModuleTask task = new RemoveModuleTask(moduleCode);
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        
        addClass.setOnAction(event -> {
			serverText.appendText("To add enter class details for " + enterModuleCode1.getText() + "\n"
					+ "Example (Monday, 9:00, 10:00, S206)" + "\n");

        });
        
        enterTextAdd.setOnAction(event -> {
            String moduleCode = enterModuleCode1.getText();
            String classData = mainTextField1.getText();

            AddClassTask task = new AddClassTask(moduleCode, classData);
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        
        removeClass.setOnAction(event -> {
			serverText.appendText("To remove enter class details for " + enterModuleCode1.getText() + "\n"
					+ "Example (Monday, 9:00, 10:00, S206)" + "\n");

        });
        
        enterTextRemove.setOnAction(event -> {
            String moduleCode = enterModuleCode1.getText();
            String classData = mainTextField1.getText();

            RemoveClassTask task = new RemoveClassTask(moduleCode, classData);
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        
        displaySchedule.setOnAction(event -> {
            String moduleCode = enterModuleCode1.getText();

            DisplayScheduleTask task = new DisplayScheduleTask(moduleCode);
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        
        stopServer.setOnAction(event -> {
        	
        	StopServerTask task = new StopServerTask();
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        
        earlyLectures.setOnAction(event -> {
            String moduleCode = enterModuleCode1.getText();

            EarlyLecturesTask task = new EarlyLecturesTask(moduleCode);
            task.setOnSucceeded(event1 -> {
                String response = task.getValue();
                serverText.appendText(response + "\n"); 
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
    }
    
	@FXML
	private AnchorPane p1;
	@FXML
	private AnchorPane p2;
	@FXML
	private AnchorPane p3;
	@FXML
	private AnchorPane p4;
	@FXML
	private AnchorPane p5;
	@FXML
	private MenuBar p6;
	@FXML
	private ColorPicker colorPicker;
	
	public void changeColor(ActionEvent e) {
		Color color = colorPicker.getValue();
		p1.setBackground(new Background(new BackgroundFill(color, null, null)));
		p2.setBackground(new Background(new BackgroundFill(color, null, null)));
		p3.setBackground(new Background(new BackgroundFill(color, null, null)));
		p4.setBackground(new Background(new BackgroundFill(color, null, null)));
		p5.setBackground(new Background(new BackgroundFill(color, null, null)));
		p6.setBackground(new Background(new BackgroundFill(color, null, null)));
	}
	
	@FXML 
	private CheckBox checkBox;
	
	public void change(ActionEvent event) {
		if (checkBox.isSelected()) {
			Color color = Color.web("#808080");
			p1.setBackground(new Background(new BackgroundFill(color, null, null)));
			p2.setBackground(new Background(new BackgroundFill(color, null, null)));
			p3.setBackground(new Background(new BackgroundFill(color, null, null)));
			p4.setBackground(new Background(new BackgroundFill(color, null, null)));
			p5.setBackground(new Background(new BackgroundFill(color, null, null)));
			p6.setBackground(new Background(new BackgroundFill(color, null, null)));			
		} else {
			Color color = Color.web("#FFFFFF");
			p1.setBackground(new Background(new BackgroundFill(color, null, null)));
			p2.setBackground(new Background(new BackgroundFill(color, null, null)));
			p3.setBackground(new Background(new BackgroundFill(color, null, null)));
			p4.setBackground(new Background(new BackgroundFill(color, null, null)));
			p5.setBackground(new Background(new BackgroundFill(color, null, null)));
			p6.setBackground(new Background(new BackgroundFill(color, null, null)));			
		}
	}
}
