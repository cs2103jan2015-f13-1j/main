package gui;

import java.io.IOException;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logic.Logic;
import fileIO.FileStream;
import util.Task;

public class ListViewGUI extends Application {
	private Stage primaryStage;
	private BorderPane root;
	// private ObservableList<Task> list;
	private Vector<Task> vectorTasks;
	private TaskDisplayController controller;
	private Logic l = new Logic();

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

		controller = loader.getController();
		
		controller.setGUI(this);

		controller.setTaskList(vectorTasks);
	}
	
	public void processUserInput(String str){
		vectorTasks = l.run(str);
		
		controller.updateTaskList(vectorTasks);
	}
	

}
