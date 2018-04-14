//Sahaj Arora 100961220 Luke Daschko 100976007 Jennifer Franklin 100315764
package edu.carleton.comp4601.resources;

import java.util.HashSet;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import javax.ws.rs.core.Response;

import edu.carleton.comp4601.dao.Skills;
import edu.carleton.comp4601.utility.CrawlerController;

@Path("/")
public class Project {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String name;
	
	public Project() {
		name = "COMP4601 Project V1.0: Sahaj Arora, Luke Daschko and Jennifer Franklin";
	}
	
	@GET
	public String printName() {
		return name;
	}
	
	@GET
	@Path("skills")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSkillsList() {
		HashSet<String> skills = Skills.getInstance().getSkills();
		JSONObject object = null;
        Response response = null;
        try {
        	object =  new JSONObject();
        	int i=0;
        	for(String skill : skills) {
        		object.put("Skill"+i, skill);
        		i++;
        	}
        	response = Response.status(Status.OK).entity(object.toString()).build();
        } catch (Exception e) {
            System.out.println("error=" + e.getMessage());
        }
        return response;	
	}
	
	@GET
	@Path("query/{skill}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTopTenRelatedSkills(
			@PathParam("skill")	String	searchTerm) {
		JSONObject object = null;
        Response response = null;
        try {
        	object =  new JSONObject();
        	CrawlerController cc = new CrawlerController();
        	Map<String, Double> results = cc.getResults(searchTerm);
        	for(String skill : results.keySet()) {
        		object.put(skill, results.get(skill));
        	}
        	response = Response.status(Status.OK).entity(object.toString()).build();
        } catch (Exception e) {
            System.out.println("error=" + e.getMessage());
        }
        return response;			
	}

}
