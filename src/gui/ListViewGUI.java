package gui;

import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import filestream.FileStream;
import util.Task;

public class ListViewGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ListView");

		/*
		 * Takes in a Vector<Task> and converts it into ObservableList<Task>
		 */
		Vector<Task> vectorTasks = FileStream.loadTasksFromXML();
		ListView<Task> listView = new ListView<Task>();
		ObservableList<Task> list = FXCollections.observableArrayList(vectorTasks);

		/*
		 * Allows custom object <Task> to be displayed in Listview
		 */
		listView.setItems(list);
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			
			@Override
			public ListCell<Task> call(ListView<Task> ListViewTask) {
				ListCell<Task> cell = new ListCell<Task>() {
					
					@Override
					protected void updateItem(Task t, boolean b) {
						super.updateItem(t, b);
						if(t != null) {
							setText(t.getTaskDesc() + "\nFrom: " + t.getStartTime() + " To: "+ t.getEndTime());
						}
					}
				};
				return cell;
			}
		});
		
		
		/*
		 * Initialize a simple display
		 */
		StackPane root = new StackPane();
		root.getChildren().add(listView);
		primaryStage.setScene(new Scene(root, 300, 450));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
