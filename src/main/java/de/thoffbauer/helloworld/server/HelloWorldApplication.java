package de.thoffbauer.helloworld.server;

import org.skife.jdbi.v2.DBI;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
	
	@Override
	public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
		configuration.getDataSourceFactory().setLogValidationErrors(true);
		
		final DBIFactory factory = new DBIFactory();
		final DBI dbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
		
		final HelloWorldResource resource = new HelloWorldResource(
				configuration.getTemplate(), 
				configuration.getDefaultName(),
				dbi);
		
		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);
		
		environment.jersey().register(resource);
	}
	
	@Override
	public String getName() {
		return "hello-world";
	}
	
	public static void main(String[] args) throws Exception {
		new HelloWorldApplication().run(args);
	}

}
