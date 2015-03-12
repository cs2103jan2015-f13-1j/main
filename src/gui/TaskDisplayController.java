package gui;


import util.Task;
import util.operator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class TaskDisplayController {
	@FXML
	private ListView<Task> taskListDisplay;
	@FXML
	private TextField inputBox;

	//private Vector<Task> VectorTaskList;
	
	

	public void setTaskList(ListView<Task> TaskList) {
		taskListDisplay= TaskList;
	}

	@FXML
	private void initialize() {		
		
	}
	@FXML
	private void handleInput(){
		inputBox.setOnKeyPressed(new EventHandler<KeyEvent>()
			    {
			        @Override
			        public void handle(KeyEvent ke)
			        {
			            if (ke.getCode().equals(KeyCode.ENTER))
			            {
			                operator.showToUser("haha"+inputBox.getText());
			            }
			        }
			    });	
	}
	
	
}