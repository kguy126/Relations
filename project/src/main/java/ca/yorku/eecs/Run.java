package ca.yorku.eecs;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Run implements HttpHandler{
	neo4j db = neo4j.getInstance();
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub
		try {
            if (exchange.getRequestMethod().equals("GET")) {
                handleGet(exchange);
                System.out.println("get");
            }else if(exchange.getRequestMethod().equals("PUT")){    	
            	 handlePut(exchange);
            	 System.out.println("put");
            } else
            	send(exchange, " Method not implemented\n", 501);
        } catch (Exception e) {
        	e.printStackTrace();
        	send(exchange, "Server error\n", 500);
        }
		
		
	}

	private void send(HttpExchange exchange, String string, int i) throws IOException {
		// TODO Auto-generated method stub
		exchange.sendResponseHeaders(i, string.length());
        OutputStream os = exchange.getResponseBody();
        os.write(string.getBytes());
        os.close();
		
	}

	private void handlePut(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub
		 URI uri = exchange.getRequestURI();String query = uri.getQuery();
	     if(uri.getPath().contains("addEmployee")) {
	    	 String name = null,employeeId = null;
	         JSONObject jsonBody = null;
	         String body = Utils.convert(exchange.getRequestBody());
	         
	         try {
					jsonBody = new JSONObject(body);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     	
	     	try {
	     			name = jsonBody.getString("name");
					 employeeId = jsonBody.getString("employeeId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         db.addEmployee(employeeId, name);
	    	 
	    	 String response = "Added" +name ;
	     	send(exchange,response,200);
	    	 
	     
	     }else if(uri.getPath().contains("addProject")){
	    	 String name = null,projectId = null;
	         JSONObject jsonBody = null;
	         String body = Utils.convert(exchange.getRequestBody());
	         
	         try {
					jsonBody = new JSONObject(body);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  	
	  	try {
					 name = jsonBody.getString("name");
					 projectId = jsonBody.getString("projectId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  	
	    	 db.addProject(projectId, name);
	    	 String response = "Added" +name ;
	    	 send(exchange,response,200);
	    	 
	       }else if(uri.getPath().contains("addRelationship")) {
	   	 
	   	    String employeeId = null,projectId  = null;
	        String body = Utils.convert(exchange.getRequestBody());
	        JSONObject jsonBody = null;
	        
	        try {
				jsonBody = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
				 employeeId = jsonBody.getString("employeeId");
				projectId = jsonBody.getString("projectId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    	db.addRelationship(employeeId,projectId);
	    	 
	   	   String response = "Added Relationship";
	   	   
	   	   send(exchange,response, 200);
	   }
 
	}
	

	private void handleGet(HttpExchange exchange) throws IOException{
		// TODO Auto-generated method stub
		  URI uri = exchange.getRequestURI();
	        
	 
	        if(uri.getPath().contains("getEmployee")) {
	        	String id = null;
	      
	        	JSONObject jsonBody = null;
	        	
	        	String body = Utils.convert(exchange.getRequestBody());
	        	
	        	try {
					jsonBody = new JSONObject(body);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	
	        	try {
					 id = jsonBody.getString("employeeId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	JSONObject response = db.getEmployee(id);
	        	
	        	send(exchange,response.toString(),200);
	        	
	        	
	        }else if(uri.getPath().contains("getMovie")) {
	        	
	        	String id = null;JSONObject jsonBody = null;
	        	
	        	String body = Utils.convert(exchange.getRequestBody());
	        	
	        	try {
					jsonBody = new JSONObject(body);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	try {
					 id = jsonBody.getString("projectId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	JSONObject str = db.getProject(id);

	        	send(exchange,str.toString(),200);
	        }
	        
	        else if(uri.getPath().contains("relation")) {
	        	
	        	String employeeId = null,projectId = null;
	        	JSONObject jsonBody = null;
	        
	        	String body = Utils.convert(exchange.getRequestBody());
	        	
	        	try {
					jsonBody = new JSONObject(body);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	        	try {
					projectId = jsonBody.getString("projectId");
					 employeeId = jsonBody.getString("employeeId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	JSONObject str = db.relation(employeeId,projectId);
	        	send(exchange,str.toString(),200);
	        	

	        }
	        
	}

}
