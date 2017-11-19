/*
 * @author Kevin Singh
 *   date: October 20, 2017
 */

package assign.resources;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import assign.services.OpenStackMeetingsService;

@ApplicationPath("/myeavesdrop")
public class OpenStackMeetingsApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	// default constructor
	public OpenStackMeetingsApplication() {		
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		classes.add(OpenStackMeetingsResource.class);
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
}
