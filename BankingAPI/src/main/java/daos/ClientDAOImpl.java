package daos;

import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Client;
//import utils.ConnectionUtil;

public class ClientDAOImpl implements ClientDAO {
	
	/*
	 * If I decide to revisit this project, I would probably make a base
	 * get method in a seperate class in the services package to avoid
	 * repetition
	 */
	
	Connection connection;
	
	public ClientDAOImpl(Connection conn) {
		connection = conn;
	}

	@Override
	public List<Client> getAllClients() {
		List<Client> clientList = new ArrayList<Client>();
		String sql = "SELECT * FROM clients";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			
            while(resultSet.next()) {
            	String name = resultSet.getString("name");
            	int clientID = resultSet.getInt("clientID");
            	
            	Client client = new Client(name, clientID);
            	clientList.add(client);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return clientList;
	}

	@Override
	public Client getClienttByID(int id) {
		Client resultClient = null;
		String sql = "SELECT * FROM clients "
				+ "WHERE ClientID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, id);
			ResultSet resultSet = prepStatement.executeQuery();
			
            while(resultSet.next()) {
            	String name = resultSet.getString("name");
            	int clientID = resultSet.getInt("ClientID");
            	
            	resultClient = new Client(name, clientID);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultClient;
	}
	
	@Override
	public Client getClientByName(String name) {
		Client resultClient = null;
		String sql = "SELECT * FROM clients "
				+ "WHERE Name = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setString(1, name);
			ResultSet resultSet = prepStatement.executeQuery();
			
            while(resultSet.next()) {
            	String clientName = resultSet.getString("name");
            	int clientID = resultSet.getInt("ClientID");
            	
            	resultClient = new Client(clientName, clientID);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultClient;
	}

	@Override
	public void createClient(String name) {
		String sql = "INSERT INTO clients (Name)"
				+ "VALUES (?)";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, name);
            prepStatement.executeUpdate();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateClient(int id, String name) {
		String sql = "UPDATE clients "
				+ "SET Name = ?"
				+ "WHERE ClientID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, name);
            prepStatement.setInt(2, id);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteClient(int id) {
		String sql = "DELETE FROM clients "
				+ "WHERE ClientID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, id);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
