package gui;

import java.util.Scanner;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Logic;
import fileIO.FileStream;
import util.Task;

import java.time.LocalDateTime;

public class MainGUI extends Application {

	private Vector<Task> vectorTasks = new Vector<Task>();
	private ListView<Task> listView = new ListView<Task>();
	private ObservableList<Task> observeList;
	private Logic l = new Logic();

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ListView");

		/*
		 * Takes in a Vector<Task> and converts it into ObservableList<Task>
		 */
		vectorTasks = FileStream.loadTasksFromXML();
		listView = new ListView<Task>();
		observeList = FXCollections.observableArrayList(vectorTasks);

		/*
		 * Allows custom object <Task> to be displayed in Listview
		 */
		listView.setItems(observeList);
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> ListViewTask) {
				ListCell<Task> cell = new ListCell<Task>() {

					@Override
					protected void updateItem(Task t, boolean b) {
						super.updateItem(t, b);
						if (t != null) {
							if (t.getStartTime() != null) {
								setText(super.getIndex() + 1 + "."
										+ t.getTaskDesc() + "\nFrom: "
										+ t.getStartTime() + " To: "
										+ t.getEndTime());
							} else if (t.getEndTime() != null) {
								setText(super.getIndex() + 1 + "."
										+ t.getTaskDesc() + "\nBy: "
										+ t.getEndTime());
							} else {
								setText(super.getIndex() + 1 + "."
										+ t.getTaskDesc() + "\n");
							}

						} else {
							setText("");
						}
					}
				};
				return cell;
			}
		});

		/*
		 * Testing Listener
		 */
		observeList.addListener(new ListChangeListener<Task>() {

			@Override
			public void onChanged(Change c) {
				System.out.println("change occur");
				while (c.next()) {
					System.out.println("next: ");
					if (c.wasAdded()) {
						System.out.println("- wasAdded");
					}
					if (c.wasPermutated()) {
						System.out.println("- wasPermutated");
					}
					if (c.wasRemoved()) {
						System.out.println("- wasRemoved");
					}
					if (c.wasReplaced()) {
						System.out.println("- wasReplaced");
					}
					if (c.wasUpdated()) {
						System.out.println("- wasUpdated");
					}
				}
			}
		});

		/*
		 * TextArea to receive input from user and parse it as a String
		 */
		TextArea textArea = new TextArea();
		textArea.setMinHeight(100);
		textArea.setWrapText(true);

		textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					// Prevents the enter key from doing a newline
					keyEvent.consume();

					String text = textArea.getText();

					// execute commanand
					// refresh list
					if (!text.toLowerCase().equals("exit")) {
						vectorTasks = l.run(text);
						observeList = FXCollections
								.observableArrayList(vectorTasks);
						listView.setItems(observeList);

						// clear text
						textArea.setText("");
					}
					else{
						System.exit(0);
					}
				}
			}
		});

		/*
		 * Initialize a simple display
		 */
		StackPane topList = new StackPane();
		topList.getChildren().add(listView);
		topList.setPrefSize(300, 400);

		StackPane bottomtextArea = new StackPane();
		bottomtextArea.getChildren().add(textArea);
		bottomtextArea.setPrefSize(300, 100);

		final VBox root = new VBox(topList, bottomtextArea);

		primaryStage.setScene(new Scene(root, 300, 500));
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
