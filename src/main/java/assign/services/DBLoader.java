package assign.services;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import assign.domain.Project;
import assign.domain.Meeting;

import java.util.logging.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;



public class DBLoader {
	
	private SessionFactory sessionFactory;
	Logger logger;
	
	public DBLoader() {
//		DEPRECATED SOLUTION		
//		// A SessionFactory is set up once for an application
//        sessionFactory = new Configuration()
//                .configure() // configures settings from hibernate.cfg.xml
//                .buildSessionFactory(); // --> DEPRECATED
//        
//        logger = Logger.getLogger("DBLoader");
        
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
                    configuration.getProperties()). buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        
	}
	
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData.");
	}
	
	public int addProject(String name, String description) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int pid = -1;
		try {
			tx = session.beginTransaction();
			Project newProject = new Project(name, description); 
			session.save(newProject);
		    pid = newProject.getProjectID();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return pid;
	}

	
	public int addMeeting(String name, int year) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int mid = -1;
		try {
			tx = session.beginTransaction();
			Meeting newMeeting = new Meeting(name, year); 
			session.save(newMeeting);
		    mid = newMeeting.getMeetingID();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return mid;
	}
	
	public int addMeetingForProject(String name, int year, int projectId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int meetingId = -1;
		try {
			tx = session.beginTransaction();
			Meeting meeting = new Meeting(name, year);
			Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("projectID", projectId));
			List<Project> projectList = criteria.list();
			meeting.setProject(projectList.get(0));
			session.save(meeting);
			meetingId = meeting.getMeetingID();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		}
		finally {
			session.close();
		}
		return meetingId;
	}

	public int updateMeeting(String name, int year, int pid, int mid) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int meetingId = -1;
		
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Meeting.class).add(Restrictions.eq("meetingID", mid));
			List<Meeting> meetingList = criteria.list();
			Meeting meeting = meetingList.get(0);
			if(meeting.getProject().getProjectID() != pid) // check the meeting exists in the project
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			meeting.setName(name);
			meeting.setYear(year);
			session.save(meeting);
			meetingId = meeting.getMeetingID();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		}
		finally {
			session.close();
		}
		return meetingId;
	}
	
	
	public void deleteProject(int pid) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Project p where p.projectID = :projectID";		
				
		Project p = (Project)session.createQuery(query).setParameter("projectID", pid).list().get(0);
		
        session.delete(p);

        session.getTransaction().commit();
        session.close();
	}
	
	public Project getProject(int pid) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("projectID", pid));
		
		List<Project> projects = criteria.list();
		
		if (projects.size() > 0) {
			session.close();
			return projects.get(0);	
		} else {
			session.close();
			return null;
		}
	}
	
}
