package services;

import io.javalin.Javalin;
import utils.ConnectionUtil;
import controllers.ClientController;
import com.github.javafaker.Faker;
import controllers.AccountController;
import java.sql.Connection;
import daos.ClientDAOImpl;
import daos.AccountDAOImpl;
import java.util.Random;

public class Driver {
	
	public static Faker faker = new Faker();
	public static Random random = new Random();
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(42069);
        Connection connection = ConnectionUtil.getConnection();
        ClientController.init(app);
        AccountController.init(app);
        
        //This snippet of code is used to initialize my database for testing
        
//        ClientDAOImpl clientDB = new ClientDAOImpl(connection);
//        AccountDAOImpl accountDB = new AccountDAOImpl(connection);
        
//        for (int i = 0; i < 51; i++) {
//        	String name = faker.name().firstName();
//        	clientDB.createClient(name);
//        }
        
//        for (int i = 0; i < 125; i++) {
//        	float start = (float) 0.00;
//        	float end = (float) 999.99;
//        	int randomID = random.nextInt(52);
//        	float percentage = random.nextFloat();
//        	
//        	float randomBalance = start + (percentage * (end - start));
//        	
//        	accountDB.createAccount(randomID, randomBalance);
//        }
    }
}