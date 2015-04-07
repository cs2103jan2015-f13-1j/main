package gui;

import javafx.application.Application; 
import javafx.beans.value.ChangeListener; 
import javafx.beans.value.ObservableValue; 
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.layout.StackPane; 
import javafx.scene.layout.HBox; 
import javafx.scene.layout.VBox; 
import javafx.stage.Stage; 
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup; 
import javafx.scene.control.Toggle; 
import javafx.scene.control.Label;  
public class OptionGroupExamples extends Application {  
	@Override public void start(Stage primaryStage) {  
		final String toggleText = "The toggle selected is: \n"; 
		final String changedtoggleText = "The ToggleButton has changed from: \n";
		
		//Use HBOX layout panes to space out the controls 
		//in a single row 
		HBox togBox = new HBox(); 
		
		VBox optBox = new VBox(30); 
		VBox labelBox = new VBox(20); 
		HBox controlBox = new HBox(10);  
		StackPane labelStack = new StackPane(); 
		
		labelStack.setAlignment(Pos.CENTER);  
		labelStack.setStyle("-fx-font-size: 32"); 
		final Label fmLabel = new Label("FM"); 
		final Label amLabel = new Label("AM"); 
		amLabel.setVisible(false); 
		final Label mWaveLabel = new Label("MediumWave"); 
		mWaveLabel.setVisible(false);  
		labelStack.getChildren().addAll(fmLabel,amLabel,mWaveLabel);  
		
		//Create labels to highlight the selected items from the option controls 
		final Label toggleLabel = new Label(toggleText); 
		final Label changedtogLabel = new Label(changedtoggleText);  
		
		//Create the toggleButtons 
		//use the setUserData to pass data about the toggleButton 
		//to the ChangeListener 
		ToggleButton fmTog = new ToggleButton("FM"); 
		fmTog.setUserData("FM"); ToggleButton 
		amTog = new ToggleButton("AM"); 
		amTog.setUserData("AM"); ToggleButton 
		mwaveTog = new ToggleButton("Medium Wave"); 
		mwaveTog.setUserData("Medium Wave"); 
		
		//Create a new ToggleGroup 
		ToggleGroup stationGrp = new ToggleGroup();  
		//Add the toggleButtons to the ToggleGroup 
		fmTog.setToggleGroup(stationGrp); 
		amTog.setToggleGroup(stationGrp); 
		mwaveTog.setToggleGroup(stationGrp); 
		fmTog.setSelected(true);  
		
		//Create a changelistener to handle the switching between togglebuttons
		stationGrp.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){ 
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) { 
				toggleLabel.setText(toggleText + new_toggle.getUserData().toString()); if (old_toggle !=null) { 
					changedtogLabel.setText(changedtoggleText + old_toggle.getUserData().toString()); 
			} 
			
				switch (new_toggle.getUserData().toString()){ 
					case "FM": fmLabel.setVisible(true); 
					amLabel.setVisible(false); 
					mWaveLabel.setVisible(false); 
					break; 
				
					case "AM": fmLabel.setVisible(false); 
					amLabel.setVisible(true);
					mWaveLabel.setVisible(false); 
					break; 
					
					case "Medium Wave":  fmLabel.setVisible(false);
					amLabel.setVisible(false); 
					mWaveLabel.setVisible(true); 
					break; 
					} 
				} 
			});  
		//Add the toggleButtons to the HBOX layout pane 
		//Note how it is the togglebuttons and not the togglegroup  
		//that are added. 
		
		togBox.getChildren().add(fmTog); 
		togBox.getChildren().add(amTog); 
		togBox.getChildren().add(mwaveTog);  
		
		//Add togBox and the labelStack to a VBox 
		
		optBox.getChildren().add(togBox); 
		optBox.getChildren().add(labelStack);  
		
		//Add the labels VBOX layout pane 
		
		labelBox.getChildren().add(toggleLabel); 
		labelBox.getChildren().add(changedtogLabel);  
		
		//Add the HBox and VBox layout panes to the overall container 
		
		controlBox.getChildren().add(optBox); 
		controlBox.getChildren().add(labelBox);  
		
		//Add the main HBOX layout pane to the scene 
		
		Scene scene = new Scene(controlBox, 800, 250);  
		
		//Show the form 
		primaryStage.setTitle("Hello World!"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); }  
	
	/** * @param args the command line arguments */ 
	public static void main(String[] args) { 
		launch(args); 
		} 
	}
