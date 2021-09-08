package services;

import daos.AccountDAOImpl;
import daos.ClientDAOImpl;

public class Validator {
	
	/**
	 * Checks if a Client exists in client table in database
	 * @param client = client DAO created with .getConnection()
	 * @param id = checked clientID, is parsed from endpoint
	 * @return = true if client in database, false otherwise
	 */
	public static boolean ifClientExists(ClientDAOImpl client, int id) {
		if (client.getClienttByID(id) == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if a Account exists in account table in database
	 * @param account = account DAO created with .getConnection()
	 * @param clientID = checked clientID, is parsed from endpoint
	 * @param accountID = checked accountID, is parsed from endpoint
	 * @return = true if client in database, false otherwise
	 */
	public static boolean ifAccountExists(AccountDAOImpl account, int clientID, int accountID) {
		if (account.getClientAccountByID(clientID, accountID) == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if withdrawal causes a negative result
	 * @param balance = account's current balance
	 * @param amount = withdrawal amount
	 * @return = false if difference is negative, true otherwise
	 */
	public static boolean ifInsufficientFunds(float balance, float amount) {
		if ((balance - amount) < 0) {
			return true;
		}
		
		return false;
	}
}
