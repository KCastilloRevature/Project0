package controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import models.Client;
import daos.ClientDAOImpl;
import utils.ConnectionUtil;

public class ClientController {

	private static Javalin javalin;
	
	public static void init(Javalin app) {
		javalin = app;
		app.get("/client", ClientController::getAllClients);
		app.get("/client/:id", ClientController::getClientByID);
		app.post("/client/:id", ClientController::insertClient);
		app.delete("/client/:id", ClientController::deleteClient);
	}
	
	public static void getAllClients(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		dao.getAllClients();
	}
	
	public static void getClientByID(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		Integer id = Integer.parseInt(ctx.pathParam("clientID"));
		dao.getClienttByID(id);
	}
	
	public static void insertClient(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		Client row = ctx.bodyAsClass(Client.class);
		dao.createClient(row);
	}
	
	public static void deleteClient(Context ctx) {
		ClientDAOImpl dao = new ClientDAOImpl(ConnectionUtil.getConnection());
		Client row = ctx.bodyAsClass(Client.class);
		dao.deleteClient(row);
	}
}
