package de.thoffbauer.helloworld.server;

public class User {
	
	private String name;
	private String address;
	
	public User(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

}
