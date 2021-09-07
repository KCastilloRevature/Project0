package services;
//import java.sql.Connection;

import io.javalin.Javalin;
//import utils.ConnectionUtil;
import controllers.ClientController;
import controllers.AccountController;

public class Driver {
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(42069);
        //Connection connection = ConnectionUtil.getConnection();
        ClientController.init(app);
        AccountController.init(app);
    }
}