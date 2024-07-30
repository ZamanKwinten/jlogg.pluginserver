package jlogg.pluginserver.servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import jlogg.plugin.loader.PluginManifestData;
import jlogg.plugin.loader.PluginManifestReader;
import jlogg.plugin.loader.PluginServerEndPoints;
import jlogg.plugin.loader.PluginServerLatestResponse;
import jlogg.pluginserver.jaxrs.JSONResponse;

public abstract class APluginServlet {

	/**
	 * Get the directory in which the plugins will be places
	 * 
	 * @return
	 */
	protected abstract File getRootDir();

	@GET
	@Path(PluginServerEndPoints.LATEST)
	@Produces(MediaType.APPLICATION_JSON)
	public Response latest() {
		var files = getRootDir().listFiles((dir, name) -> name.endsWith(".jar"));
		if (files == null) {
			return Response.ok().build();
		}

		File jar = Arrays.stream(files).sorted((a, b) -> (int) (b.lastModified() - a.lastModified())).findFirst().get();
		PluginManifestData data = PluginManifestReader.getManifestDataFromJar(jar);

		return JSONResponse.ok(new PluginServerLatestResponse(data, jar.getName()));
	}

	@GET
	@Path(PluginServerEndPoints.DOWNLOAD + "/{filename}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public StreamingOutput getBytes(@PathParam("filename") String filename) {
		File file = new File(getRootDir(), filename);
		if (!file.exists()) {
			throw new RuntimeException("File " + filename + " does not exist!");
		}

		return new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				Files.copy(file.toPath(), output);
			}
		};
	}
}
