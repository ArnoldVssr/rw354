import java.net.*;
import java.io.*;

public class MyClient 
{
	private Socket socket = null;
    private ObjectInputStream inFromServer = null;
    private ObjectOutputStream outToServer = null;
    private boolean isConnected = false;

    
    public void communicate() 
    {
    	String inputName = "";
    	SocketAddress userAddress = null;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	
    	while (!isConnected)
    	{
    		try 
    		{
    			socket = new Socket("localHost", 6066);
    			System.out.println("User Connected.");
    			isConnected = true;
    			userAddress = socket.getRemoteSocketAddress();
    			outToServer = new ObjectOutputStream(socket.getOutputStream());
                inFromServer = new ObjectInputStream(socket.getInputStream());
    			System.out.print("Username: ");
    			inputName = reader.readLine();
    			User user = new User(inputName, userAddress);
    			/*System.out.println("Username changed to " + user.getName());
    			System.out.println("Object to be written = " + user);
    			System.out.println("Sending writeobject");*/
                outToServer.writeObject(user);
                String test = (String) inFromServer.readObject();
                System.out.println(test);
                //System.out.println("Sent writeobject");
                //System.out.println("Testing");
                
            }
    		catch (SocketException socketError) 
    		{
    			System.err.println(socketError.getMessage());
            } 
    		catch (IOException e)
            {
    			System.err.println(e.getMessage());
            } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public static void main(String[] args) 
    {
        MyClient client = new MyClient();
        client.communicate();
    }
}