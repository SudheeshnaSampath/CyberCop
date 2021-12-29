/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCaseView extends CaseView{

	AddCaseView(String header) {
		super(header);
	}

	@Override
	Stage buildView() {
		//set new Scene for Add Menu Item and return Stage
		updateButton.setText("Add Case");
		
		caseDatePicker.setValue(LocalDate.now());
	
		Scene s=new Scene(updateCaseGridPane);
		stage.setScene(s);
		
		return stage;
	}

}
