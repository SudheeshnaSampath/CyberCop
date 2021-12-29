/*Name: Sudheeshna Sampath; Andrew ID: sudheess */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{

	private StringProperty caseDate=new SimpleStringProperty();
	private StringProperty caseTitle=new SimpleStringProperty();
	private StringProperty caseType=new SimpleStringProperty();
	private StringProperty caseNumber=new SimpleStringProperty();
	private StringProperty caseLink=new SimpleStringProperty();
	private StringProperty caseCategory=new SimpleStringProperty();
	private StringProperty caseNotes=new SimpleStringProperty();

	public Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory,
			String caseNotes) {
		// TODO Auto-generated constructor stub
		this.caseDate.set(caseDate);
		this.caseTitle.set(caseTitle);
		this.caseType.set(caseType);
		this.caseNumber.set(caseNumber);
		this.caseLink.set(caseLink);
		this.caseCategory.set(caseCategory);
		this.caseNotes.set(caseNotes);
		
	}

	public String getCaseDate() {
		return this.caseDate.get();
	}
	public void setCaseDate(String date) {
		caseDate.set(date);
	}
	public StringProperty caseDateProperty() {
		return this.caseDate;
	}
	
	public String getCaseType() {
		return this.caseType.get();
	}
	public void setCaseType(String type) {
		caseType.set(type);
	}
	public StringProperty caseTypeProperty() {
		return this.caseType;
	}

	public String getCaseTitle() {
		return this.caseTitle.get();
	}
	public void setCaseTitle(String title) {
		caseTitle.set(title);
	}
	public StringProperty caseTitleProperty() {
		return this.caseTitle;
	}
	
	public String getCaseNumber() {
		return this.caseNumber.get();
	}
	public void setCaseNumber(String number) {
		caseNumber.set(number);
	}
	public StringProperty caseNumberProperty() {
		return this.caseNumber;
	}
	
	public String getCaseLink() {
		if(this.caseLink.get().equals("")) {
			return " ";
		}
		return this.caseLink.get();
	}
	public void setCaseLink(String link) {
		caseLink.set(link);
	}
	public StringProperty caseLinkProperty() {
		return this.caseLink;
	}
	
	public String getCaseCategory() {
		if(this.caseCategory.get().equals("")) {
			return " ";
		}
		return this.caseCategory.get();
	}
	public void setCaseCategory(String category) {
		caseCategory.set(category);
	}
	public StringProperty caseCategoryProperty() {
		return this.caseCategory;
	}
	
	public String getCaseNotes() {
		if(this.caseNotes.get().equals("")) {
			return " ";
		}
		return this.caseNotes.get();
	}
	public void setCaseNotes(String notes) {
		caseNotes.set(notes);
	}
	public StringProperty caseNotesProperty() {
		return this.caseNotes;
	}
	
	@Override
	public int compareTo(Case o) {
		// TODO Auto-generated method stub
		return this.caseDate.get().compareTo(o.caseDate.get());
	}
	
	public String toString() {
		return this.caseNumber.get();
		
	}

}
