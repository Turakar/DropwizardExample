package de.thoffbauer.helloworld.server;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.skife.jdbi.v2.DBI;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

	private final String template;
	private final String defaultName;
	private final AtomicLong counter;
	private final UserDAO dao;
	
	public HelloWorldResource(String template, String defaultName, DBI dbi) {
		this.template = template;
		this.defaultName = defaultName;
		this.counter = new AtomicLong();
		this.dao = dbi.onDemand(UserDAO.class);
	}
	
	@GET
	@Timed
	public Saying sayHello(@QueryParam("name") Optional<String> optionalName) {
		String address = "";
		if(optionalName.isPresent() && !optionalName.get().isEmpty()) {
			User user = dao.findByName(optionalName.get());
			if(user != null) {
				address = user.getAddress() + " ";
			}
		}
		String name = optionalName.or(defaultName);
		if(name.isEmpty()) {
			name = defaultName;
		}
		final String value = String.format(template, address + name);
		return new Saying(counter.incrementAndGet(), value);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Saying register(User user) {
		String found = dao.findAddressByName(user.getName());
		if(found != null && !found.isEmpty()) {
			dao.update(user);
			return new Saying(counter.incrementAndGet(), "Updated");
		} else {
			dao.insert(user);
			return new Saying(counter.incrementAndGet(), "Registered");
		}
	}
	
	@DELETE
	@Timed
	public Saying unregister(@QueryParam("name") String name) {
		String found = dao.findAddressByName(name);
		if(found == null || found.isEmpty()) {
			return new Saying(counter.incrementAndGet(), "Unknown user!");
		}
		dao.delete(name);
		return new Saying(counter.incrementAndGet(), "Unregistered");
	}
}
