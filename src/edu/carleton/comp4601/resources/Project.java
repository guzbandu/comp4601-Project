//Sahaj Arora 100961220 Luke Daschko 100796007 Jennifer Franklin 100315764
package edu.carleton.comp4601.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

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

}
