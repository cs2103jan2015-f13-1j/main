package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
		this.primaryStage.initStyle(StageStyle.UNDECORATED);	//removes default windows border
        this.primaryStage.getIcons().add(new Image("file:images/icon.png"));

		initRootLayout();
		showTaskOverview();
		
	}
	
	private void initRootLayout() {

		root = new BorderPane();

		Scene scene = new Scene(root, 450, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		
	}

	public void resetWindowWidth() {
		primaryStage.setResizable(true);
		primaryStage.setWidth(700);
		primaryStage.setResizable(false);
	}

	public void setWindowWidth() {
		primaryStage.setResizable(true);
		primaryStage.setWidth(450);
		primaryStage.setResizable(false);
	}

	public void minimizeWindow() {
		primaryStage.setIconified(true);
	}
	
	public Stage getWindow() {
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
