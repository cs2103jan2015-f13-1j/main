package gui;

import java.util.Vector;

import fileIO.FileStream;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.Logic;

public class TaskDisplayController {

	@FXML
	private TextArea inputBox;

	@FXML
	private ListView<Task> listView = new ListView<Task>();

	@FXML
	private Label label = new Label();

	private ObservableList<Task> list;

	private Logic l = new Logic();

	private ListViewGUI gui;

	private Vector<Task> VectorTaskList;

	public TaskDisplayController() {

	}

	public void setTaskList(Vector<Task> TaskList) {

		list = FXCollections.observableList(TaskList);

		listView.setItems(list);
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			
			class TaskCell extends ListCell<Task> {
		        HBox hbox = new HBox();
		        Text desc = new Text("(empty)");
		        Text details = new Text("(empty)");
		        Pane pane = new Pane();
		        CheckBox done = new CheckBox();
		        Button delete = new Button("Del");

		        public TaskCell() {
		            super();
		            hbox.getChildren().addAll(done, desc, details, pane, delete);
		            HBox.setHgrow(pane, Priority.ALWAYS);
		            delete.setOnAction(new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(ActionEvent event) {
		                    System.out.println("DELETE THIS!");
		                }
		            });
		        }
		        
		        public void updateDesc(Text descValue) {
		            desc = descValue;
		        }
		        
		        public void updateDetails(Text detailsValue) {
		            details =  detailsValue;
		        }
		    }
			
				@Override
				public ListCell<Task> call(ListView<Task> ListViewTask) {
					
					 TaskCell cell = new TaskCell() {
						@Override
						protected void updateItem(Task t, boolean b) {
							super.updateItem(t, b);
							if (t != null) {
								updateDesc(formatTask1(t));
								updateDetails(formatTask2(t));
								setGraphic(hbox);
							} else {
								setGraphic(new Text(""));
							}
						}
					};
					return cell;
				}
			});

	}

	private Text formatTask1(Task t) {
		Text text;
		text = new Text(t.getIndex() + ": " + t.getTaskDesc());
		return text;
		}
	
	private Text formatTask2(Task t) {
		Text text;
		if (t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {
			text = new Text(
					"\nFrom: "
					+ TimeExtractor.formatDateTime(t.getStartTime()) + " To: "
					+ TimeExtractor.formatDateTime(t.getEndTime()));

		} else if (t.getTaskType().equals(TASK_TYPE.DEADLINE)) {
			text = new Text(
					"\nBy: "
					+ TimeExtractor.formatDateTime(t.getEndTime()));
		} else {
			text = new Text("\n");
		}

		return text;
	}

	@FXML
	private void initialize() {
		FileStream.initializeDir();
		VectorTaskList = FileStream.loadTasksFromXML();
		setTaskList(VectorTaskList);

		inputBox.setPromptText("Enter Command:");
		inputBox.setWrapText(true);
		updateLabel("");
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
						processUserInput(text);

						// clear text
						inputBox.setText("");
					} else {
						System.exit(0);
					}

				}
				
				if (key.getCode().equals(KeyCode.F5)) {
					FileStream.changeDir();
				}
				
			}
		});
	}

	public void setGUI(ListViewGUI listViewGUI) {
		this.gui = listViewGUI;

	}

	public void updateLabel(String s) {
		label.setText(s);
	}

	public void processUserInput(String str) {
		VectorTaskList = l.run(str);
		
		//Comments on replacing listView.setItems with the following 2 lines:
		//This theoretically works the same way, but the 2 lines will fix the way listView updates accordingly.
		//listView.setItems(list);		
		listView.getItems().clear();
		listView.getItems().addAll(VectorTaskList);
		
		String resultToUser = l.getText();
		updateLabel(resultToUser);
	}

}