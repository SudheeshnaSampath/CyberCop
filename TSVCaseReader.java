/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSVCaseReader extends CaseReader {

	TSVCaseReader(String filename) {
		super(filename);
	}

	@Override
	List<Case> readCases(){

		int countEmpty = 0;
		String[] caseListtemp= null;
		List<Case> caselist = new ArrayList<>();
		//Exception handling
		try {
			//read values from file to array 
			StringBuilder cases = new StringBuilder();
			Scanner input = new Scanner(new File(filename));
			while (input.hasNextLine()) {
				cases.append(input.nextLine() + "\n");
			}
			caseListtemp=cases.toString().split("\n");
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//loop through array, split by tab and find count of cases with missing date,title,type or case number
		for(int i=0;i<caseListtemp.length;i++) {
			String[] caseListArr=caseListtemp[i].split("\t");
			String caseDate = caseListArr[0].trim();
			String caseTitle = caseListArr[1].trim();
			String caseType = caseListArr[2].trim();
			String caseNumber = caseListArr[3].trim();
			String caseLink = caseListArr[4].trim();
			String caseCategory = caseListArr[5].trim();
			String caseNotes = caseListArr[6].trim();
			if(caseDate.isBlank() || caseTitle.isBlank() || caseType.isBlank() || caseNumber.isBlank()) {
				countEmpty++;
			}else {				
				Case case1=new Case(caseDate,caseTitle,caseType,caseNumber,caseLink,caseCategory,caseNotes);
				caselist.add(case1);
			}
		}
		//check if there are missing values and throw data exception accordingly
		try {
			for(int i=0;i<caseListtemp.length;i++) {
				String[] caseListArr=caseListtemp[i].split("\t");
				//check if date, title, type or case  number is blank. if so, throw data exception.
				if(caseListArr[0].trim().isBlank() | caseListArr[1].trim().isBlank() | caseListArr[2].trim().isBlank() | caseListArr[3].trim().isBlank()) {
					throw new DataException(countEmpty +" cases rejected. \n The file must have cases with \n tab seperated date, title, type and case number!");
				}
			}
		}catch(DataException e) {
			//do something
		}

		return caselist;
	}

}


