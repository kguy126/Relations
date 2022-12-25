package ca.yorku.eecs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	
        assertTrue( true );
    }
    

    public void testaddEmployeePass() throws JSONException, IOException {
    	neo4j a = neo4j.getInstance();
    	a.addEmployee("pn2024", "Harvey Specter");
    	String id = a.getemployeeId();
    	assertEquals(id,"pn2024");
    }
    
    
    public void testaddProjectPass() {
    	neo4j b = neo4j.getInstance();
    	b.addProject("pn7003", "Author Social");
    	String id = b.getprojectId();
    	assertEquals(id,"pn7003");
    }
    
    public void testaddRelationshipPass() {
    	neo4j newRelationship = neo4j.getInstance();
    	newRelationship.addRelationship("pn2024", "pn7003");
    	String employeeId = newRelationship.getemployeeId();
    	String projectId = newRelationship.getprojectId();
    	assertEquals(employeeId,"pn2024");
    	assertEquals(projectId,"pn7003");
    }
    
  

}