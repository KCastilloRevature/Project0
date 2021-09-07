package models;

import java.util.Objects;

public class Client {
	private String name;
	private int clientID;
	
	public Client() {
		super();
	}
	
	public Client(String name, int clientID) {
		super();
		this.name = name;
		this.clientID = clientID;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientID, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(clientID, other.clientID) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "client [name=" + name + ", clientID=" + clientID + "]";
	}
	
}
