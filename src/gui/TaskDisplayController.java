package gui;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

import fileIO.FileStream;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	@FXML
	private ToggleButton showTimed = new ToggleButton();

	@FXML
	private ToggleButton showDeadline = new ToggleButton();

	@FXML
	private ToggleButton showFloating = new ToggleButton();

	private ObservableList<Task> list;

	private Logic l = new Logic();

	private ListViewGUI gui;

	private Vector<Task> VectorTaskList;
	
	Vector<Task> removed = new Vector<Task>();
	
	private Vector<String> commandHistory;
	private int commandHistoryIndex;
	
	private String SelCrit = null; //Selection Criteria for togglebuttons
	
	final ToggleGroup group = new ToggleGroup();

	 class TaskCell extends ListCell<Task> {
		HBox hbox = new HBox();
		CheckBox done = new CheckBox();
		Text desc = new Text("(empty)");
		Text details = new Text("(empty)");
		int index = -1;
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
					processUserInput(("delete "+index));
				}
			});

			flag.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					processUserInput(("toggleflag "+index));
				}
			});
			hbox.getChildren().addAll(done, taskVBox, pane, buttonVBox);
			HBox.setHgrow(pane, Priority.ALWAYS);

		}

		@Override
		protected void updateItem(Task t, boolean b) {
			super.updateItem(t, b);

			inputBox.setText("Displaying all when Updated: SelCrit is "+SelCrit);
			
//			if(SelCrit==null) 	
//				inputBox.setText("Displaying all when Updated: SelCrit is "+SelCrit);
//			else if(SelCrit.equalsIgnoreCase("timed") && !t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {
//				setGraphic(null);
//				inputBox.setText("Displaying timed when Updated: SelCrit is "+SelCrit);
//				return;
//			}
//			else if(SelCrit.equalsIgnoreCase("deadline") && !t.getTaskType().equals(TASK_TYPE.DEADLINE)) {
//				setGraphic(null);
//				inputBox.setText("Displaying deadlined when Updated: SelCrit is "+SelCrit);
//				return;
//			}
//			else if(SelCrit.equalsIgnoreCase("floating") && !t.getTaskType().equals(TASK_TYPE.FLOATING_TASK)) {
//				inputBox.setText("Displaying floating when Updated: SelCrit is "+SelCrit);
//				setGraphic(null);
//				return;
//			}
			
			
			if (t != null) {
				desc.setText(formatTask1(t));
				desc.setWrappingWidth(listView.getMinWidth());
				details.setText(formatTask2(t));
				details.setWrappingWidth(listView.getMinWidth());
				index = t.getIndex();
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
		
		commandHistory = new Vector<String>();
		commandHistoryIndex = 0;

		inputBox.setPromptText("Enter Command:");
		inputBox.setWrapText(true);
		
		showTimed.setToggleGroup(group);
		showDeadline.setToggleGroup(group);
		showFloating.setToggleGroup(group);
		showTimed.setUserData("timed");
		showDeadline.setUserData("deadline");
		showFloating.setUserData("floating");
		
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if(new_toggle == null) {
					SelCrit=null;
				}
				else {
					SelCrit = (String)new_toggle.getUserData();
				}
//				ObservableList<Task> tasks = FXCollections.observableArrayList((Task[])listView.getItems().toArray());

//				listView.setItems(null);				
				
				VectorTaskList = l.run("");
				for(Task t: VectorTaskList) {
					if(SelCrit.equalsIgnoreCase("timed") && !t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {
						VectorTaskList.remove(t);
					}
					else if(SelCrit.equalsIgnoreCase("deadline") && !t.getTaskType().equals(TASK_TYPE.DEADLINE)) {
						VectorTaskList.remove(t);
					}
					else if(SelCrit.equalsIgnoreCase("floating") && !t.getTaskType().equals(TASK_TYPE.FLOATING_TASK)) {
						VectorTaskList.remove(t);
					}
				}
				
				inputBox.setText("Handler run");
				
				listView.getItems().clear();
				listView.getItems().addAll(VectorTaskList);
				ObservableList<Task> tasks = listView.getItems();

				listView.setItems(null);
				listView.setItems(tasks);

//				for (Task task : removed) {
//					tasks.add(task);
//					removed.remove(task);
//				}				

//				for(Task t : tasks) {
//					if(SelCrit==null) {
//						//do nothing
//					}
//					if(SelCrit.equalsIgnoreCase("timed") && !t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {
//						tasks.remove(t);
//						removed.add(t);
//					}
//				}
				
//				if(SelCrit.equalsIgnoreCase("timed")) {
//					tasks.remove(1);
//				}
					
//				listView.setItems(tasks);
			}
		});
		
		
		
		System.out.println("handler set");	
				

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
					commandHistory.add(text);
					commandHistoryIndex = commandHistory.size();

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
				
				if (key.getCode().equals(KeyCode.UP)) {
					showPrevCommandUp();
				}
				
				if (key.getCode().equals(KeyCode.DOWN)) {
					showPrevCommandDown();
				}
				
				if (key.getCode().equals(KeyCode.CONTROL) && key.getCode().equals(KeyCode.A)) {
					inputBox.selectAll();
				}
				
				if (key.getCode().equals(KeyCode.BACK_SPACE)) {
					commandHistoryIndex = commandHistory.size();
				}

			}
		});
	}
	
	private void showPrevCommandUp() {
		if(commandHistoryIndex == 0) {
			commandHistoryIndex = commandHistory.size();
		}
		
		if(commandHistoryIndex > 0) {
			commandHistoryIndex--;
		} 
		
		String text = commandHistory.get(commandHistoryIndex);
		inputBox.setText(text);
		inputBox.positionCaret(text.length());
		

	}
	
	private void showPrevCommandDown() {
		if(commandHistoryIndex == commandHistory.size()-1) {
			commandHistoryIndex = -1;;
		}
		
		if(commandHistoryIndex < commandHistory.size()) {
			commandHistoryIndex++;
		}
		
		String text = commandHistory.get(commandHistoryIndex);
 		inputBox.setText(text);
		inputBox.positionCaret(text.length());	
		
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