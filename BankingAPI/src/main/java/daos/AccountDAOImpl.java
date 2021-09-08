package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Account;
//import models.Client;
//import utils.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO{
	
	/*
	 * If I decide to revisit this project, I would probably make a base
	 * get method in a seperate class in the services package to avoid
	 * repetition
	 */
	
	Connection connection;
	
	public AccountDAOImpl(Connection conn) {
		connection = conn;
	}

	/**
	 * Executes the SQL statement that gets all accounts from a
	 * specific client and returns a List of said accounts
	 * @param clientID = clientID needed to specify 
	 * which client's accounts to return
	 */
	@Override
	public List<Account> getAllClientAccounts(int clientID) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			//System.out.println(prepStatement.toString());
			ResultSet resultSet = prepStatement.executeQuery();
			
            while(resultSet.next()) {
            	int accountID = resultSet.getInt("accountID");
            	int clientID2 = resultSet.getInt("clientID");
            	int balance = resultSet.getInt("balance");
            	
            	Account account = new Account(accountID, clientID2, balance);
            	accountList.add(account);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return accountList;
	}

	/**
	 * Executes the SQL statement that searches for a specific 
	 * client's account and returns said account
	 * @param clientID = clientID needed to specify 
	 * which client's account to return
	 * @param accountID = accountID needed to specify
	 * which account to return
	 */
	@Override
	public Account getClientAccountByID(int clientID, int accountID) {
		Account resultAccount = null;
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ? "
				+ "AND AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setInt(2, accountID);
			ResultSet resultSet = prepStatement.executeQuery();
			
            while(resultSet.next()) {
            	int accountID2 = resultSet.getInt("accountID");
            	int clientID2 = resultSet.getInt("clientID");
            	int balance = resultSet.getInt("balance");
            	
            	resultAccount = new Account(accountID2, clientID2, balance);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultAccount;
	}

	/**
	 * (Test Method)
	 * Executes the SQL statement that gets a client's accounts that are
	 * more than a specific amount and returns said accounts
	 * @param clientID = clientID needed to specify 
	 * which client's accounts to return
	 * @param amount = amount to compare accounts with
	 */
	@Override
	public List<Account> getClientAccountsByMinAmt(int clientID, float amount) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ? AND Balance > ?";
		
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setFloat(2, amount);
			ResultSet resultSet = prepStatement.executeQuery();
			
            while(resultSet.next()) {
            	int accountID = resultSet.getInt("accountID");
            	int clientID2 = resultSet.getInt("clientID");
            	int balance = resultSet.getInt("balance");
            	
            	Account account = new Account(accountID, clientID2, balance);
            	accountList.add(account);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	
		return accountList;
	}

	/**
	 * (Test Method)
	 * Executes the SQL statement that gets a client's accounts that are
	 * less than a specific amount and returns said accounts
	 * @param clientID = clientID needed to specify 
	 * which client's accounts to return
	 * @param amount = amount to compare accounts with
	 */
	@Override
	public List<Account> getClientAccountsByMaxAmt(int clientID, float amount) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts WHERE ClientID = ? AND Balance < ?";	
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setFloat(2, amount);
			ResultSet resultSet = prepStatement.executeQuery();
			System.out.println(prepStatement.toString());
			System.out.println(resultSet.toString());
			
            while(resultSet.next()) {
            	int accountID = resultSet.getInt("accountID");
            	int clientID2 = resultSet.getInt("clientID");
            	int balance = resultSet.getInt("balance");
            	
            	Account account = new Account(accountID, clientID2, balance);
            	accountList.add(account);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	
		return accountList;
	}

	/**
	 * Executes the SQL statement that gets a client's accounts that are
	 * within a specific range and returns said accounts
	 * @param clientID = clientID needed to specify 
	 * which client's accounts to return
	 * @param minAmount = minimum amount in range
	 * @param maxAmount = maximum amount in range
	 */
	@Override
	public List<Account> getClientAccountsByRange(int clientID, float minAmount, float maxAmount) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ? AND Balance > ? AND Balance < ?";
		
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setFloat(2, minAmount);
			prepStatement.setFloat(3, maxAmount);
			ResultSet resultSet = prepStatement.executeQuery();
			
            while(resultSet.next()) {
            	int accountID = resultSet.getInt("accountID");
            	int clientID2 = resultSet.getInt("clientID");
            	int balance = resultSet.getInt("balance");
            	
            	Account account = new Account(accountID, clientID2, balance);
            	accountList.add(account);
            }
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	
		return accountList;
	}

	/**
	 * Executes the SQL statement that creates an account in
	 * the account table in the database
	 * @param clientID = clientID needed to specify which
	 * client will have said account
	 * @param balance = specifies how much money is in new account
	 */
	@Override
	public void createAccount(int clientID, float balance) {
		String sql = "INSERT INTO accounts (ClientID, Balance) "
				+ "VALUES (?, ?)";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, clientID);
            prepStatement.setFloat(2, balance);
            prepStatement.executeUpdate();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Executes the SQL statement that deletes an account in
	 * the account table in the database
	 * @param accountID = specifies which account should be deleted
	 */
	@Override
	public void deleteAccount(int accountID) {
		String sql = "DELETE FROM accounts "
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, accountID);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Executes the SQL statement in which an ammount is added to
	 * an account's balance
	 * @param accountID = specifies which account will be deposited
	 * @param balance = specifies the amount that will be deposited
	 */
	@Override
	public void deposit(int accountID, float balance) {
		String sql = "UPDATE accounts "
				+ "SET Balance = Balance + ?"
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setFloat(1, balance);
            prepStatement.setInt(2, accountID);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes the SQL statement in which an ammount is subtracted from
	 * an account's balance
	 * @param accountID = specifies which account will be withdrawn
	 * @param balance = specifies the amount that will be withdrawn
	 */
	@Override
	public void withdraw(int accountID, float balance) {
		String sql = "UPDATE accounts "
				+ "SET Balance = Balance - ?"
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setFloat(1, balance);
            prepStatement.setInt(2, accountID);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes the SQL statement in which an ammount is updated to
	 * an account's balance
	 * @param accountID = specifies which account will be updated
	 * @param amount = specifies the amount that will be updated
	 */
	@Override
	public void update(int accountID, float amount) {
		String sql = "UPDATE accounts "
				+ "SET Balance = ?"
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setFloat(1, amount);
            prepStatement.setInt(2, accountID);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}