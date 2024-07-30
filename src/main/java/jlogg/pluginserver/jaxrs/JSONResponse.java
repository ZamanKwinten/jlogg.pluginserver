package jlogg.pluginserver.jaxrs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.core.Response;

public class JSONResponse {
	private JSONResponse() {

	}

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Response ok(Object o) {
		return Response.ok(gson.toJson(o)).build();
	}
}
