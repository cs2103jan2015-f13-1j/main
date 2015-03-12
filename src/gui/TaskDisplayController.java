package gui;

import java.time.format.DateTimeFormatter;
import java.util.Vector;

import util.Task;
import util.operator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;

public class TaskDisplayController {
	
	
	@FXML
	private TextField inputBox;
	@FXML
	private ListView<Task> listView = new ListView<Task>();
	
	private ObservableList<Task> list;

	// private Vector<Task> VectorTaskList;

	public TaskDisplayController() {

	}

	public void setTaskList(Vector<Task> TaskList) {

		list = FXCollections.observableArrayList(TaskList);
		listView.setItems(list);
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> ListViewTask) {
				ListCell<Task> cell = new ListCell<Task>() {

					@Override
					protected void updateItem(Task t, boolean b) {
						super.updateItem(t, b);
						if (t != null) {
							setText(t.getTaskDesc() + "\nFrom: "
									+ t.getStartTime() + " To: "
									+ t.getEndTime());
						}
					}
				};
				return cell;
			}
		});
	}

	@FXML
	private void initialize() {
		
		
	}

	@FXML
	private void handleInput() {
		inputBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					operator.showToUser("haha" + inputBox.getText());
				}
			}
		});
	}

}