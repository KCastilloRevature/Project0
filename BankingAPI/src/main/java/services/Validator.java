package services;

import daos.AccountDAOImpl;
import daos.ClientDAOImpl;

public class Validator {
	public static boolean ifClientExists(ClientDAOImpl client, int id) {
		if (client.getClienttByID(id) == null) {
			return false;
		}
		
		return true;
	}
	
	public static boolean ifAccountExists(AccountDAOImpl account, int clientID, int accountID) {
		if (account.getClientAccountByID(clientID, accountID) == null) {
			return false;
		}
		
		return true;
	}
	
	public static boolean ifInsufficientFunds(float balance, float amount) {
		if ((balance - amount) < 0) {
			return true;
		}
		
		return false;
	}
}
