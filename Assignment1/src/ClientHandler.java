import java.net.*;
import java.util.HashSet;
import java.io.*;
 
public class ClientHandler extends Thread 
{
    private Socket socket = null;
    private ObjectInputStream recieved = null;
    private ObjectOutputStream sent = null;
    private static HashSet<User> users = new HashSet<User>();
    private boolean unique = false;
    
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
        	System.out.println("remote Socket Address "
                    + socket.getRemoteSocketAddress());
        	
        	recieved = new ObjectInputStream(socket.getInputStream());
        	sent = new ObjectOutputStream(socket.getOutputStream());
        	while (!unique)
        	{
	        	User cur_user = (User) recieved.readObject();
	        	
	        	if (containsUser(cur_user))
	        	{
	        		System.out.println("Name taken, notifying client");
	        		sent.writeObject(true);
	        	}
	        	else
	        	{
	        		System.out.println("Unique name, sending welcome to client");
	        		sent.writeObject(false);
	        		unique = true;
	        	}
        	}
        	while(true)
        	{
        		//System.out.println("test");
        		break;
        	}
        	//socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
			e.printStackTrace();
		}
    }
}
