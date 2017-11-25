package assign.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.domain.ErrorHandler;
import assign.domain.Meeting;
import assign.domain.Meetings;
import assign.domain.NewProject;
import assign.domain.Project;
import assign.domain.Projects;
import assign.services.DBLoader;
import assign.services.OpenStackMeetingsService;
import assign.services.OpenStackMeetingsServiceImpl;

@Path("/projects")
public class OpenStackMeetingsResource {
	
	// Placeholder for OpenStackMeetings service;
	DBLoader dbloader = new DBLoader();
	OpenStackMeetingsService osmService;
	String link = "http://eavesdrop.openstack.org/meetings";
	String password;
	String username;
	String dburl;	
	String dbhost, dbname;
	
	
	// constructor gets and assigns parameters form servlet context
	public OpenStackMeetingsResource(@Context ServletContext servletContext) {
		dbhost = servletContext.getInitParameter("DBHOST");
		dbname = servletContext.getInitParameter("DBNAME");
		dburl = "jdbc:mysql://" + dbhost + ":3306/" + dbname;
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.osmService = new OpenStackMeetingsServiceImpl(dburl, username, password);		
	}

	// default empty constructor
//	public OpenStackMeetingsResource() {
//		this.osmService = new OpenStackMeetingsServiceImpl();
//	}
	
	// Use this for unit testing
	protected void setOpenStackMeetingsService(OpenStackMeetingsService osmService) {
		this.osmService = osmService;
	}
	
	// Default landing page for /projects - shows all projects
	@GET
	@Path("")
	@Produces("application/xml")
	public Projects getAllProjects() throws Exception {
		//String link = "http://eavesdrop.openstack.org/meetings";
		//String value = "";
		Projects projects = new Projects();
		//List<String> projectList = osmService.getData(link, value);
		//projects.setProjects(projectList);
		
		return projects;    
	}	
	
	
	@POST
	@Consumes("application/xml")
	public Response createProject(InputStream is) throws Exception {
		int projectID = readNewProject(is);
		//newProject = this.osmService.addProject(newProject);
		//int pid = dbloader.addProject(name, description)
		//return Response.created(URI.create("/projects/" + project.getProjectID())).build();
		return Response.created(URI.create("/projects/" + projectID)).build();
	}

	@PUT
	@Path("/{project_id}")
	@Consumes("application/xml")
	public Response updateProject(InputStream is, @PathParam("project_id") int pid) throws Exception {
		if(pid <= 0)
			throw new WebApplicationException();
		
		NewProject newProject = readUpdatedProject(is, pid);
		newProject = this.osmService.updateProject(newProject);
		return Response.noContent().build();
	}
	
	@GET
	@Path("/{project_id}")
	@Produces("application/xml")
	public Project getProject(@PathParam("project_id") int pid) {
		Project project;
		try {
			project = dbloader.getProject(pid);
			if(project.equals(null))
				throw new Exception();
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.NOT_FOUND);
		}
		return project;
	}
	
	@DELETE
	@Path("/{project_id}")
	@Produces("application/xml")
	public Response deleteProject(@PathParam("project_id") int pid) {
		Project project = readDeleteProject(pid);
		try {
			project = osmService.deleteProject(project);
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.NOT_FOUND);
		}
		return Response.ok().build();
	}
	
	
	protected int readNewProject(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Project project = new Project();
			NodeList nodes = root.getChildNodes();
			
			String name = null;
			String description = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					name = element.getTextContent();
					if(name.equals(""))
						throw new WebApplicationException();
					//project.setName(name);
				}
				else if (element.getTagName().equals("description")) {
					description = element.getTextContent();
					if(description.equals(""))
						throw new WebApplicationException();
					//project.setDescription(description);
				}
			}
			int pid = dbloader.addProject(name, description);
			return pid;
		}
		catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
   }
	

	protected NewProject readUpdatedProject(InputStream is, int pid) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			NewProject project = new NewProject();
			NodeList nodes = root.getChildNodes();
			
			
			int proj_id = pid;
			project.setProjectID(proj_id);
			String name;
			String description;
	
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					name = element.getTextContent();
					if(name.equals(""))
						throw new WebApplicationException();
					project.setName(name);
				}
				else if (element.getTagName().equals("description")) {
					description = element.getTextContent();
					if(description.equals(""))
						throw new WebApplicationException();
					project.setDescription(description);
				}
			}
			return project;
		}
		catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
   }
	
	protected Project readGetProject(int pid) {
		Project project = new Project();
		project.setProjectID(pid);

		return project;
	}

	protected Project readDeleteProject(int pid) {
		Project project = new Project();
		project.setProjectID(pid);

		return project;
	}
		
}