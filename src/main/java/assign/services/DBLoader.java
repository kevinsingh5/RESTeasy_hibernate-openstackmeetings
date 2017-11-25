package assign.services;

import java.util.Date;
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

public class DBLoader {
	
	private SessionFactory sessionFactory;
	Logger logger;
	
	public DBLoader() {
//		// A SessionFactory is set up once for an application
//        sessionFactory = new Configuration()
//                .configure() // configures settings from hibernate.cfg.xml
//                .buildSessionFactory();
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
	
	public int addMeetingForProject(String name, int year, Long projectId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int meetingId = -1;
		try {
			tx = session.beginTransaction();
			Meeting meeting = new Meeting(name, year);
			Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("project_id", projectId));
			List<Project> projectList = criteria.list();
			meeting.setProject(projectList.get(0));
			session.save(meeting);
			meetingId = meeting.getMeetingID();
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
		return meetingId;
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
	
	/*
	public Long addAssignmentAndCourse(String title, String courseTitle) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long assignmentId = null;
		try {
			tx = session.beginTransaction();
			Assignment newAssignment = new Assignment( title, new Date() );
			UTCourse course = new UTCourse(courseTitle);
			newAssignment.setUtcourse(course);
			session.save(course);
			session.save(newAssignment);
		    assignmentId = newAssignment.getId();
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
		return assignmentId;
	}
	
	public Long addAssignmentsToCourse(List<String> assignments, String courseTitle) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long courseId = null;
		try {
			tx = session.beginTransaction();
			UTCourse course = new UTCourse(courseTitle);
			session.save(course);
			courseId = course.getId();
			for(String a : assignments) {
				Assignment newAssignment = new Assignment( a, new Date() );
				newAssignment.setUtcourse(course);
				session.save(newAssignment);
			}
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
		return courseId;
	}
	
	public List<Assignment> getAssignmentsForACourse(Long courseId) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Assignment where course=" + courseId; // BAD PRACTICE
		List<Assignment> assignments = session.createQuery(query).list();
		session.close();
		return assignments;
	}
	
	public List<Object[]> getAssignmentsForACourse(String courseName) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String query = "from Assignment a join a.course c where c.courseName = :cname";		
				
		List<Object[]> assignments = session.createQuery(query).setParameter("cname", courseName).list();
		session.close();
		
		return assignments;
	}
	
	public Assignment getAssignment(String title) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Assignment.class).
        		add(Restrictions.eq("title", title));
		
		List<Assignment> assignments = criteria.list();
		
		session.close();
		
		if (assignments.size() > 0) {
			return assignments.get(0);			
		} else {
			return null;
		}
	}
	
////////////////////

	public Homework getHomework_using_get(Long homeworkId) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Homework h = (Homework)session.get(Homework.class, homeworkId);
		session.close();
		return h;		
	}

	public Homework getHomework_using_load(Long homeworkId) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Homework h = null;
		try {
		h = (Homework)session.load(Homework.class, homeworkId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			session.close();
		}
		return h;		
	}
	
	public Homework getHomework_using_criteria(Long homeworkId) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Homework.class).
        		add(Restrictions.eq("id", homeworkId));
		List<Homework> homeworks = criteria.list();
		if (homeworks.size() > 0) {
			session.close();
			return homeworks.get(0);	
		} else {
			session.close();
			return null;
		}
	}
	

	public void deleteAssignment(String title) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Assignment a where a.title = :title";		
				
		Assignment a = (Assignment)session.createQuery(query).setParameter("title", title).list().get(0);
		
        session.delete(a);

        session.getTransaction().commit();
        session.close();
	}
	
	public void deleteCourse(String courseName) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from UTCourse c where c.courseName = :courseName";		
				
		UTCourse c = (UTCourse)session.createQuery(query).setParameter("courseName", courseName).list().get(0);
		
        session.delete(c);

        session.getTransaction().commit();
        session.close();
	}
	
	
	public Assignment getAssignment(Long assignmentId) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Assignment.class).
        		add(Restrictions.eq("id", assignmentId));
		
		List<Assignment> assignments = criteria.list();
		
		session.close();
		
		return assignments.get(0);		
	}*/
}
