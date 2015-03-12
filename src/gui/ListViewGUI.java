package gui;

import java.io.IOException;
import java.util.Vector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import fileIO.FileStream;
import util.Task;

public class ListViewGUI extends Application {
	private Stage primaryStage;
	private BorderPane root;
	// private ObservableList<Task> list;
	private Vector<Task> vectorTasks;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ListView");

		vectorTasks = FileStream.loadTasksFromXML();

		initRootLayout();

		showTaskOverview();

		/*
		 * Initialize a simple display
		 * 
		 * StackPane root = new StackPane(); root.getChildren().add(listView);
		 * primaryStage.setScene(new Scene(root, 300, 450));
		 * primaryStage.setResizable(false); primaryStage.show();
		 */
	}

	private void initRootLayout() {
		root = new BorderPane();
		Scene scene = new Scene(root, 300, 450);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void showTaskOverview() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ListViewGUI.class
				.getResource("TaskDisplayPage.fxml"));
		AnchorPane TaskDisplayPage = (AnchorPane) loader.load();
		root.setCenter(TaskDisplayPage);

		TaskDisplayController controller = loader.getController();

		controller.setTaskList(vectorTasks);
	}

}
