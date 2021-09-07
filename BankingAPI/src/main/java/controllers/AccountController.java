package controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
//import models.Client;
import models.Account;
import daos.AccountDAOImpl;
//import daos.ClientDAOImpl;
import utils.ConnectionUtil;

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
		app.patch("/client/:clientID/accounts/:accountFrom"
				+ "/transfer/:accountTo", 
				AccountController::transferAccount);
		app.post("/client/:clientID/accounts", 
				AccountController::insertAccount);
		app.put("/client/:clientID/accounts/:accountID",
				AccountController::updateAccount);
		app.delete("/client/:clientID/accounts/:accountID", 
				AccountController::deleteAccount);
	}
	
	public static void getAllClientAccounts(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		dao.getAllClientAccounts(clientID);
	}
	
	public static void getClientAccountByID(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		dao.getClientAccountByID(clientID, accountID);
	}
	
	public static void getClientAccountLessThan(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int amount = Integer.parseInt(ctx.pathParam("amount"));
		dao.getClientAccountsByMinAmt(clientID, amount);
	}
	
	public static void getClientAccountGreaterThan(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int amount = Integer.parseInt(ctx.pathParam("amount"));
		dao.getClientAccountsByMinAmt(clientID, amount);
	}
	
	public static void getClientAccountRange(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int minAmount = Integer.parseInt(ctx.pathParam("minAmount"));
		int maxAmount = Integer.parseInt(ctx.pathParam("maxAmount"));
		dao.getClientAccountsByRange(clientID, minAmount, maxAmount);
	}
	
	public static void insertAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		Account row = ctx.bodyAsClass(Account.class);
		dao.createAccount(row);
	}
	
	public static void updateAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int amount = Integer.parseInt(ctx.pathParam("amount"));
		dao.update(clientID, amount);
	}
	
	public static void deleteAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		Account row = ctx.bodyAsClass(Account.class);
		dao.deleteAccount(row);
	}
	
	public static void depositOrWithdraw(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		
		String[] jsonObject = jsonParser(ctx.body());
		if (jsonObject[0].equals("deposit")) {
			dao.deposit(accountID, Float.parseFloat(jsonObject[1]));
		}
		
		else if (jsonObject[0].equals("withdraw")) {
			dao.withdraw(accountID, Float.parseFloat(jsonObject[1]));
		}
	}
	
	public static void transferAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int accountFrom = Integer.parseInt(ctx.pathParam("accountFrom"));
		int accountTo = Integer.parseInt(ctx.pathParam("accountTo"));
		
		String[] jsonObject = jsonParser(ctx.body());
		dao.withdraw(accountFrom, Float.parseFloat(jsonObject[1]));
		dao.deposit(accountFrom, Float.parseFloat(jsonObject[1]));
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
