/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;


public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView
	private Case mc;

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//create instance of CaseReadFactory() and call .createReader()
		CaseReadFactory crf=new CaseReadFactory();
		CaseReader cr = crf.createReader(filename);
		List<Case> caseListTemp =  cr.readCases();
		//pass case list from CaseReader and sort and load to case Map
		caseList =  FXCollections.observableArrayList(caseListTemp);
		Collections.sort(caseList);
		for(Case c:caseList) {
			caseMap.put(c.getCaseNumber(),c);
		}
	}
	
	//writeCases writes data to the filename given
	boolean writeCases(String filename){
		FileWriter file_write ;
		try {
			file_write = new FileWriter(filename);
			StringBuilder sc=new StringBuilder();	
			for (Case c:caseList) {
				sc.append(c.getCaseDate()+"\t"+c.getCaseTitle()+"\t"+c.getCaseType()+"\t"+c.getCaseNumber()+"\t"+c.getCaseLink()+"\t"+c.getCaseCategory()+"\t"+c.getCaseNotes()+"\n");
	        }
			file_write.write(sc.toString());
			file_write.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//find the unique list of years in case list
		List<String> uniqueYear = new ArrayList<>();
		for(Case c : caseList) {
			if(!uniqueYear.contains(c.getCaseDate().substring(0, 4)))
				uniqueYear.add(c.getCaseDate().substring(0, 4));
		}

		//loop through the unique Year list to find cases from each year and load to year Map
		for(String uy:uniqueYear) {
			Iterator<Case> iter= caseList.iterator();
			List<Case> cases = new ArrayList<>();
			while (iter.hasNext()) {
				Case cy=iter.next();
				if(cy.getCaseDate().substring(0, 4).contains(uy)) 
					cases.add(cy);
			}
			yearMap.put(uy,cases);
		}
		//create year list using key of yearMap
		for (String key : yearMap.keySet() ) {
			yearList.add(key);
		}
		Collections.sort(yearList);
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//Create a copy of case List
		List<Case> matchingCases = new ArrayList<Case>(caseList);

		//iterate through case list and filter out case records that don't match conditions
		Iterator<Case> iter= matchingCases.iterator();
		if(title!=null && !title.isEmpty()) {
			while (iter.hasNext()) {
				mc = iter.next();
				if(!mc.getCaseTitle().toLowerCase().contains(title.toLowerCase())) {
					iter.remove();
				}
			}
		}

		//updating iterator() based on filtered list
		iter= matchingCases.iterator();
		if(caseType!=null && !caseType.isEmpty()) {
			while (iter.hasNext()) {
				mc = iter.next();
				if(!mc.getCaseType().equalsIgnoreCase(caseType)) {
					iter.remove();
				}
			}
		}

		iter= matchingCases.iterator();
		if(year!=null && !year.isEmpty()) {
			while (iter.hasNext()) {
				mc = iter.next();
				if(!mc.getCaseDate().substring(0,4).equals(year)) {
					iter.remove();
				}
			}
		}

		iter= matchingCases.iterator();
		if(caseNumber!=null && !caseNumber.isEmpty()) {
			while (iter.hasNext()) {
				mc = iter.next();
				if(!mc.getCaseNumber().contains(caseNumber)) {
					iter.remove();
				}
			}
		}

		return matchingCases;
	}	

}
