package controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
//import models.Client;
import daos.ClientDAOImpl;
import utils.ConnectionUtil;
import services.Validator;

public class ClientController {

	private static Javalin javalin;
	
	public static void init(Javalin app) {
		javalin = app;
		app.get("/client", ClientController::getAllClients);
		app.get("/client/:id", ClientController::getClientByID);
		
		/*
		 * This post statement has been slightly modified compared to the
		 * specified endpoint in order to successfully update my database 
		 * with the appropriate information.
		 */
		app.post("/client/:name", ClientController::insertClient);
		
		app.put("/client/:id/:name", ClientController::updateClient);
		app.delete("/client/:id", ClientController::deleteClient);
	}
	
	/**
	 * Executes the GET endpoint that returns all clients 
	 * from the client table
	 * @param ctx
	 */
	public static void getAllClients(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		ctx.json(dao.getAllClients());
		ctx.status(200);
	}
	
	/**
	 * Executes the GET endpoint that returns specific account by
	 * Client ID
	 * @param ctx
	 */
	public static void getClientByID(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		Integer id = Integer.parseInt(ctx.pathParam("id"));
		if (!Validator.ifClientExists(dao, id)) {
			ctx.status(400);
			ctx.result("No such client found.");
		}
		
		ctx.json(dao.getClienttByID(id));
	}
	
	/**
	 * Executes the PUT endpoint that creates a Client in the client
	 * table in the database
	 * @param ctx
	 */
	public static void insertClient(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		String name = ctx.pathParam("name");
		dao.createClient(name);
		ctx.status(201);
	}
	
	/**
	 * Executes the PUT endpoint that updates a Client in the client
	 * table in the database
	 * @param ctx
	 */
	public static void updateClient(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		Integer id = Integer.parseInt(ctx.pathParam("id"));
		String name = ctx.pathParam("name");
		if (!Validator.ifClientExists(dao, id)) {
			ctx.status(400);
			ctx.result("No such client found.");
		}
		else {
			dao.updateClient(id, name);
		}
		
	}
	
	/**
	 * Executes the DELETE endpoint that deletes a Client in the Client
	 * table in the database
	 * @param ctx
	 */
	public static void deleteClient(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		Integer id = Integer.parseInt(ctx.pathParam("id"));
		if (!Validator.ifClientExists(dao, id)) {
			ctx.status(400);
			ctx.result("No such client found.");
		}
		else {
			dao.deleteClient(id);
			ctx.status(205);
		}
	}
}
