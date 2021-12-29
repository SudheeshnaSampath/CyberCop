/*Name: Sudheeshna Sampath; Andrew ID: sudheess */
package hw3;

public class CaseReadFactory {
	
	public CaseReader createReader(String filename){
		//extract extension from filename and pass CSVCaseReader() or TSVCaseReader() based on the substring
		if(filename.substring(filename.length()-3, filename.length()).equalsIgnoreCase("csv")) {
			return new CSVCaseReader(filename);
		}
		else if (filename.substring(filename.length()-3, filename.length()).equalsIgnoreCase("tsv")){
			return new TSVCaseReader(filename);
		}else {
			return null;
		}
	}
}
