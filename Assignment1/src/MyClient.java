import java.net.*;
import java.io.*;

public class MyClient 
{
	private Socket userSocket = null;
    private ObjectInputStream inFromServer = null;
    private ObjectOutputStream outToServer = null;
    private boolean isConnected = false;
    
    //enums
	private static final int USER = 0;
    private static final int WHISPER = 1;
    private static final int LOBBY = 2;
    private static final int HASHSET = 3;
    private static final int SENTMESSAGE = 8;
    private static final int BYE = 9;

    
    public void communicate() 
    {
    	boolean unique = false;
    	int userPort = -1;
    	int state = 0;
    	String inputName = "";
    	String userChoice = "";
    	String messageBody = "";
    	String toUser = "";
    	InetAddress userAddress;
    	BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    	
    	//Connect to server
    	while (!isConnected)
    	{
    		try 
    		{
    			userSocket = new Socket("localHost", 6066);
    			System.out.println("User Connected.");
    			isConnected = true;
    		}
    		catch (SocketException socketError) 
    		{
    			System.err.println(socketError.getMessage());
            }
    		catch (IOException IOerror)
            {
    			System.err.println(IOerror.getMessage());
            }
    	}
    	
    	//Setup
    	User user = new User();
    	userAddress = userSocket.getInetAddress();
		userPort = userSocket.getPort();
		try
		{
			outToServer = new ObjectOutputStream(userSocket.getOutputStream());
	        inFromServer = new ObjectInputStream(userSocket.getInputStream());
		}
		catch (IOException IOerror)
        {
			System.err.println(IOerror.getMessage());
        }

        
        //set username
    	while (!unique)
    	{
    		try
    		{
    			System.out.print("Username: ");
    			inputName = inputReader.readLine();
    			user = new User(inputName, userAddress, userPort);
    			outToServer.writeObject(USER);
                outToServer.writeObject(user);
                unique = (Boolean) inFromServer.readObject();
                
                if (unique == true)
            	{
            		System.out.println("Name already taken");
            		System.out.println();
            		System.out.print("Username: ");
        			inputName = inputReader.readLine();
        			user = new User(inputName, userAddress, userPort);
        			outToServer.writeObject(0);
                    outToServer.writeObject(user);
            	}
            	else
            	{
            		unique = true;
            	}
    		}
    		catch (IOException IOerror)
            {
    			System.err.println(IOerror.getMessage());
            }
            catch (ClassNotFoundException e) 
            {
            	System.err.println(e.getMessage());
    		}
    	}
    	System.out.println("Welcome, " + user.getName());
		System.out.println();

    	while (!userChoice.toLowerCase().equals("bye"))
        {
    		
    		try
    		{
	        	System.out.println("What you want?");
	        	System.out.println("1)Whisper");
	        	System.out.println("2)Disconnect");
	        	
	        	try
	        	{
	        		state = Integer.parseInt(inFromServer.readObject().toString());
	        		if (state == SENTMESSAGE)
	        		{
	        			Message receivedMessage = (Message) inFromServer.readObject();
	        			
	        			System.out.println(receivedMessage.getRecipient() + ": " + receivedMessage.getMessage());
	        		}
	        	}
	    		catch (IOException IOerror)
	            {
	    			System.err.println(IOerror.getMessage());
	            }
	            catch (ClassNotFoundException e) 
	            {
	            	System.err.println(e.getMessage());
	    		}
	        	
	        	
	        	userChoice = inputReader.readLine();
	        	
	        	if (userChoice.equals("1"))
	        	{
	        		System.out.print("Recipient: ");
	        		toUser = inputReader.readLine();
	        		System.out.print("Message: ");
	        		messageBody = inputReader.readLine();
	        		Message message = new Message(toUser, messageBody);
	        		outToServer.writeObject(WHISPER);
	        		outToServer.writeObject(message);
	        	}
	        	else if (userChoice.equals("2"))
	        	{
	        		outToServer.writeObject(BYE);
	    	        inFromServer.close();
	    	        outToServer.close();
	    	        userSocket.close();
	    	        System.out.println(user.getName() + " disconnected");
	    	        userChoice = "bye";
	        	}
    		}
    		catch (IOException IOerror)
            {
    			System.err.println(IOerror.getMessage());
            }
        }
    }
    
    public static void main(String[] args) 
    {
        MyClient client = new MyClient();
        client.communicate();
    }
}