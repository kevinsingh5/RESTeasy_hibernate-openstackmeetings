package assign.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "meetings")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Meeting {
	
	private String name;
	private int year;
	@XmlAttribute
	private int meeting_id;
	@XmlTransient
	private Project project;

	// default constructor for Hibernate
	public Meeting() {
		
	}
	
	public Meeting(String name, int year) {
		this.name = name;
		this.year = year;
	}
	
	public Meeting(String name, int year, int id) {
		this.name = name;
		this.year = year;
		this.meeting_id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getMeetingID(){
		return meeting_id;
	}
	
	public void setMeetingID(int id) {
		this.meeting_id = id;
	}
	
    @ManyToOne
    @JoinColumn(name="project_id")
    public Project getProject() {
    	return this.project;
    }
    
    public void setProject(Project proj) {
    	this.project = proj;
    }

}
