package daos;

import java.util.List;
import models.Client;

public interface ClientDAO {
	public List<Client> getAllClients();
	public Client getClientByName(String name);
	public Client getClienttByID(int id);
	public void createClient(String name);
	public void deleteClient(int id);
}
