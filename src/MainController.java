import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

	@FXML
	private TableView<Task> taskTable;
	@FXML
	private TableColumn<Task, String> taskDescColumn;
	@FXML
	private TableColumn<Task, String> dateColumn;
	@FXML
	private TableColumn<Task, String> startTimeColumn;
	@FXML
	private TableColumn<Task, String> endTimeColumn;
	
	//Reference to mainGUI
	private MainGUI mainGUI;

	//call constructor before initialize() method
	public MainController() {
	}

	//Initialize controller class. Method is called after fxml file has been loaded
	@FXML
	private void initialize() {
		//Initialize tables information.
		taskDescColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("taskDesc"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("date"));
		startTimeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("startTime"));
		endTimeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("endTime"));
		
		//Listen for changes and update list when changed
		//NOT DONE
	}

	//Method called to give a reference back to itself
	public void setMainGUI(MainGUI mainGUI) {
		this.mainGUI = mainGUI;

		//Add observable list data to the table
		taskTable.setItems(mainGUI.getTaskData());
	}

}
