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
import javafx.scene.text.Text;
import javafx.util.Callback;

public class TaskDisplayController {
	
	
	@FXML
	private TextArea inputBox;
	
	@FXML
	private ListView<Task> listView = new ListView<Task>();
	
	@FXML
	private Label label = new Label();
	
	private ObservableList<Task> list;
	
	private ListViewGUI gui;

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
						private Text text;

					@Override
					protected void updateItem(Task t, boolean b) {
						super.updateItem(t, b);
						if (t != null) {
							
							if (t.getStartTime() != null) {
								text = new Text(super.getIndex() + 1 + "."
										+ t.getTaskDesc() + "\nFrom: "
										+ t.getStartTime() + " To: "
										+ t.getEndTime());
								text.setWrappingWidth(listView.getPrefWidth());
								setGraphic(text);
							} else if (t.getEndTime() != null) {
								setText(super.getIndex() + 1 + "."
										+ t.getTaskDesc() + "\nBy: "
										+ t.getEndTime());
							} else {
								//setText(super.getIndex() + 1 + "."
								//		+ t.getTaskDesc() + "\n");
								text = new Text(super.getIndex() + 1 + "." + t.getTaskDesc() + "\n");
								text.setWrappingWidth(listView.getPrefWidth());
								setGraphic(text);
							}

						} else {
							setGraphic(new Text(""));
						}
					}
				};
				return cell;
			}
		});
	}

	@FXML
	private void initialize() {
		inputBox.setPromptText("Enter Command:");
		inputBox.setWrapText(true);
		label.setText("");
	}

	@FXML
	private void handleInput() {
		
		inputBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)) {
					// Prevents the enter key from doing a newline
					key.consume();

					String text = inputBox.getText();
					
					// Terminates program if exit, else refresh list
					if (!text.toLowerCase().equals("exit")) {
						gui.processUserInput(text);

						// clear text
						inputBox.setText("");
					}
					else{
						System.exit(0);
					}
					
				}
			}
		});
	}

	public void setGUI(ListViewGUI listViewGUI) {
		this.gui = listViewGUI;
		
	}

	public void updateTaskList(Vector<Task> vectorTasks) {
		list = FXCollections
				.observableArrayList(vectorTasks);
		listView.setItems(list);
		inputBox.setPromptText("Enter Command:");
	}
	
	public void updateLabel(String s) {
		label.setText(s);
	}

}