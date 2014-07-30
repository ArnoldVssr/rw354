import java.net.*;
import java.util.HashSet;
import java.io.*;
 
public class ClientHandler extends Thread 
{
    private Socket socket = null;
    private ObjectInputStream recieved = null;
    private ObjectOutputStream sent = null;
    private static HashSet<User> users = new HashSet<User>();
    
	private static final int USER = 0;
    private static final int WHISPER = 1;
    private static final int LOBBY = 2;
    private static final int HASHSET = 3;
    private static final int BYE = 9;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
	
	public static boolean containsUser(User user)
	{
		if (users.contains(user))
		{
			return true;
		}
		else
		{
			users.add(user);
			return false;
		}
	}
    
    public void run() {
 
        try
        {
        	int state = 0;
        	boolean unique = false;
        	System.out.println("remote Socket Address "
                    + socket.getRemoteSocketAddress());
        	
    		recieved = new ObjectInputStream(socket.getInputStream());
        	sent = new ObjectOutputStream(socket.getOutputStream());
        	User cur_user = new User();
        	// Get unique user
        	while (!unique)
        	{
        		state = Integer.parseInt(recieved.readObject().toString());
        		if (state == USER)
        		{
        			cur_user = (User) recieved.readObject();	
        		}
            	
            	if (containsUser(cur_user))
            	{
            		System.out.println("Name taken, notifying client");
            		sent.writeObject(true);
            	}
            	else
            	{
            		System.out.println("Unique name, notifying client");
            		sent.writeObject(false);
            		unique = true;
            	}
        	}
        	// Wait user input
        	while(true)
        	{
        		Object mes = recieved.readObject();
        		if (mes.getClass().getName() == "java.lang.String")
        		{
        			String message = (String) mes;
        			if (message.equalsIgnoreCase("bye"))
        			{
                		System.out.println("Exiting, notifying client");
                		sent.writeObject("Closing");
                		users.remove(cur_user);
                    	recieved.close();
                    	sent.close();
                		socket.close();
                		break;
        			}
        			else if(message.equalsIgnoreCase("whisp"))
        			{
        				
        			}
        			else
            		{
            			System.out.println(message);
            		}
        		}
        	}
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
