package de.thoffbauer.helloworld.server;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;
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
	public Saying sayHello(@QueryParam("name") Optional<String> name) {
		String address = "";
		if(name.isPresent()) {
			User user = dao.findByName(name.get());
			if(user != null) {
				address = user.getAddress() + " ";
			}
		}
		final String value = String.format(template, address + name.or(defaultName));
		return new Saying(counter.incrementAndGet(), value);
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Timed
	public Saying register(@FormParam("name") @NotEmpty String name, @FormParam("address") @NotEmpty String address) {
		User user = new User(name, address);
		String found = dao.findAddressByName(name);
		if(found != null && !found.isEmpty()) {
			dao.update(user);
			return new Saying(counter.incrementAndGet(), "Updated");
		} else {
			dao.insert(user);
			return new Saying(counter.incrementAndGet(), "Registered");
		}
	}
}
