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
	
	//initializes endpoints that require me to modify my accounts table
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
	
	/**
	 * Executes the GET endpoint that returns a list of client account
	 * objects
	 * @param ctx 
	 */
	public static void getAllClientAccounts(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		ctx.json(dao.getAllClientAccounts(clientID));
	}
	
	/**
	 * Executes the GET endpoint that returns a client account by specified ID
	 * @param ctx
	 */
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
	
	/*
	 * For some reason, the method below is bugged, it's not 
	 * filtering out results based on amounts and is identical
	 * to getAllClientAccounts() at the moment.
	 */
	
	/**
	 * (Test Method) Executes the GET endpoint that 
	 * returns a client account that have balances that
	 * are less than specified amount
	 * @param ctx
	 */
	public static void getClientAccountLessThan(Context ctx) {
		AccountDAOImpl accountDAO = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		float amount = Float.parseFloat(ctx.pathParam("amount"));
		if (!Validator.ifClientExists(clientDAO, clientID)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		else {
			ctx.json(accountDAO.getClientAccountsByMaxAmt(clientID, amount));
		}
		
	}
	
	/*
	 * For some reason, the method below is bugged, it's not 
	 * filtering out results based on amounts and is identical
	 * to getAllClientAccounts() at the moment.
	 */
	
	/**
	 * (Test Method) Executes the GET endpoint that 
	 * returns a client account that have balances that
	 * are greater than specified amount
	 * @param ctx
	 */
	public static void getClientAccountGreaterThan(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		float amount = Float.parseFloat(ctx.pathParam("amount"));
		if (!Validator.ifClientExists(clientDAO, clientID)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		else {
			ctx.json(dao.getClientAccountsByMinAmt(clientID, amount));
		}
		
	}
	
	/*
	 * For some reason, the method below is bugged, it's not 
	 * filtering out results based on amounts and is identical
	 * to getAllClientAccounts() at the moment.
	 */
	
	/**
	 * Executes the GET endpoint that 
	 * returns a client account that have balances that
	 * are within a specified range
	 * @param ctx
	 */
	public static void getClientAccountRange(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		ClientDAOImpl clientDAO = new ClientDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		float minAmount = Float.parseFloat(ctx.pathParam("minAmount"));
		float maxAmount = Float.parseFloat(ctx.pathParam("maxAmount"));
		if (!Validator.ifClientExists(clientDAO, clientID)) {
			ctx.status(404);
			ctx.result("No such client exists");
		}
		else {
			ctx.json(dao.getClientAccountsByRange(clientID, minAmount, maxAmount));
		}
		
	}
	/**
	 * Executes the POST endpoint that creates an account
	 * onto the database
	 * @param ctx
	 */
	public static void insertAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		float balance = Float.parseFloat(ctx.pathParam("balance"));
		dao.createAccount(clientID, balance);
		ctx.status(201);
	}
	
	/**
	 * Executes the PUT endpoint that updates an account with a 
	 * new balance on the database
	 * @param ctx
	 */
	public static void updateAccount(Context ctx) {
		AccountDAOImpl dao = new AccountDAOImpl(ConnectionUtil.getConnection());
		int clientID = Integer.parseInt(ctx.pathParam("clientID"));
		int accountID = Integer.parseInt(ctx.pathParam("accountID"));
		float amount = Float.parseFloat(ctx.pathParam("amount"));
		if (!Validator.ifAccountExists(dao, clientID, accountID)) {
			ctx.status(404);
			ctx.result("No such account exists");
		}
		else {
			dao.update(accountID, amount);
		}
		
	}
	
	/**
	 * Executes the DELETE endpoint that deletes an account 
	 * from the database
	 * @param ctx
	 */
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
	
	/**
	 * Executes the PATCH endpoint that deposits or withdraws money from an
	 * account in the database
	 * @param ctx
	 */
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
	
	/**
	 * Executes the PATCH endpoint that transfer money between two
	 * accounts in the database
	 * @param ctx
	 */
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
	
	/**
	 * Parses the jsonObject that is within the request body
	 * @param json = String that will be parsed
	 * @return
	 */
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
