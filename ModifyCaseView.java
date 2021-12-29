/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModifyCaseView extends CaseView {

	ModifyCaseView(String header) {
		super(header);
	}

	@Override
	Stage buildView() {
		//set new Scene for Modify Menu Item and return its Stage
		updateButton.setText("Modify Case");
		Scene s=new Scene(updateCaseGridPane);
		stage.setScene(s);
		return stage;
	}

}
