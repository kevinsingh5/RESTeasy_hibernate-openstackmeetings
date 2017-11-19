package assign.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewProject {

	private String name;
	private String description;
	private int project_id;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;		
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
		
	public String getDescription() {
		return this.description;
	}
	
	public void setProjectID(int id) {
		this.project_id = id;
	}
	
	public int getProjectID() {
		return this.project_id;
	}

}
