//package assign.resources;
//
//import static org.junit.Assert.*;
//
//import java.util.List;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathFactory;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//
//import assign.services.OpenStackMeetingsServiceImpl;
//
//
//public class TestOpenStackMeetingsResource
//{
//
//	private OpenStackMeetingsServiceImpl osmService;
//
//	@Before
//	public void initClient() {
//		osmService = new OpenStackMeetingsServiceImpl();
//	}
//
//	@Test // tests the function getAllProjects() from the resource class
//	public void testGetAllProjects() throws Exception {
//		System.out.println("*** Testing getAllProjects ***");
//
//		String my_url = "http://localhost:8080/assignment3/myeavesdrop/projects";
//		String expression = "projects/project";
//		String url = "http://eavesdrop.openstack.org/meetings";
//
//		NodeList projectsFromURL = xmlParser(my_url, expression);
//		List<String> projectsFromResource = osmService.getData(url, "");
//
//		// Asserts
//		System.out.println("    -> Comparing projects from eavesdrop website to output from getAllProjects()");
//		for(int i=0; i < projectsFromURL.getLength(); i++) {
//			Node n = projectsFromURL.item(i);
//			assertEquals(n.getTextContent(), projectsFromResource.get(i));
//		}
//		System.out.println("    -> Test succeeded!");
//		System.out.println();
//	}
//	
//	@Test // tests the function getMeeting() from the resource class
//	public void testGetMeeting1() throws Exception {
//		System.out.println("*** Testing getMeeting on solum_team_meeting ***");
//
//		String my_url = "http://localhost:8080/assignment3/myeavesdrop/projects/solum_team_meeting/meetings";
//		String expression = "meetings/year";
//		String url = "http://eavesdrop.openstack.org/meetings/solum_team_meeting/";
//
//		NodeList meetingsFromURL = xmlParser(my_url, expression);
//		List<String> meetingsFromResource = osmService.getData(url, "");
//
//		// Asserts
//		System.out.println("    -> Comparing meetings from eavesdrop website to output from getMeeting()");
//		for(int i=0; i < meetingsFromURL.getLength(); i++) {
//			Node n = meetingsFromURL.item(i);
//			assertEquals(n.getTextContent(), meetingsFromResource.get(i));
//		}
//		System.out.println("    -> Test succeeded!");
//		System.out.println();
//	}
//	
//	@Test // tests the function getMeeting() from the resource class
//	public void testGetMeeting2() throws Exception {
//		System.out.println("*** Testing getMeeting on 3rd_party_ci ***");
//
//		String my_url = "http://localhost:8080/assignment3/myeavesdrop/projects/3rd_party_ci/meetings";
//		String expression = "meetings/year";
//		String url = "http://eavesdrop.openstack.org/meetings/3rd_party_ci/";
//
//		NodeList meetingsFromURL = xmlParser(my_url, expression);
//		List<String> meetingsFromResource = osmService.getData(url, "");
//
//		// Asserts
//		System.out.println("    -> Comparing meetings from eavesdrop website to output from getMeeting()");
//		for(int i=0; i < meetingsFromURL.getLength(); i++) {
//			Node n = meetingsFromURL.item(i);
//			assertEquals(n.getTextContent(), meetingsFromResource.get(i));
//		}
//		System.out.println("    -> Test succeeded!");
//		System.out.println();
//	}
//
//	@Test // tests the error handling in the resource class for a project not found
//	public void testProjectNotFound() throws Exception {
//		System.out.println("*** Testing error-handling on non-existent-project ***");
//
//		String my_url = "http://localhost:8080/assignment3/myeavesdrop/projects/non-existent-project/meetings";
//		String expression = "output/error";
//
//		NodeList meetingsFromURL = xmlParser(my_url, expression);
//
//		// Asserts
//		System.out.println("    -> Comparing error from getMeeting() ErrorHandler to expected output");
//		for(int i=0; i < meetingsFromURL.getLength(); i++) {
//			Node n = meetingsFromURL.item(i);
//			assertEquals(n.getTextContent(), "Project non-existent-project does not exist");
//		}
//		System.out.println("    -> Test succeeded!");
//		System.out.println();
//	}	
//	
//	
//	// XML parser
//	public NodeList xmlParser(String url, String expression) throws Exception {
//		XPath xpath = XPathFactory.newInstance().newXPath();
//
//		InputSource inputSource = new InputSource(url);
//		NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
//
//		return nodes;
//	}
//
//}
