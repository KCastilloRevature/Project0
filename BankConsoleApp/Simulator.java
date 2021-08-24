package BankConsoleApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import java.util.Random;
import java.util.Scanner;

public class Simulator {
	private static ArrayList<Client> clientDB = new ArrayList<Client>();
	private static int clientID = 0;
	private static int accountID = 1000;
	private static boolean hasQuit = false;
	
	private static void printPrompt() {
		System.out.println(
				"WELCOME TO THE CASTILLO BANK! HERE YOU CAN DO THESE COMMANDS"
				+ "HERE!\n\n"
				+ "Add client - add [CLIENT NAME]\n"
				+ "Add account - add [CLIENT NAME] account\n"
				+ "Add balance - add [CLIENT NAME] [ACCOUNT ID] [BALANCE]\n"
				+ "Check client - check [CLIENT NAME]\n"
				+ "Remove client - remove [CLIENT NAME]\n"
				+ "Remove account - remove [CLIENT NAME] account\n"
				+ "Remove balance - remove [CLIENT NAME] [ACCOUNT ID] [BALANCE]\n"
				+ "Transfer funds - transfer [SOURCE ACCOUNT ID] [DESTINATION ACCOUNT ID] [BALANCE]\n"
				+ "Quit - quit\n\n"
				+ "Your input here: ");
	}
	
	private static void addClient(Client client) {
		clientDB.add(client);
	}
	
	private static void checkClient(String checkedClient) {
		for (Client client : clientDB) {
			if (client.getName().equals(checkedClient)) {
				System.out.println("Name: " + client.getName());
				System.out.println("Client ID: " + client.getId() + "\n");
				
				HashMap<Integer, Integer> accounts = client.getAccounts();
				Iterator acctIterator = accounts.entrySet().iterator();
				while (acctIterator.hasNext()) {
					Map.Entry mapElement = (Map.Entry)acctIterator.next();
					System.out.println("Account ID: " + mapElement.getKey());
					System.out.println("Balance: " + mapElement.getValue() + "\n\n");
				}
			}
		}
	}
	
	private static void removeClient(Client client) {
		clientDB.remove(client);
	}
	
	private static String[] collectInput() { 
		Scanner scan = new Scanner(System.in);
		return scan.nextLine().split(" ");
	}
	
	private static void parse(String[] command) {
		String instruction = command[0].toLowerCase();
		switch (instruction) {
			case "add":
				break;
				
			case "remove":
				break;
				
			case "check":
				String checkedClient = command[1].toLowerCase();
				checkClient(checkedClient);
				break;
				
			case "transfer":
				Integer srcAccount = Integer.valueOf(command[1]);
				Integer destAccount = Integer.valueOf(command[2]);
				Integer transBalance = Integer.valueOf(command[3]);
				transferFunds(srcAccount, destAccount, transBalance);
				break;
				
			case "quit":
				hasQuit = true;
				break;
				
			default:
				System.out.println("I'm sorry, what was that?");
				break;
		}
	}
	
	private static void transferFunds(int srcID, int destID, int balance) {
		for (Client client : clientDB) {
			if (client.getId() == srcID) {
				client.removeAmount(srcID, balance);
			}
			
			if (client.getId() == destID) {
				client.addAmount(destID, balance);
			}
		}
	}
	
	public static void main(String[] args) {
		while (!hasQuit) {
			printPrompt();
			String[] input = collectInput();
			parse(input);
		}
	}
}