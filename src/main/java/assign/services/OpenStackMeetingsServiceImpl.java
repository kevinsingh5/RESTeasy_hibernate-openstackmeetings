package assign.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.NewProject;
import assign.domain.Project;


public class OpenStackMeetingsServiceImpl implements OpenStackMeetingsService {	
	
	String dbURL = "";
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;
	
	public OpenStackMeetingsServiceImpl(String dburl, String username, String password) {
		this.dbURL = dburl;
		this.dbUsername = username;
		this.dbPassword = password;
		
		ds = setupDataSource();
	}
	
	// sets up the required fields for DataSource
	private DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
	}

	public NewProject addProject (NewProject proj) throws Exception {
		// establish connection to the database specified by DataSource ds
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO projects(name, description) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS); // gets keys back
		
		stmt.setString(1, proj.getName());
		stmt.setString(2, proj.getDescription());
		
		int affectedRows = stmt.executeUpdate(); // executeUpdate() doesn't return any values

        if (affectedRows == 0) {
            throw new SQLException("Creating project failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	proj.setProjectID(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating project failed, no ID obtained.");
        }
        
        // Close the connection
        conn.close();
		return proj;
	}
	
	
	public NewProject updateProject(NewProject proj) throws Exception {
		// establish connection to the database specified by DataSource ds
		Connection conn = ds.getConnection();
		
		String update = "UPDATE projects SET description=? WHERE project_id=?";
		PreparedStatement stmt = conn.prepareStatement(update,
                Statement.RETURN_GENERATED_KEYS); // gets keys back
		
		stmt.setString(1, proj.getDescription());
		stmt.setInt(2, proj.getProjectID());

		try {
			int affectedRows = stmt.executeUpdate(); // executeUpdate() doesn't return any values
	        if (affectedRows == 0) {
	            throw new SQLException("project_id not found. Updating project failed, no rows affected.");
	        }
		} catch (SQLException e) {
			throw new WebApplicationException(e, Response.Status.NOT_FOUND);
		}
		
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        while (generatedKeys.next()) {
        	proj.setProjectID(generatedKeys.getInt(1));
        	proj.setDescription(generatedKeys.getString("description"));
        }
        
        // Close the connection
        conn.close();
		return proj;
	}
	
	public Project getProject(Project proj) throws Exception {
		// establish connection to the database specified by DataSource ds
		Connection conn = ds.getConnection();
		
		String update = "SELECT * FROM projects WHERE project_id=?";
		PreparedStatement stmt = conn.prepareStatement(update,
                Statement.RETURN_GENERATED_KEYS); // gets keys back
		
		stmt.setInt(1, proj.getProjectID());

		ResultSet result;
		try {
			result = stmt.executeQuery(); // executeUpdate() doesn't return any values
		} catch (SQLException e) {
			throw new WebApplicationException(e, Response.Status.NOT_FOUND);
		}
		
        //ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (result.next()) {
        	proj.setName(result.getString("name"));
        	proj.setProjectID(result.getInt("project_id"));
        	proj.setDescription(result.getString("description"));
        } else {
        	throw new WebApplicationException();
        }
        
        // Close the connection
        conn.close();
		return proj;
	}

	public Project deleteProject(Project proj) throws Exception {
		// establish connection to the database specified by DataSource ds
		Connection conn = ds.getConnection();
		
		String update = "DELETE FROM projects WHERE project_id=?";
		PreparedStatement stmt = conn.prepareStatement(update,
                Statement.RETURN_GENERATED_KEYS); // gets keys back
		
		stmt.setInt(1, proj.getProjectID());

		try {
			int affectedRows = stmt.executeUpdate(); // executeUpdate() doesn't return any values
	        if (affectedRows == 0) {
	            throw new SQLException("project_id not found. Deleting project failed, no rows affected.");
	        }
		} catch (SQLException e) {
			throw new WebApplicationException(e, Response.Status.NOT_FOUND);
		}
		
        
        // Close the connection
        conn.close();
		return proj;
	}
	
	
}
