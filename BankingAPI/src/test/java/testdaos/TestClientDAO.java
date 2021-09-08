package testdaos;

import daos.ClientDAOImpl;
import models.Client;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class TestClientDAO {
	@InjectMocks
	private static ClientDAOImpl testClient;
	
	@Mock
	private static Connection mockConn;
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Mock
	private static Statement mockBaseStatement;
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Mock
	private static PreparedStatement mockStatement;
	{
		MockitoAnnotations.initMocks(this);
	}
	
    @Mock
    private static ResultSet mockResults;
	{
		MockitoAnnotations.initMocks(this);
	}
	
    @BeforeClass
    public static void setUp() {
    	testClient = new ClientDAOImpl(mockConn);
    }
    
    @AfterClass
    public static void tearDown() {
    	testClient = null;
    }
    
    //TODO: Find out why this test method fails, and fix this test
    //method
    @Test
    public void test_getClientByIDSuccess() throws SQLException {
    	//Arrange
    	int id = 1;
    	String str = "Kevin";
    	String sql = "SELECT * FROM clients"
    			+ "WHERE ClientID = ?";
    	Mockito.when(mockConn.prepareStatement(sql)).thenReturn(mockStatement);
    	Mockito.doNothing().when(mockStatement).setInt(1, id);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResults);
        Mockito.when(mockResults.next()).thenReturn(true);
        Mockito.when(mockResults.getString("name")).thenReturn(str);
        Mockito.when(mockResults.getInt("ClientID")).thenReturn(id);
        
        //Act
        Client client = testClient.getClienttByID(1);
        
        //Assert
        Assert.assertEquals(str, client.getName());
        Assert.assertEquals(id, client.getClientID());
        
        verify(mockStatement, times(1)).executeQuery();
    }
    
    //TODO: Find out why this test method fails, and fix this test
    //method
    @Test
    public void test_getClientByNameSuccess() throws SQLException {
    	//Arrange
    	int id = 1;
    	String str = "Kevin";
    	String sql = "SELECT * FROM clients"
    			+ "WHERE Name = ?";
    	Mockito.when(mockConn.prepareStatement(sql)).thenReturn(mockStatement);
    	Mockito.doNothing().when(mockStatement).setInt(1, id);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResults);
        Mockito.when(mockResults.next()).thenReturn(true);
        Mockito.when(mockResults.getString("name")).thenReturn(str);
        Mockito.when(mockResults.getInt("ClientID")).thenReturn(id);
        
        //Act
        Client client = testClient.getClientByName(str);
        
        //Assert
        Assert.assertEquals(str, client.getName());
        Assert.assertEquals(id, client.getClientID());
        
        verify(mockStatement, times(1)).executeQuery();
    }
    
    //TODO: Finish this test method, find a way to "mock" iterating through
    //a ResultSet and "mock" adding in Client objects into a list
    @Test
    public void test_getAllClientsSuccess() throws SQLException {
    	//Arrange
    	List<Client> expectedDB = Arrays.asList(new Client("Kevin", 1));
    	Mockito.when(testClient.getAllClients()).thenReturn(
    			Arrays.asList(new Client("Kevin", 1)));
        
        //Act
        List<Client> clientList = testClient.getAllClients();
        
        //Assert
        Assert.assertEquals(expectedDB, clientList);
    }
    
    //TODO: Finish this test method, find a way to look for the resulting
    //data to test.
    //@Test
    public void test_createClientSuccess() throws SQLException {
    	//Arrange
    	String str = "John";
		String sql = "INSERT INTO clients (Name)"
				+ "VALUES (?)";
		Mockito.when(mockConn.prepareStatement(sql)).thenReturn(mockStatement);
		Mockito.doNothing().when(mockStatement).setString(1, str);
		Mockito.doNothing().when(mockStatement.executeUpdate());
		
		//Act
		testClient.createClient(str);
		
		//Assert
		//assert that database has been successfully updated, maybe
		//AssertEquals(expected list object, actual list object)?
		verify(mockStatement, times(1)).executeUpdate();
    }
}
