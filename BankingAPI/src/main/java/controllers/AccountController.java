package controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import models.Client;
import models.Account;
import daos.AccountDAOImpl;
import daos.ClientDAOImpl;
import utils.ConnectionUtil;
import services.Validator;

public class AccountController {
	private static Javalin javalin;
	
	/*
	 * If I ever decide to revisit this project, some parts of this controller class
	 * may need to be refactored. The functions that mess with JSON objects 
	 * specifically can probably be broken down into seperate methods that can
	 * be in their own classes.
	 */
	
	public static void init(Javalin app) {
		javalin = app;
		app.get("/client/:clientID/accounts", 
				AccountController::getAllClientAccounts);
		app.get("/client/:clientID/accounts/:accountID", 
				AccountController::getClientAccountByID);
		app.get("/client/:clientID/accounts/"
				+ "?amountLessThan=:amount", 
				AccountController::getClientAccountLessThan);
		app.get("/client/:clientID/accounts/"
				+ "?amountGreaterThan=:amount", 
				AccountController::getClientAccountGreaterThan);
		app.get("/client/:clientID/accounts/"
				+ "?amountLessThan=:maxamount"
				+ "&amountGreaterThan=:minamount", 
				AccountController::getClientAccountRange);
		app.patch("/client/:clientID/accounts/:accountID", 
				AccountController::depositOrWithdraw);
		/*
		 * This particular REST endpoint has been slightly modified in order
		 * to fulfill a validation check that was required of me.
		 */
		app.patch("/clientF/:clientFrom/clientT/:clientTo"
				+ "/accounts/:accountFrom"
				+ "/transfer/:accountTo", 
				AccountController::transferAccount);
		
		/*
		 * This particular REST endpoint has been slightly modified in order
		 * to add in an balance to an account upon instantiation.
		 */
		app.post("/client/:clientID/accounts/:balance", 
				AccountController::insertAccount);
		
		/*
		 * This particular REST endpoint has been slightly modified in order
		 * to update a specified balance to an account upon instantiation.
		 */
		app.put("/client/:clientID/accounts/:accountID/:amount",
				AccountController::updateAccount);
		app.delete("/client/:clientID/accounts/:accountID", 
				AccountController::deleteAccount);
	}
	
	public static void getAllClientAccounts(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		ctx.json(dao.getAllClientAccounts(clientID));
	}
	
	public static void getClientAccountByID(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		if (!Validator.ifAccountExists(dao, clientID, accountID)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		else {
			ctx.json(dao.getClientAccountByID(clientID, accountID));
		}
		
	}
	
	public static void getClientAccountLessThan(Context ctx) {
		AccountDAOImpl accountDAO = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int amount = Integer.parseInt(ctx.pathParam("amount"));
		if (!Validator.ifClientExists(clientDAO, clientID)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		else {
			ctx.json(accountDAO.getClientAccountsByMaxAmt(clientID, amount));
		}
		
	}
	
	public static void getClientAccountGreaterThan(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int amount = Integer.parseInt(ctx.pathParam("amount"));
		if (!Validator.ifClientExists(clientDAO, clientID)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		else {
			ctx.json(dao.getClientAccountsByMinAmt(clientID, amount));
		}
		
	}
	
	public static void getClientAccountRange(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int minAmount = Integer.parseInt(ctx.pathParam("minAmount"));
		int maxAmount = Integer.parseInt(ctx.pathParam("maxAmount"));
		if (!Validator.ifClientExists(clientDAO, clientID)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		else {
			ctx.json(dao.getClientAccountsByRange(clientID, minAmount, maxAmount));
		}
		
	}
	
	public static void insertAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		float balance = Float.parseFloat(ctx.pathParam("balance"));
		dao.createAccount(clientID, balance);
		ctx.status(201);
	}
	
	public static void updateAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		int amount = Integer.parseInt(ctx.pathParam("amount"));
		if (!Validator.ifAccountExists(dao, clientID, accountID)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		else {
			dao.update(accountID, amount);
		}
		
	}
	
	public static void deleteAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		if (!Validator.ifAccountExists(dao, clientID, accountID)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		else {
			dao.deleteAccount(accountID);
		}
		
	}
	
	public static void depositOrWithdraw(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		
		if (!Validator.ifAccountExists(dao, clientID, accountID)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		
		else {
			String[] jsonObject = jsonParser(ctx.body());
			if (jsonObject[0].equals("deposit")) {
				dao.deposit(accountID, Float.parseFloat(jsonObject[1]));
			}
			
			else if (jsonObject[0].equals("withdraw")) {
				Account account = dao.getClientAccountByID(clientID, accountID);
				float balance = account.getBalance();
				if (Validator.ifInsufficientFunds(balance, Float.parseFloat(jsonObject[1]))) {
					ctx.status(422);
					ctx.result("Insufficient funds");
				}
				else {
					dao.withdraw(accountID, Float.parseFloat(jsonObject[1]));
				}
			}
		}
	}
	
	public static void transferAccount(Context ctx) {
		AccountDAOImpl accountDAO = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientFrom = Integer.parseInt(ctx.pathParam("clientFrom"));
		int clientTo = Integer.parseInt(ctx.pathParam("clientTo"));
		int accountFrom = Integer.parseInt(ctx.pathParam("accountFrom"));
		int accountTo = Integer.parseInt(ctx.pathParam("accountTo"));
		
		String[] jsonObject = jsonParser(ctx.body());
		
		if (!Validator.ifClientExists(clientDAO, clientFrom)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		
		else if (!Validator.ifAccountExists(accountDAO, clientFrom, accountFrom)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		
		else if (!Validator.ifAccountExists(accountDAO, clientTo, accountTo)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		
		else if (Validator.ifInsufficientFunds(Float.parseFloat(jsonObject[1]), Float.parseFloat(jsonObject[1]))) {
			ctx.status(422);
			ctx.result("Insufficient funds");
		}
		
		else {
			accountDAO.withdraw(accountFrom, Float.parseFloat(jsonObject[1]));
			accountDAO.deposit(accountTo, Float.parseFloat(jsonObject[1]));
		}
	}
	
	public static String[] jsonParser(String json) {
		String[] finishedJSON = new String[2];
		String[] parser = json.split("\"");
		finishedJSON[0] = parser[1];
		parser = parser[2].split(":");
		parser = parser[1].split("}");
		finishedJSON[1] = parser[0];
		return finishedJSON;
	}

}
