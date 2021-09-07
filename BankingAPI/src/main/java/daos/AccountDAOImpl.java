package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Account;
import models.Client;
import utils.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO{
	
	Connection connection;
	
	public AccountDAOImpl(Connection conn) {
		connection = conn;
	}

	@Override
	public List<Account> getAllClientAccounts(int clientID) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts"
				+ "WHERE ClientID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
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

	@Override
	public Account getClientAccountByID(int clientID, int accountID) {
		Account resultAccount = null;
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ?"
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

	@Override
	public List<Account> getClientAccountsByMinAmt(int clientID, int amount) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ? AND Balance > ?";
		
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setInt(2, amount);
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

	@Override
	public List<Account> getClientAccountsByMaxAmt(int clientID, int amount) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ? AND Balance < ?";
		
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setInt(2, amount);
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

	@Override
	public List<Account> getClientAccountsByRange(int clientID, int minAmount, int maxAmount) {
		List<Account> accountList = new ArrayList<Account>();
		String sql = "SELECT * FROM accounts "
				+ "WHERE ClientID = ? AND Balance > ? AND Balance < ?";
		
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
			prepStatement.setInt(1, clientID);
			prepStatement.setInt(2, minAmount);
			prepStatement.setInt(3, maxAmount);
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

	@Override
	public void createAccount(Account account) {
		String sql = "INSERT INTO accounts (ClientID, Balance)"
				+ "VALUES (?, ?)";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, account.getClientID());
            prepStatement.setFloat(2, account.getBalance());
            prepStatement.executeUpdate();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteAccount(Account account) {
		String sql = "DELETE FROM accounts "
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, account.getAccountID());
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deposit(int accountID, float balance) {
		String sql = "UPDATE accounts "
				+ "SET Balance = Balance + ?"
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, accountID);
            prepStatement.setFloat(2, balance);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void withdraw(int accountID, float balance) {
		String sql = "UPDATE accounts "
				+ "SET Balance = Balance - ?"
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, accountID);
            prepStatement.setFloat(2, balance);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(int accountID, float amount) {
		String sql = "UPDATE accounts "
				+ "SET Balance = ?"
				+ "WHERE AccountID = ?";
		try {
			PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, accountID);
            prepStatement.setFloat(2, amount);
            prepStatement.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}