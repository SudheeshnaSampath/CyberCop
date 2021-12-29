/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;  
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws DataException{
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//write your code here
		ccView.closeFileMenuItem.setDisable(true);
		ccView.addCaseMenuItem.setDisable(true);
		ccView.modifyCaseMenuItem.setDisable(true);
		ccView.deleteCaseMenuItem.setDisable(true);
		//Open Menu EventHandler
		ccView.openFileMenuItem.setOnAction((event) -> {
			ccView.isFileOpen.set(true);
			ccView.openFileMenuItem.setDisable(true);
			ccView.closeFileMenuItem.setDisable(false);
			ccView.addCaseMenuItem.setDisable(false);
			ccView.modifyCaseMenuItem.setDisable(false);
			ccView.deleteCaseMenuItem.setDisable(false);
			//Selecting file from Directory
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(APP_TITLE);
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File file = fileChooser.showOpenDialog(stage);

			//Reading case and year list from CCModel
			ccModel.readCases(file.toString());
			ccModel.buildYearMapAndList();

			//Loading case list to case table view and year list to year combo box
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.yearComboBox.setItems(ccModel.yearList);

			//set default value to first record
			ccView.caseTableView.getSelectionModel().select(0);
			currentCase = 	ccView.caseTableView.getSelectionModel().getSelectedItem();
			ccView.yearComboBox.getSelectionModel().select(currentCase.getCaseDate().substring(0, 4));
			ccView.titleTextField.setText(currentCase.getCaseTitle());
			ccView.caseTypeTextField.setText(currentCase.getCaseType());
			ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
			ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

			//update stage title and message label 
			stage.setTitle(file.toString());
			ccView.messageLabel.setText( ccModel.caseList.size()+" cases.");

			//addListener to bind the caseTableView to selected record and set currentCase accordingly
			ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				currentCase=newValue;
				if (newValue != null) {
					for (Case currentCase : ccModel.caseList) {
						if (currentCase.getCaseTitle().equalsIgnoreCase(newValue.getCaseTitle())) {
							//set text field and ComboBox to selected value
							ccView.yearComboBox.getSelectionModel().select(currentCase.getCaseDate().substring(0, 4));
							ccView.titleTextField.setText(currentCase.getCaseTitle());
							ccView.caseTypeTextField.setText(currentCase.getCaseType());
							ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
							ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
							//Attach URL Links
							if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
								URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
								if (url != null) ccView.webEngine.load(url.toExternalForm());
							} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
								ccView.webEngine.load(currentCase.getCaseLink());
							} else {
								URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
								if (url != null) ccView.webEngine.load(url.toExternalForm());
							}
						}
					}
				}
			});

		});

		//Close Menu EventHandler
		ccView.closeFileMenuItem.setOnAction((event) -> {
			//clear all gui components 
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();

			ccView.yearComboBox.getItems().clear();
			ccView.caseTableView.getItems().clear();

			stage.setTitle("");
			ccView.messageLabel.setText("");
			ccView.openFileMenuItem.setDisable(false);
			ccView.closeFileMenuItem.setDisable(true);
			ccView.addCaseMenuItem.setDisable(true);
			ccView.modifyCaseMenuItem.setDisable(true);
			ccView.deleteCaseMenuItem.setDisable(true);
		});

		//Exit Menu EventHandler
		ccView.exitMenuItem.setOnAction((event) -> {
			//leave application platform
			Platform.exit();
		});

		//SearchButton EventHandler
		ccView.searchButton.setOnAction((event) -> {
			//check text field values against searchCases() method from ccModel
			String title= ccView.titleTextField.getText();
			String type= ccView.caseTypeTextField.getText();
			String year= ccView.yearComboBox.getValue();
			String caseNumber= ccView.caseNumberTextField.getText();
			List<Case> searchresults = ccModel.searchCases(title,type,year,caseNumber);
			//cast list to observableList
			ObservableList<Case> searchResults =  FXCollections.observableArrayList(searchresults);
			ccView.caseTableView.setItems(searchResults);
			//display caseList size
			ccView.messageLabel.setText(searchResults.size()+" cases.");
		});

		//ClearButton EventHandler
		ccView.clearButton.setOnAction((event) ->{
			//clear all required fields
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.yearComboBox.getSelectionModel().clearSelection();
			ccView.messageLabel.setText("");

		});

		//Call the CaseMenuItemHandler using addCaseMenuItem, modifyCaseMenuItem, deleteCaseMenuItem
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());

		//SaveMenuItemHandler
		ccView.saveFileMenuItem.setOnAction((event) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(APP_TITLE);
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File file = fileChooser.showSaveDialog(stage);
			if (ccModel.writeCases(file.getAbsolutePath())) {
				ccView.messageLabel.setText(file.getName()+" saved.");
			}
			else {
				ccView.messageLabel.setText("File save failed");
			}
		});
		

		//CaseCountChartMenuItemHandler		
		ccView.caseCountChartMenuItem.setOnAction((event) -> {
			ccView.showChartView(ccModel.yearMap);
		});

	}

	public class CaseMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			MenuItem mi= (MenuItem)event.getSource();
			switch(mi.getText()) {
			case "Add case": 				//if Add Case menu Item is selected	
				AddCaseView addCV=new AddCaseView(mi.getText()); //create instance of AddCaseView
				addCV.buildView().show();			//call buildView() method
				addCV.updateButton.setOnAction((e)->{       //setOnAction when updateButton is selected
					//Add case records entered  text fields to caseList
					String caseDate = addCV.caseDatePicker.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					String title = addCV.titleTextField.getText();
					String type = addCV.caseTypeTextField.getText();
					String caseNumber = addCV.caseNumberTextField.getText();
					String caseLink = addCV.caseLinkTextField.getText();
					String caseCategory = addCV.categoryTextField.getText();
					String caseNotes = addCV.caseNotesTextArea.getText();
					//check for missing values or duplicate case number entry and throw data exception accordingly
					try {
						if(caseDate.isEmpty()|title.isEmpty()|type.isEmpty()|caseNumber.isEmpty()) {
							throw new DataException("Cases must have date, title, type and case number");
						}else if(ccModel.caseMap.containsKey(caseNumber)){
							throw new DataException("Duplicate case number");
						}else {
							ccModel.caseList.add(new Case(caseDate, title, type, caseNumber, caseLink,caseCategory,caseNotes));
							ccModel.buildYearMapAndList();
							ccView.caseTableView.setItems(ccModel.caseList); 	//save new case records to tableView
							ccView.yearComboBox.setItems(ccModel.yearList);
							ccView.messageLabel.setText( ccModel.caseList.size()+" cases."); //set caseList size
						}
					}catch(DataException de) {
						//de.printStackTrace();
					}

				}); 
				addCV.clearButton.setOnAction((e)->{  //clear all textfields in addMenuItem stage 
					addCV.titleTextField.clear();
					addCV.caseTypeTextField.clear();
					addCV.caseNumberTextField.clear();
					addCV.caseNotesTextArea.clear();
					addCV.caseDatePicker.setValue(LocalDate.now());	    	
				});
				addCV.closeButton.setOnAction((e)->{ //close addMenuItem stage 
					addCV.stage.close();
				});
				break;
			case "Modify case":			//if Modify Case menu Item is selected	
				ModifyCaseView mCV=new ModifyCaseView(mi.getText()); //creating instance of ModifyCaseView
				mCV.buildView().show();
				//parsing current values to show in Case View
				mCV.caseDatePicker.setValue(LocalDate.parse(currentCase.getCaseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))); 
				mCV.titleTextField.setText(currentCase.getCaseTitle());
				mCV.caseTypeTextField.setText(currentCase.getCaseType());
				mCV.caseNumberTextField.setText(currentCase.getCaseNumber());
				mCV.caseLinkTextField.setText(currentCase.getCaseLink());
				mCV.caseNotesTextArea.setText(currentCase.getCaseNotes());					
				//add case (update) Button in Add Case values to show in Case View
				mCV.updateButton.setOnAction((e)->{
					//set currentCase to values entered in modified menu item stage
					String caseDate = mCV.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					String title= mCV.titleTextField.getText();
					String type = mCV.caseTypeTextField.getText();
					String caseNumber = mCV.caseNumberTextField.getText();
					//check for missing values or duplicate case number entry and throw data exception accordingly
					try {
						if(caseDate.isEmpty()|title.isEmpty()|type.isEmpty()|caseNumber.isEmpty()) {
							throw new DataException("Cases must have date, title, type and case number");
						}else if(ccModel.caseMap.containsKey(caseNumber)){
							throw new DataException("Duplicate case number");
						}else {
							//replace currentCase values in caseList accordingly
							currentCase.setCaseDate(caseDate);
							currentCase.setCaseTitle(title);
							currentCase.setCaseType(type);
							currentCase.setCaseNumber(caseNumber);
							currentCase.setCaseLink(mCV.caseLinkTextField.getText());
							currentCase.setCaseCateogry(mCV.categoryTextField.getText());
							currentCase.setCaseNotes(mCV.caseNotesTextArea.getText()); 
							ccModel.caseList.set(ccModel.caseList.indexOf(currentCase),currentCase);
							ccModel.buildYearMapAndList();
							ccView.caseTableView.setItems(ccModel.caseList);
							ccView.yearComboBox.setItems(ccModel.yearList);
							ccView.messageLabel.setText( ccModel.caseList.size()+" cases.");
						}
					}catch(DataException de) {
						//de.printStackTrace();
					}
				}); 
				//clear Button in Add Case values to show in Case View
				mCV.clearButton.setOnAction((e)->{	//clear all textfields in modifyMenuItem stage 
					mCV.titleTextField.clear();
					mCV.caseTypeTextField.clear();
					mCV.caseNumberTextField.clear();
					mCV.caseNotesTextArea.clear();
					mCV.caseDatePicker.setValue(LocalDate.now());	    	
				});
				mCV.closeButton.setOnAction((e)->{ //close modifyMenuItem stage 
					mCV.stage.close();
				});
				break;
			case "Delete case": 		//if Delete Case menu Item is selected	
				DeleteCaseView dCV=new DeleteCaseView(mi.getText()); //creating instance of DeleteCaseView
				dCV.buildView().show();
				//parsing current values to show in Case View
				dCV.caseDatePicker.setValue(LocalDate.parse(currentCase.getCaseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				dCV.titleTextField.setText(currentCase.getCaseTitle());
				dCV.caseTypeTextField.setText(currentCase.getCaseType());
				dCV.caseNumberTextField.setText(currentCase.getCaseNumber());
				dCV.caseLinkTextField.setText(currentCase.getCaseLink());
				dCV.caseNotesTextArea.setText(currentCase.getCaseNotes());	
				dCV.updateButton.setOnAction((e)->{	//delete case record in deleteMenuItem stage 
					ccModel.caseList.remove(currentCase);
					ccModel.buildYearMapAndList();
					ccView.caseTableView.setItems(ccModel.caseList);
					ccView.yearComboBox.setItems(ccModel.yearList);
					ccView.messageLabel.setText( ccModel.caseList.size()+" cases");
				}); 
				dCV.clearButton.setOnAction((e)->{	//clear all textfields in deleteMenuItem stage 
					dCV.titleTextField.clear();
					dCV.caseTypeTextField.clear();
					dCV.caseNumberTextField.clear();
					dCV.caseNotesTextArea.clear();
					dCV.caseDatePicker.setValue(LocalDate.now());	    	
				});
				dCV.closeButton.setOnAction((e)->{ 	//close deleteMenuItem stage 
					dCV.stage.close();
				});
				break;
			default:
				break;
			}
		}

	}

}

