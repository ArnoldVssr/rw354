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
    private static final int SENTMESSAGE = 8;
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
    
    public void run() 
    {
    	
    	
    	/*try ( PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
        {
                String inputLine, outputLine;
                KnockKnockProtocol kkp = new KnockKnockProtocol();
                outputLine = kkp.processInput(null);
                out.println(outputLine);

                while ((inputLine = in.readLine()) != null) {
                    outputLine = kkp.processInput(inputLine);
                    out.println(outputLine);
                    if (outputLine.equals("Bye"))
                        break;
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
    	
    	
    	
 
        try
        {
        	int state;
        	boolean unique = false;
        	System.out.println("remote Socket Address "
                    + socket.getRemoteSocketAddress());
        	
    		recieved = new ObjectInputStream(socket.getInputStream());
        	sent = new ObjectOutputStream(socket.getOutputStream());
        	User currentUser = new User();
        	
        	// Get unique user
        	while (!unique)
        	{
        		state = Integer.parseInt(recieved.readObject().toString());
        		if (state == USER)
        		{
        			currentUser = (User) recieved.readObject();	
        		}
            	
            	if (containsUser(currentUser))
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
        	
        	// Wait for user input
        	while(true)
        	{
        		state = Integer.parseInt(recieved.readObject().toString());
        		if (state == WHISPER)
        		{
        			Message userMessage = (Message) recieved.readObject();
        			for (User recipient:users)
        			{
        				if (userMessage.getRecipient() == recipient.getName())
        				{
        					Socket whisperSocket = new Socket(recipient.getAddress(), recipient.getPort());
        					sent.writeObject(SENTMESSAGE);
        					ObjectOutputStream sentWhisper = new ObjectOutputStream(whisperSocket.getOutputStream());
        					sentWhisper.writeObject(new Message(currentUser.getName(), userMessage.getMessage()));
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
            		users.remove(currentUser);
                	recieved.close();
                	sent.close();
            		socket.close();
        		}
        		/*Object mes = recieved.readObject();
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
        		}*/
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