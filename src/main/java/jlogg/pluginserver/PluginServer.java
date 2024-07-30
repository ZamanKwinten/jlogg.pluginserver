package jlogg.pluginserver;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.servlet.ServletContainer;

public class PluginServer {

	public static void start(List<String> packages) throws Exception {
		var server = new Server(VMargs.portNumber);

		var contextHandler = new ServletContextHandler("/", ServletContextHandler.SESSIONS);
		server.setHandler(contextHandler);

		var jersey = contextHandler.addServlet(ServletContainer.class, "/*");
		jersey.setInitOrder(0);

		jersey.setInitParameter("jersey.config.server.provider.packages",
				packages.stream().collect(Collectors.joining(";")));

		server.start();
	}
}
