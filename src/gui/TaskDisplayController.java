package gui;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Vector;
import fileIO.FileStream;
import util.Output;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import logic.Logic;

//@author A0093906X
public class TaskDisplayController {

	@FXML
	private TextArea inputBox;

	@FXML
	private ListView<Task> listView = new ListView<Task>();

	@FXML
	private Label label = new Label();

	@FXML
	private Button slideButton = new Button();

	@FXML
	private ToggleButton showTimed = new ToggleButton();

	@FXML
	private ToggleButton showDeadline = new ToggleButton();

	@FXML
	private ToggleButton showFloating = new ToggleButton();

	@FXML
	private RadioButton dueToday = new RadioButton("Due Today");

	@FXML
	private RadioButton dueTomorrow = new RadioButton("Due in One Day");

	@FXML
	private RadioButton dueThisWeek = new RadioButton("Due This Week");

	@FXML
	private RadioButton dueThisMonth = new RadioButton("Due This Month");

	@FXML
	private RadioButton dueAllTime = new RadioButton("All Time");

	@FXML
	private VBox sideBar = new VBox();

	@FXML
	private ToolBar toolBar = new ToolBar();

	@FXML
	private Button minimize = new Button();

	@FXML
	private Button closeApp = new Button();

	private Timeline timelineOut;
	private Timeline timelineIn;
	private static String previousKey;

	private SimpleBooleanProperty isExpanded = new SimpleBooleanProperty();

	private ObservableList<Task> list;

	private Logic l = new Logic();

	private ListViewGUI gui;

	private Vector<Task> VectorTaskList;
	private Vector<Task> DisplayTaskList = new Vector<Task>();

	private Vector<String> commandHistory;
	private int commandHistoryIndex;
	private boolean autoCompleteState = true;

	// Selection Criteria for togglebuttons
	private boolean isTimedOn = true;
	private boolean isDeadlineOn = true;
	private boolean isFloatingOn = true;
	private String checkDue = "dueAllTime";

	final ToggleGroup timed = new ToggleGroup();
	final ToggleGroup deadline = new ToggleGroup();
	final ToggleGroup floating = new ToggleGroup();
	final ToggleGroup due = new ToggleGroup();

	// @author A0093906X
	class TaskCell extends ListCell<Task> {
		HBox hbox = new HBox();
		CheckBox done = new CheckBox();
		Text desc = new Text("(empty)");
		Text details = new Text("(empty)");
		int index = -1;
		Pane pane = new Pane();
		VBox buttonVBox = new VBox();
		VBox taskVBox = new VBox();
		Button flag = new Button();
		Button delete = new Button();

		// @author A0093906X
		public TaskCell() {
			super();

			delete.getStyleClass().add("delete");
			// flag.getStyleClass().add("flag");
			done.getStyleClass().add("done");

			taskVBox.getChildren().addAll(desc, details);
			buttonVBox.getChildren().addAll(flag, delete);
			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					processUserInput(("delete " + index));
				}
			});

			flag.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					processUserInput(("toggleflag " + index));
				}
			});
			done.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					processUserInput(("togglemark " + index));
				}
			});

			hbox.getChildren().addAll(done, taskVBox, pane, buttonVBox);
			HBox.setHgrow(pane, Priority.ALWAYS);

		}

		// @author A0093906X
		@Override
		protected void updateItem(Task t, boolean b) {
			super.updateItem(t, b);

			if (t != null) {
				getStyleClass().add("full");
				if (t.getTaskType().equals(TASK_TYPE.FLOATING_TASK)) {
					desc.getStyleClass().add("floating");
				}
				setGraphic(hbox);

				if (t.getDone()) {
					done.setSelected(true);
				}

				else {
					done.setSelected(false);
				}

				if (t.getDone()) {
					done.setSelected(true);
					desc.getStyleClass().add("strikethrough");
					desc.setFill(Color.GREY);
					details.getStyleClass().add("strikethrough");
					details.setFill(Color.GREY);
				}

				else if (t.isDue()) {
					done.setSelected(false);
					desc.getStyleClass().remove("strikethrough");
					desc.setFill(Color.FIREBRICK);
					details.getStyleClass().remove("strikethrough");
					details.setFill(Color.FIREBRICK);
				}

				else {
					done.setSelected(false);
					desc.getStyleClass().remove("strikethrough");
					desc.setFill(Color.WHITE);
					details.getStyleClass().remove("strikethrough");
					details.setFill(Color.WHITE);
				}

				hbox.setPrefWidth(350);
				if (t.isDue()) {
					desc.setText(formatTask1(t) + " (Overdue!)");
				} else {
					desc.setText(formatTask1(t));
				}
				desc.getStyleClass().add("desc");

				desc.setWrappingWidth(listView.getPrefWidth());
				details.setText(formatTask2(t));
				details.getStyleClass().add("details");

				flag.getStyleClass().remove("flag-selected");
				flag.getStyleClass().remove("flag");

				if (t.getFlag()) {
					flag.getStyleClass().add("flag-selected");
				} else {
					flag.getStyleClass().add("flag");
				}
				details.setWrappingWidth(listView.getPrefWidth());
				index = t.getIndex();
			}

			else {
				setGraphic(null);
			}

		}
	}

	// @author A0111855J
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

	// @author A0093906X
	private static String formatTask1(Task t) {
		return (t.getIndex() + ": " + t.getTaskDesc());
	}

	// @author A0093906X
	private static String formatTask2(Task t) {

		if (t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {

			return (String
					.format("%-27s\t%1s",
							"\nFrom: "
									+ TimeExtractor.formatDateTime(t
											.getStartTime()),
							"To: "
									+ TimeExtractor.formatDateTime(t
											.getEndTime()))).replace(' ', ' ');
		} else if (t.getTaskType().equals(TASK_TYPE.DEADLINE)) {

			return ("\nBy: " + TimeExtractor.formatDateTime(t.getEndTime()));
		}

		else {
			return ("\n");
		}
	}

	// @author A0111855J
	// This class records x,y co-ordinates as an object
	private class Delta {
		double x;
		double y;
	}
	
	public void moveUndercoratedBorder() {

		// customize toolBar to enable moving an undecorated application
		final Delta dragDelta = new Delta();
		toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = gui.getWindow().getX() - mouseEvent.getScreenX();
				dragDelta.y = gui.getWindow().getY() - mouseEvent.getScreenY();
			}
		});
		toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				gui.getWindow().setX(mouseEvent.getScreenX() + dragDelta.x);
				gui.getWindow().setY(mouseEvent.getScreenY() + dragDelta.y);
			}
		});
	}

	/** exit buttons for customized window border*/
	public void exitWindow() {
		closeApp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
	}

	// close buttons for customized window border
	public void minimizeWindow() {
		minimize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.minimizeWindow();
			}
		});
	}

	
	//@author A0093906X
	@FXML
	private void initialize() {
		moveUndercoratedBorder();
		exitWindow();
		minimizeWindow();

		VectorTaskList = l.initializeList();
		setTaskList(VectorTaskList);

		commandHistory = new Vector<String>();
		commandHistoryIndex = 0;

		inputBox.setPromptText("Enter Command:");
		inputBox.setWrapText(true);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inputBox.requestFocus();
			}
		});

		slideButton.getStyleClass().add("sld");
		minimize.getStyleClass().add("min");
		closeApp.getStyleClass().add("cls");

		showTimed.setToggleGroup(timed);
		showTimed.setSelected(true);
		showTimed.getStyleClass().add("selected");
		showDeadline.setToggleGroup(deadline);
		showDeadline.setSelected(true);
		showDeadline.getStyleClass().add("selected");
		showFloating.setToggleGroup(floating);
		showFloating.setSelected(true);
		showFloating.getStyleClass().add("selected");

		dueToday.setToggleGroup(due);
		dueToday.setUserData("dueToday");
		dueToday.getStyleClass().add("radio");
		dueTomorrow.setToggleGroup(due);
		dueTomorrow.setUserData("dueTomorrow");
		dueTomorrow.getStyleClass().add("radio");
		dueThisWeek.setToggleGroup(due);
		dueThisWeek.setUserData("dueThisWeek");
		dueThisWeek.getStyleClass().add("radio");
		dueThisMonth.setToggleGroup(due);
		dueThisMonth.setUserData("dueThisMonth");
		dueThisMonth.getStyleClass().add("radio");
		dueAllTime.setToggleGroup(due);
		dueAllTime.setUserData("dueAllTime");
		dueAllTime.getStyleClass().add("radio");
		dueAllTime.setSelected(true);

		sideBar.toBack();

		timed.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle toggle, Toggle new_toggle) {
						if (new_toggle == null) {
							isTimedOn = false;
							showTimed.getStyleClass().removeAll();
							// showTimed.getStyleClass().add("deselected");
						} else {
							isTimedOn = true;
							showTimed.getStyleClass().removeAll();
							// showTimed.getStyleClass().add("selected");
						}
						createDisplayTaskList();

						setTaskList(DisplayTaskList);
					}
				});

		deadline.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle toggle, Toggle new_toggle) {
						if (new_toggle == null) {
							isDeadlineOn = false;
							showDeadline.getStyleClass().removeAll();
							// showDeadline.getStyleClass().add("deselected");
						} else {
							isDeadlineOn = true;
							showDeadline.getStyleClass().removeAll();
							// showDeadline.getStyleClass().add("selected");
						}
						createDisplayTaskList();
						setTaskList(DisplayTaskList);
					}
				});

		floating.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle toggle, Toggle new_toggle) {
						if (new_toggle == null) {
							isFloatingOn = false;
							showFloating.getStyleClass().removeAll();
							// showFloating.getStyleClass().add("deselected");
						} else {
							isFloatingOn = true;
							showFloating.getStyleClass().removeAll();
							// showFloating.getStyleClass().add("selected");
						}
						createDisplayTaskList();
						setTaskList(DisplayTaskList);
					}
				});

		due.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null) {
					checkDue = "dueAllTime";
				} else {
					checkDue = new_toggle.getUserData().toString();
				}
				createDisplayTaskList();
				setTaskList(DisplayTaskList);
			}
		});

		GUIMsg feedback = new GUIMsg(System.out, label);
		System.setOut(feedback);
		System.setErr(feedback);
		label.setText("");
		setAnimation();

		slideButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent paramT) {
				togglePaneVisibility();
			}
		});

		isExpanded.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(
					ObservableValue<? extends Boolean> paramObservableValue,
					Boolean paramT1, Boolean paramT2) {
				if (paramT2) {
					// To expand sidebar
					timelineIn.play();
					gui.resetWindowWidth();
				} else {
					// To close sidebar
					timelineOut.play();
					gui.setWindowWidth();
				}
			}
		});

		sideBar.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(
					ObservableValue<? extends Boolean> paramObservableValue,
					Boolean paramT1, Boolean paramT2) {
				if (paramT2) {
					timelineIn.play();
				}
			}

		});

	}

	//@author A0105952H
	private void setAnimation() {

		sideBar.translateXProperty().set(-250);

		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		};
		timelineIn = new Timeline();
		timelineOut = new Timeline();

		/* Animation for sidebar to scroll right. */
		timelineIn.setCycleCount(1);
		timelineIn.setAutoReverse(true);

		final KeyValue kvDwn1 = new KeyValue(sideBar.translateXProperty(), 0);
		final KeyFrame kfDwn = new KeyFrame(Duration.millis(1), onFinished,
				kvDwn1);
		timelineIn.getKeyFrames().add(kfDwn);

		/* Animation for sidebar to scroll left. */
		timelineOut.setCycleCount(1);
		timelineOut.setAutoReverse(true);
		final KeyValue kvUp1 = new KeyValue(sideBar.translateXProperty(), -250);
		final KeyFrame kfUp = new KeyFrame(Duration.millis(100), kvUp1);
		timelineOut.getKeyFrames().add(kfUp);
	}

	//@author A0105952H
	private void togglePaneVisibility() {
		if (isExpanded.get()) {
			isExpanded.set(false);
		} else {
			isExpanded.set(true);
		}
	}

	@FXML
	private void handleInput() {

		final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.Z,
				KeyCombination.CONTROL_DOWN);
		final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.Y,
				KeyCombination.CONTROL_DOWN);

		// @author A0111855J
		inputBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)) {
					key.consume();

					String text = inputBox.getText();
					commandHistory.add(text);
					commandHistoryIndex = commandHistory.size();

					// Terminates program if exit, else refresh list
					if (!text.trim().toLowerCase().equals("exit")
							&& !text.trim().toLowerCase().equals("quit")) {
						processUserInput(text);

						// clear text area after each input
						inputBox.setText("");
					} else {
						System.exit(0);
					}

				}

				if (key.getCode().equals(KeyCode.F1)) {
					processUserInput("help");
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

				if (key.getCode().equals(KeyCode.ALT) && autoCompleteState) {
					turnOffAutoComplete();
				}

				else if (key.getCode().equals(KeyCode.ALT)
						&& !autoCompleteState) {
					turnOnAutoComplete();
				}

				if (key.getCode().equals(KeyCode.BACK_SPACE)) {
					commandHistoryIndex = commandHistory.size();
				}

				if (keyComb1.match(key)) {
					processUserInput("undo");
				}

				if (keyComb2.match(key)) {
					processUserInput("redo");
				}

			}

		});

		//@author A0105952H
		inputBox.addEventHandler(KeyEvent.KEY_RELEASED,
				new EventHandler<KeyEvent>() {
					String autoCompleteList[] = { "add ", "clear", "changedir",
							"create ", "delete ", "edit ", "exit", "flag ",
							"help", "mark ", "man", "search ", "undo", "redo",
							"prioritise ", "quit", "search task ",
							"search before ", "search date ", "search desc ",
							"search type ", "edit desc ", "edit task ",
							"edit start", "edit end", "edit starttime ",
							"edit startdate ", "edit endtime ", "edit enddate " };

					public void handle(KeyEvent key) {

						if (!autoCompleteState) {
							return;
						}
						boolean isPartOfWord = false;
						String input = inputBox.getText();

						for (String s : autoCompleteList) {
							input = input.replaceAll("\\s+", " ");
							if (!input.isEmpty()
									&& s.toLowerCase().startsWith(
											input.toLowerCase())) {
								Output.showToUser("Enter space to autocomplete");
								previousKey = s;
								isPartOfWord = true;
								break;
							}
						}
						if (key.getCode().equals(KeyCode.SPACE)) {
							if (previousKey != null) {
								inputBox.setText(previousKey);
								inputBox.end();
								previousKey = null;
								Output.showToUser(" ");
							}
						}
						if (key.getCode().equals(KeyCode.BACK_SPACE)) {
							Output.showToUser(" ");
							previousKey = null;
						} else if (!isPartOfWord
								&& !key.getCode().equals(KeyCode.ENTER)
								&& !key.getCode().equals(KeyCode.ALT)) {
							Output.showToUser(" ");
							previousKey = null;
						} else if (key.getCode().equals(KeyCode.ENTER)) {
							previousKey = null;
						}

					}
				});
	}

	// @author A0111855J
	private void showPrevCommandUp() {
		if (commandHistory.size() == 0) {
			return;
		}

		if (commandHistoryIndex == 0) {
			commandHistoryIndex = commandHistory.size();
		}

		if (commandHistoryIndex > 0) {
			commandHistoryIndex--;
		}

		String text = commandHistory.get(commandHistoryIndex);
		inputBox.setText(text);
		inputBox.positionCaret(text.length());

	}

	private void showPrevCommandDown() {
		if (commandHistoryIndex == commandHistory.size()) {
			return;
		}

		if (commandHistoryIndex == commandHistory.size() - 1) {
			commandHistoryIndex = -1;
		}

		if (commandHistoryIndex < commandHistory.size()) {
			commandHistoryIndex++;
		}

		String text = commandHistory.get(commandHistoryIndex);
		inputBox.setText(text);
		inputBox.positionCaret(text.length());

	}

	private void turnOnAutoComplete() {
		autoCompleteState = true;
		Output.showToUser("Auto-Complete On");
	}

	private void turnOffAutoComplete() {
		autoCompleteState = false;
		Output.showToUser("Auto-Complete Off");
	}

	public void setGUI(ListViewGUI listViewGUI) {
		this.gui = listViewGUI;

	}

	public void updateLabel(String s) {
		label.setText(s);
	}

	// @author A0093906X
	public void processUserInput(String str) {

		assert str != null : "String is null!";

		VectorTaskList = l.run(str);
		createDisplayTaskList();
		setTaskList(DisplayTaskList);
	}

	// @author A0093906X
	public void createDisplayTaskList() {
		DisplayTaskList.clear();
		for (Task t : VectorTaskList) {
			if (t.getTaskType().equals(TASK_TYPE.TIMED_TASK) && isTimedOn) {
				if (isDisplayedByDueDate(t)) {
					DisplayTaskList.add(t);
				}
			} else if (t.getTaskType().equals(TASK_TYPE.DEADLINE)
					&& isDeadlineOn) {
				if (isDisplayedByDueDate(t)) {
					DisplayTaskList.add(t);
				}
			} else if (t.getTaskType().equals(TASK_TYPE.FLOATING_TASK)
					&& isFloatingOn) {
				if (isDisplayedByDueDate(t)) {
					DisplayTaskList.add(t);
				}
			}
		}
	}

	// @author A0093906X
	public boolean isDisplayedByDueDate(Task t) {
		LocalDateTime end = t.getEndTime();
		if (end == null) {
			return true;
		}

		else {
			LocalDate taskDate = LocalDate.of(end.getYear(),
					end.getMonthValue(), end.getDayOfMonth());
			LocalDate nowDate = LocalDate.now();

			if (checkDue == "dueAllTime") {
				return true;
			}

			else if (checkDue == "dueThisMonth") {
				if (taskDate.isBefore(nowDate.plusDays(30))) {
					return true;
				} else {
					return false;
				}
			}

			else if (checkDue == "dueThisWeek") {
				if (taskDate.isBefore(nowDate.plusDays(7))) {
					return true;
				} else {
					return false;
				}
			}

			else if (checkDue == "dueTomorrow") {
				if (taskDate.isBefore(nowDate.plusDays(2))) {
					return true;
				} else {
					return false;
				}
			}

			else if (checkDue == "dueToday") {
				if (taskDate.isBefore(nowDate.plusDays(1))) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	//@author A0105952H
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