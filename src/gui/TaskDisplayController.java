package gui;

import java.io.OutputStream;
import java.io.PrintStream;
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

	 class TaskCell extends ListCell<Task> {
		HBox hbox = new HBox();
		CheckBox done = new CheckBox();
		Text desc = new Text("(empty)");
		Text details = new Text("(empty)");
		Pane pane = new Pane();
		VBox buttonVBox = new VBox();
		VBox taskVBox = new VBox();
		Button flag = new Button("Flag");
		Button delete = new Button("Del");

		public TaskCell() {
			super();
			taskVBox.getChildren().addAll(desc, details);
			buttonVBox.getChildren().addAll(flag, delete);
			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("DELETE!");
				}
			});

			flag.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("THIS IS IMPORTANT!");
					processUserInput("haha");
				}
			});
			hbox.getChildren().addAll(done, taskVBox, pane, buttonVBox);
			HBox.setHgrow(pane, Priority.ALWAYS);

		}

		@Override
		protected void updateItem(Task t, boolean b) {
			super.updateItem(t, b);
			if (t != null) {
				desc.setText(formatTask1(t));
				details.setText(formatTask2(t));
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
			// @Override
			// public ListCell<Task> call(ListView<Task> ListViewTask) {
			// TaskCell cell = new TaskCell() {
			// @Override
			// protected void updateItem(Task t, boolean b) {
			// super.updateItem(t, b);
			// if (t != null) {
			// Node nodeDesc = hbox.getChildren().get(1);
			// Node nodeDetails = hbox.getChildren().get(2);
			// if(nodeDesc instanceof Text) {
			// nodeDesc = formatTask1(t);
			// }
			// details = formatTask2(t);
			// setGraphic(hbox);
			// } else {
			// setGraphic(new Text(null));
			// }
			// }
			// };
			// return cell;
			// }
		});

	}

	private static String formatTask1(Task t) {
		return (t.getIndex() + ". " + t.getTaskDesc());
	}

	private static String formatTask2(Task t) {

		if (t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {
			return ("\nFrom: " + TimeExtractor.formatDateTime(t.getStartTime())
					+ " To: " + TimeExtractor.formatDateTime(t.getEndTime()));

		} else if (t.getTaskType().equals(TASK_TYPE.DEADLINE)) {
			return ("\nBy: " + TimeExtractor.formatDateTime(t.getEndTime()));
		} else {
			return ("\n");
		}
	}

	@FXML
	private void initialize() {
		VectorTaskList = l.initializeList();
		
		setTaskList(VectorTaskList);

		inputBox.setPromptText("Enter Command:");
		inputBox.setWrapText(true);

		GUIMsg feedback = new GUIMsg(System.out, label);
		System.setOut(feedback);
		System.setErr(feedback);
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
					if (!text.toLowerCase().equals("exit")
							&& !text.toLowerCase().equals("quit")) {
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

		// Comments on replacing listView.setItems with the following 2 lines:
		// This theoretically works the same way, but the 2 lines will fix the
		// way listView updates accordingly.
		// listView.setItems(list);
		listView.getItems().clear();
		listView.getItems().addAll(VectorTaskList);

	}

	public class GUIMsg extends PrintStream {
		private Label label;

		public GUIMsg(OutputStream out, Label label) {
			super(out);
			this.label = label;
		}

		@Override
		public synchronized void write(byte[] buf, int off, int len) {
			final String message = new String(buf, off, len);

			label.setText(message);
		}

		@Override
		public synchronized void write(int i) {
			label.setText(String.valueOf((char) i));
		}
	}

}