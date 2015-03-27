package gui;

import java.util.Vector;

import fileIO.FileStream;
import gui.TaskDispC.XCell;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
	
	static class TaskCell extends ListCell<Task> {
        HBox hbox = new HBox();
        CheckBox done = new CheckBox();
        Text desc = new Text("(empty)");
        Text details = new Text("(empty)");
        Pane pane = new Pane();
        Button delete = new Button("Del");

        public TaskCell() {
            super();
            hbox.getChildren().addAll(done, desc, details, pane, delete);
            HBox.setHgrow(pane, Priority.ALWAYS);
            delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("DELETE!");
                }
            });
        }
        
        @Override
        protected void updateItem(Task t, boolean b) {
			super.updateItem(t, b);
			if (t != null) {
	            desc = formatTask1(t);
	            desc.setText("TEST");
	            details = formatTask2(t);
                setGraphic(hbox);
			} else {
				setGraphic(null);
			}
		}
    }

	public void setTaskList(Vector<Task> TaskList) {

		list = FXCollections.observableList(TaskList);
		listView.setItems(list);
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			
			@Override
            public ListCell<Task> call(ListView<Task> param) {
                return new TaskCell();
            }
//			@Override
//			public ListCell<Task> call(ListView<Task> ListViewTask) {
//				TaskCell cell = new TaskCell() {
//					@Override
//					protected void updateItem(Task t, boolean b) {
//						super.updateItem(t, b);
//						if (t != null) {
//							Node nodeDesc = hbox.getChildren().get(1);
//							Node nodeDetails = hbox.getChildren().get(2);
//					        if(nodeDesc instanceof Text) {
//					            nodeDesc = formatTask1(t);
//					            }
//							details = formatTask2(t);
//							setGraphic(hbox);
//						} else {
//							setGraphic(new Text(null));
//						}
//					}
//				};
//				return cell;
//			}
		});

	}

	private static Text formatTask1(Task t) {
		Text text;
		text = new Text(t.getIndex() + ". " + t.getTaskDesc());
		return text;
	}
	
	private static Text formatTask2(Task t) {
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