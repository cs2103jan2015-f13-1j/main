import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

public class MainGUI extends Application {

	private Stage primaryStage;

	//Data as an observable list of Task(s) to track changes
	//Current dummy list
	private ObservableList<Task> taskData = FXCollections.observableArrayList();

	//Default constructor dummy
	public MainGUI() {

		taskData.add(new Task("Do homework1", "Tomorrow"));
		taskData.add(new Task("Do homework2", "Sunday"));
		taskData.add(new Task("Meet Up", "17/03/2015"));
		taskData.add(new Task("Testing a super super super super super long"
				+ "long long long long line of code", "23/03/2015"));
		taskData.add(new Task("Edit Stuffs", "26/03/2015"));
		taskData.add(new Task("Do homework10", "27/03/2015"));
	}

	//Returns data as observable list of Task
	public ObservableList<Task> getTaskData() {
		return taskData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ontask");

		initLayout();

	}

	//Initialize layout
	public void initLayout() {
		try {
			//load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainGUI.class.getResource("view/MainLayer.fxml"));
			AnchorPane mainLayout = (AnchorPane) loader.load();

			//Gives controller access to main
			MainController controller = loader.getController();
			controller.setMainGUI(this);

			//show the scene containing the main layout
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
