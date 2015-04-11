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
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import logic.Logic;

public class TaskDisplayController {

	@FXML
	private TextArea inputBox;

	@FXML
	private ListView<Task> listView = new ListView<Task>();

	@FXML
	private Label label = new Label();

	@FXML
	private Label slide = new Label();

	@FXML
	private ToggleButton showTimed = new ToggleButton();

	@FXML
	private ToggleButton showDeadline = new ToggleButton();

	@FXML
	private ToggleButton showFloating = new ToggleButton();

	@FXML
	private RadioButton dueToday = new RadioButton("Due Today");

	@FXML
	private RadioButton dueTomorrow = new RadioButton("Due Tomorrow");

	@FXML
	private RadioButton dueThisWeek = new RadioButton("Due This Week");

	@FXML
	private RadioButton dueThisMonth = new RadioButton("Due This Month");

	@FXML
	private RadioButton dueAllTime = new RadioButton("All Time");

	@FXML
	private VBox sideBar = new VBox();

	private Timeline timelineUp;
	private Timeline timelineDown;
	private static String previousKey;

	private StackPane rightArrow = StackPaneBuilder
			.create()
			.style("-fx-padding: 8px 5px 0px 5px;-fx-background-color: black;-fx-shape: \"M1 0 L1 1 L0 .5 Z\";")
			.maxHeight(10).maxWidth(15).build();
	private StackPane leftArrow = StackPaneBuilder
			.create()
			.style("-fx-padding: 8px 5px 0px 5px;-fx-background-color: black;-fx-shape: \"M0 0 L0 1 L1 .5 Z\";")
			.maxHeight(10).maxWidth(15).build();
	private SimpleBooleanProperty isExpanded = new SimpleBooleanProperty();

	private ObservableList<Task> list;

	private Logic l = new Logic();

	private ListViewGUI gui;

	private Vector<Task> VectorTaskList;
	private Vector<Task> DisplayTaskList = new Vector<Task>();

	private Vector<String> commandHistory;
	private int commandHistoryIndex;

	// Selection Criteria for togglebuttons
	private boolean isTimedOn = true;
	private boolean isDeadlineOn = true;
	private boolean isFloatingOn = true;
	private String checkDue = "dueAllTime";

	final ToggleGroup timed = new ToggleGroup();
	final ToggleGroup deadline = new ToggleGroup();
	final ToggleGroup floating = new ToggleGroup();
	final ToggleGroup due = new ToggleGroup();

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

		@Override
		protected void updateItem(Task t, boolean b) {
			super.updateItem(t, b);

			if (t != null) {
				getStyleClass().add("full");
				setGraphic(hbox);

				if (t.getDone()) {

					done.setSelected(true);
				}

				else {
					done.setSelected(false);
				}

				if (t.getDone()) {
					done.setSelected(true);
					// setStyle("done");
					this.getStyleClass().add("done");
					desc.getStyleClass().add("strikethrough");
					details.getStyleClass().add("strikethrough");
				} else {
					done.setSelected(false);
				}

				hbox.setPrefWidth(400);
				desc.setText(formatTask1(t));
				desc.setWrappingWidth(listView.getPrefWidth());
				details.setText(formatTask2(t));
				details.setWrappingWidth(listView.getPrefWidth());
				index = t.getIndex();
			}

			else {
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

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inputBox.requestFocus();
			}
		});

		showTimed.setToggleGroup(timed);
		showTimed.setSelected(true);
		showDeadline.setToggleGroup(deadline);
		showDeadline.setSelected(true);
		showFloating.setToggleGroup(floating);
		showFloating.setSelected(true);

		dueToday.setToggleGroup(due);
		dueToday.setUserData("dueToday");
		dueTomorrow.setToggleGroup(due);
		dueTomorrow.setUserData("dueTomorrow");
		dueThisWeek.setToggleGroup(due);
		dueThisWeek.setUserData("dueThisWeek");
		dueThisMonth.setToggleGroup(due);
		dueThisMonth.setUserData("dueThisMonth");
		dueAllTime.setToggleGroup(due);
		dueAllTime.setUserData("dueAllTime");
		dueAllTime.setSelected(true);

		sideBar.toBack();

		timed.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle toggle, Toggle new_toggle) {
						if (new_toggle == null) {
							isTimedOn = false;
						} else {
							isTimedOn = true;
						}
						cleanDisplayTaskList();
						Output.showToUser(DisplayTaskList.size() + "");

						setTaskList(DisplayTaskList);
					}
				});

		deadline.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle toggle, Toggle new_toggle) {
						if (new_toggle == null) {
							isDeadlineOn = false;
						} else {
							isDeadlineOn = true;
						}
						cleanDisplayTaskList();
						setTaskList(DisplayTaskList);
					}
				});

		floating.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle toggle, Toggle new_toggle) {
						if (new_toggle == null) {
							isFloatingOn = false;
						} else {
							isFloatingOn = true;
						}
						cleanDisplayTaskList();
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
				cleanDisplayTaskList();
				setTaskList(DisplayTaskList);
			}
		});

		GUIMsg feedback = new GUIMsg(System.out, label);
		System.setOut(feedback);
		System.setErr(feedback);
		label.setText("");
		slide.setText("slide");
		slide.setGraphic(rightArrow);
		setAnimation();

		slide.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
					// To expand
					timelineDown.play();
					slide.setGraphic(leftArrow);
					gui.resetWindowWidth();
				} else {
					// To close
					timelineUp.play();
					slide.setGraphic(rightArrow);
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
					timelineDown.play();
					slide.setGraphic(leftArrow);
				}
			}

		});

	}

	private void setAnimation() {

		sideBar.translateXProperty().set(-250);

		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		};
		timelineDown = new Timeline();
		timelineUp = new Timeline();

		/* Animation for scroll right. */
		timelineDown.setCycleCount(1);
		timelineDown.setAutoReverse(true);

		final KeyValue kvDwn1 = new KeyValue(sideBar.translateXProperty(), 0);
		final KeyFrame kfDwn = new KeyFrame(Duration.millis(1), onFinished,
				kvDwn1);
		timelineDown.getKeyFrames().add(kfDwn);

		/* Animation for scroll up. */
		timelineUp.setCycleCount(1);
		timelineUp.setAutoReverse(true);
		final KeyValue kvUp1 = new KeyValue(sideBar.translateXProperty(), -250);
		final KeyFrame kfUp = new KeyFrame(Duration.millis(100), kvUp1);
		timelineUp.getKeyFrames().add(kfUp);
	}

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
		final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.R,
				KeyCombination.CONTROL_DOWN);

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

		inputBox.addEventHandler(KeyEvent.KEY_RELEASED,
				new EventHandler<KeyEvent>() {
					String autoCompleteList[] = { "add ", "changedir",
							"create ", "delete ", "edit ", "exit", "flag",
							"mark ", "search ", "undo", "redo", "prioritise",
							"quit", "search task ", "search before ",
							"search date ", "search type ", "edit desc ",
							"edit task ", "edit start", "edit end",
							"edit starttime ", "edit startdate ",
							"edit endtime ", "edit enddate " };

					public void handle(KeyEvent key) {

						String input = inputBox.getText();

						for (String s : autoCompleteList) {
							input = input.replaceAll("\\s+", " ");
							if (!input.isEmpty()
									&& s.toLowerCase().startsWith(input)) {
								Output.showToUser("Enter space to autocomplete");
								previousKey = s;
								break;
							}
						}
						if (key.getCode().equals(KeyCode.BACK_SPACE)) {
							Output.showToUser(" ");
							previousKey = null;
						}

						if (key.getCode().equals(KeyCode.SPACE)) {
							if (previousKey != null) {
								inputBox.setText(previousKey);
								inputBox.end();
								previousKey = null;
								Output.showToUser(" ");
							}
						}
					}
				});
	}

	private void showPrevCommandUp() {
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
		cleanDisplayTaskList();
		setTaskList(DisplayTaskList);
	}

	public void cleanDisplayTaskList() {
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