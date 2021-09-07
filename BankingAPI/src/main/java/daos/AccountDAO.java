package daos;

import java.util.List;
import models.Account;

public interface AccountDAO {
	
	public List<Account> getAllClientAccounts(int clientID);
	public Account getClientAccountByID(int clientID, int accountID);
	public List<Account> getClientAccountsByMinAmt(int clientID, int amount);
	public List<Account> getClientAccountsByMaxAmt(int clientID, int amount);
	public List<Account> getClientAccountsByRange(int clientID, int minAmount, int maxAmount);
	public void createAccount(Account account);
	public void deleteAccount(Account account);
	public void deposit(int accountID, float balance);
	public void withdraw(int accountID, float balance);
	public void update(int accountID, float amount);
}
