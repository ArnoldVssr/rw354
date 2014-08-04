import java.net.*;
import java.util.HashSet;
import java.io.*;
 
public class ClientHandler extends Thread 
{
    private Socket socket = null;
    private ObjectInputStream recieved = null;
    private ObjectOutputStream sent = null;
    private static HashSet<User> users = new HashSet<User>();
    private static User cur_user = new User();
    
	private static final int USER = 0;
    private static final int WHISPER = 1;
    private static final int LOBBY = 2;
    private static final int HASHSET = 3;
    private static final int BYE = 9;
    
    public ClientHandler(Socket socket) {
    	super();
        this.socket = socket;
    }
    
    public void run() {
 
        try
        {
        	int state;
        	boolean unique = false;
        	System.out.println("remote Socket Address "
                    + socket.getRemoteSocketAddress());
        	
    		recieved = new ObjectInputStream(socket.getInputStream());
        	sent = new ObjectOutputStream(socket.getOutputStream());
        	
        	System.out.println("Before while");
        	// Get unique user
        	while (!unique)
        	{
        		/*System.out.println("Sync not working");
        		synchronized (MyClient.slock) {
					while (MyClient.readDone)
					{
						MyClient.slock.wait();
						System.out.println("Waiting on slock");
					}
					System.out.println("State incoming.");
					state = Integer.parseInt(recieved.readObject().toString());
					System.out.println(state);
				}
        		synchronized (MyClient.slock)
        		{
        			System.out.println("state: " + state);
        			if (state == USER)
        			{
        				MyClient.readDone = true;
        				MyClient.slock.notify();
        			}
        		}
        		synchronized (MyClient.slock) {
					while (MyClient.readDone)
					{
						MyClient.slock.wait();
					}
					cur_user =(User) recieved.readObject();
				}
        		synchronized (MyClient.slock)
        		{
        			if (users.contains(cur_user))
                	{
                		sent.writeObject(true);
                		System.out.println("Name taken, notifying client");
                	}
                	else
                	{
                		users.add(cur_user);
                		sent.writeObject(false);
                		System.out.println("Unique name, notifying client");
                		unique = true;
                	}
        			MyClient.slock.notify();
        		}*/
        		state = Integer.parseInt(recieved.readObject().toString());
        		System.out.println("state: " + state);
        		if (state == USER)
        		{
        			System.out.println(cur_user.getName());
        			cur_user = (User) recieved.readObject();
        			System.out.println(cur_user.getName());
        		}
            	
            	if (users.contains(cur_user))
            	{
            		sent.writeObject(true);
            		System.out.println("Name taken, notifying client");
            	}
            	else
            	{
            		users.add(cur_user);
            		sent.writeObject(false);
            		System.out.println("Unique name, notifying client");
            		unique = true;
            	}
        	}
        	// Wait user input
        	while(true)
        	{
        		state = Integer.parseInt(recieved.readObject().toString());
        		if (state == WHISPER)
        		{
        			Message temp = (Message) recieved.readObject();
        			for (User a:users)
        			{
        				if (temp.getRecipient() == a.getName())
        				{
        					Socket whispSoc = new Socket(a.getAddress(), a.getPort());
        					System.out.println(cur_user.getName() + " " + temp.getMessage());
        					ObjectOutputStream whispSent = new ObjectOutputStream(whispSoc.getOutputStream());
        					whispSent.writeObject(new Message(cur_user.getName(), temp.getMessage()));
        					break;
        				}
        			}
        		}
        		else if (state == LOBBY)
        		{
        			
        		}
        		else if (state == HASHSET)
        		{
        			
        		}
        		else if (state == BYE)
        		{
        			System.out.println("Exiting, notifying client");
            		sent.writeObject("Closing");
            		users.remove(cur_user);
                	recieved.close();
                	sent.close();
            		socket.close();
            		break;
        		}
        	}
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