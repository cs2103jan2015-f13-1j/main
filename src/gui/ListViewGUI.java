package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ListViewGUI extends Application {
	private Stage primaryStage;
	private BorderPane root;
	private TaskDisplayController controller;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ontask");
		this.primaryStage.getIcons().add(new Image("file:images/icon.png"));

		initRootLayout();

		showTaskOverview();
	}

	private void initRootLayout() {
		

		root = new BorderPane();
		Scene scene = new Scene(root, 450, 600);
		primaryStage.setScene(scene);
		//primaryStage.setResizable(false);
		
		primaryStage.show();
	}
	
	public void resetWindowWidth(){
		primaryStage.setWidth(700);
	}
	
	public void setWindowWidth(){
		primaryStage.setWidth(450);
	}
	
	public Stage getWindow(){
		return primaryStage;
	}

	private void showTaskOverview() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ListViewGUI.class
				.getResource("TaskDisplayPage.fxml"));
		AnchorPane TaskDisplayPage = (AnchorPane) loader.load();
		root.setCenter(TaskDisplayPage);

		controller = loader.getController();

		controller.setGUI(this);

	}
}
