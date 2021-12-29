/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteCaseView extends CaseView{

	DeleteCaseView(String header) {
		super(header);
	}

	@Override
	Stage buildView() {
		//set new Scene for Delete Menu Item and return its Stage
		updateButton.setText("Delete Case");
		Scene s=new Scene(updateCaseGridPane);
		stage.setScene(s);
		return stage;
	}

}
