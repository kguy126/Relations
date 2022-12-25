package ca.yorku.eecs;
import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.naming.spi.DirStateFactory.Result;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.types.Path;

public class neo4j {
	private Driver driver;
	private String uriDb;
	private static neo4j instance;
	private String employeeId, employeeName, projectId, projectName;
	private boolean relationship;
	private ArrayList<String> employeeList;
  //Database neo4j constructor
  public neo4j(){
	  uriDb = "bolt://localhost:7687"; 
		Config config = Config.builder().withoutEncryption().build();
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","123456"), config);  
  }
  
  public static neo4j getInstance(){
		if(instance == null) instance = new neo4j();
		return instance;	
	}
  
  public void addEmployee(String id, String name) {
		this.employeeId = id;
		this.employeeName = name;
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (e:Employee {name: $x, id: $y})",parameters("x", employeeName, "y", employeeId)));
			session.close();
		}
	}
  
  public void addProject(String id, String name) {
		this.projectId = id;
		this.projectName = name;
		
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (p:Project {name: $x, id: $y})",parameters("x", projectName, "y", projectId)));
			session.close();
		}	
	}
  
	public void addRelationship(String employeeId, String projectId) {
		this.employeeId = employeeId;
		this.projectId = projectId;
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MATCH (e:Employee),(p:Project) WHERE p.id=$x AND e.id=$y CREATE (e)-[:WORKED_ON]->(p)",parameters("x", this.projectId, "y", this.employeeId)));
			session.close();
		}
	}
  
	public JSONObject getEmployee(String employeeId) {
		this.employeeId=employeeId;
		JSONObject response= new JSONObject();
		
		try (Session session = driver.session()){
			try(Transaction tx = session.beginTransaction()){
				org.neo4j.driver.Result result = tx.run("match (e:Employee{id:$x}) RETURN (e.name) as e ",parameters( "x", this.employeeId ));
				org.neo4j.driver.Result resultArray = tx.run("match (p:Project)--(:Employee{id:$x}) return p.id",parameters("x",this.employeeId));
				
				if(result.hasNext()) {
					String name = result.next().get(0).asString();
					try {
						response.put("name", name);
						
						response.put("employeeId", this.employeeId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			      ArrayList<String> projectList = new ArrayList<>();
					while(resultArray.hasNext()) {
						String id = resultArray.next().get(0).asString();
						projectList.add(id);	
					}				
					try {
						response.put("projects", projectList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
		}
		return response;
	}
	
	public JSONObject getProject(String projectId) {
		this.projectId=projectId;
		JSONObject response= new JSONObject();
		
		try (Session session = driver.session()){
			try(Transaction tx = session.beginTransaction()){
				org.neo4j.driver.Result result =  tx.run("match (p:Project{id:$x}) RETURN (p.name) as p",parameters("x", this.projectId));
				org.neo4j.driver.Result resultArray = tx.run("match (e:Employee)--(p:Project{id:$x}) return e.id",parameters("x",this.projectId));
				
				if(result.hasNext()) {
					String name = result.next().get(0).asString();
					try {
						response.put("name", name);
						response.put("projectId", this.projectId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			      ArrayList<String> employeeList = new ArrayList<>();
					while(resultArray.hasNext()) {
						String id = resultArray.next().get(0).asString();
						employeeList.add(id);	
					}				
					try {
						response.put("employees", employeeList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
		}
		return response;
	}
	

	public JSONObject relation(String employeeId, String projectId) {
		this.employeeId=employeeId;
		this.projectId= projectId;
		
		try (Session session = driver.session()){
			try(Transaction tx = session.beginTransaction()){
				org.neo4j.driver.Result result = tx.run("match (p:Projecr{id:$x})--(e:Employee{id:$y}) return (e) ",parameters("x", this.projectId,"y", this.employeeId));
				relationship= result.hasNext();
				}
		}
		
		JSONObject response= new JSONObject();
		try 
		{
			response.put("employeeId", this.employeeId);
			response.put("projectId",this.projectId);	
			response.put("hasRelationship", relationship);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	

  
  
	
//ACCESSORS AND MUTATORS
  public String getemployeeId() {
	  return employeeId;
  }
  public String getemployeename() {
	  return employeeName;
  }
  
  public String getprojectId() {
	  return projectId;
  }
  
  public String getprojectname() {
	  return projectName;
  }
  
  public boolean hasrelation() {
	  return relationship;
  }
  
  
  public ArrayList<String> getemployeelist() {
		return employeeList;
	}
  
  public void setemployeeId(String employeeId) {
	  this.employeeId= employeeId;
  }
  public void setemployeename(String employeename) {
	  this.employeeName= employeename;
  }
  public void setprojectId(String projectId) {
	  this.projectId= projectId;
  }
  
  public void setprojectname(String projectname) {
	  this.projectName= projectname;
  }
  
  public void sethasrelation(boolean relation) {
	  this.relationship= relation;
  }
  

  public void setEmployeeList(ArrayList<String> employeeList) {
		this.employeeList = employeeList;
	}
}
