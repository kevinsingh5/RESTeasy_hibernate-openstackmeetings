package assign.services;

import assign.domain.NewProject;
import assign.domain.Project;


public interface OpenStackMeetingsService {

	public NewProject addProject(NewProject newProject) throws Exception;
		
	public NewProject updateProject(NewProject newProject) throws Exception;

	public Project getProject(Project project) throws Exception;

	public Project deleteProject(Project project) throws Exception;
	
}
