/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("serial")
public class DataException extends RuntimeException {

	DataException(String message){
		super(message);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(message);
		alert.showAndWait();
	}
}
