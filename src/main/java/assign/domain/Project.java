package assign.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

	


@Entity
@Table (name = "projects")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

	private String name;
	private String description;
	@XmlAttribute
	private int project_id;
	@XmlElementWrapper(name="meetings")
	private Set<Meeting> meeting;
		
	// default constructor for Hibernate
	public Project() {
		
	}
	
	public Project(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public void setProjectID(int id) {
		this.project_id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getProjectID() {
		return project_id;
	}
	
    //@OneToMany(mappedBy = "project")
    @Cascade({CascadeType.DELETE})
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")//, cascade = CascadeType.ALL)
    public Set<Meeting> getMeetings() {
    	return this.meeting;
    }
    
    public void setMeetings(Set<Meeting> meetings) {
    	this.meeting = meetings;
    }
	
}
