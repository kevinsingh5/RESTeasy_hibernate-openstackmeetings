package assign.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "output")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorHandler {
		
	String error;
	
	public void setError(String e) {
		this.error = e;
	}
	
	public String getError() {
		return this.error;
	}
	
}
