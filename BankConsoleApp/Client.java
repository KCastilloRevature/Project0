package BankConsoleApp;

import java.util.HashMap;
import java.util.Set;

public class Client {
	private String name; //name of client
	private int id; //randomly assigned client ID 
	
	//hashmap of accounts <AccountID, Balance>
	//(AccountID will be randomly assigned)
	private HashMap<Integer, Integer> accounts = new HashMap<Integer, Integer>();
	
	public Client(String name, int id, HashMap<Integer, Integer> accounts) {
		this.name = name;
		this.id = id;
		this.accounts = accounts;
	}
	
	public Client(String name, int id) {
		this(name, id, new HashMap<Integer, Integer>());
	}

	//typical getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<Integer, Integer> getAccounts() {
		return accounts;
	}

	public void setAccounts(HashMap<Integer, Integer> accounts) {
		this.accounts = accounts;
	}
	
	//maybe these methods below can be somewhere else? not entirely sure
	
	/*
	 * get method to return list of accounts based on specified amount: less than,
	 * greater than, and between a range are the 3 following methods
	 */
	public HashMap<Integer, Integer> getAccountsLT(int amount) {
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		Set<Integer> acctID = accounts.keySet();
		for (int id : acctID) {
			int balance = accounts.get(id);
			if (balance <= amount) {
				result.put(id, balance);
			}
		}
		return result;
	}
	
	public HashMap<Integer, Integer> getAccountsGT(int amount) {
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		Set<Integer> acctID = accounts.keySet();
		for (int id : acctID) {
			int balance = accounts.get(id);
			if (balance >= amount) {
				result.put(id, balance);
			}
		}
		return result;
	}

	public HashMap<Integer, Integer> getAccountsRange(int minAmount, int maxAmount) {
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		Set<Integer> acctID = accounts.keySet();
		for (int id : acctID) {
			int balance = accounts.get(id);
			if (balance >= minAmount && balance <= maxAmount) {
				result.put(id, balance);
			}
		}
		return result;
	}
	
	//adding or removing accounts for client
	public void addAccount(int id, int balance) {
		accounts.put(id, balance);
	}
	
	public void removeAccount(int id) {
		accounts.remove(id);
	}
	
	//adding or removing from balance, transfer uses the combination of both
	public void addAmount(int accountID, int amount) {
		Set<Integer> acctID = accounts.keySet();
		for (int id : acctID) {
			if (id == accountID) {
				int balance = accounts.get(id);
				balance += amount;
				accounts.replace(id, balance);
			}
		}
	}
	
	public void removeAmount(int accountID, int amount) {
		Set<Integer> acctID = accounts.keySet();
		for (int id : acctID) {
			if (id == accountID) {
				int balance = accounts.get(id);
				balance -= amount;
				accounts.replace(id, balance);
			}
		}
	}
}
	
	
