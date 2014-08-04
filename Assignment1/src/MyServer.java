import java.net.*;
import java.io.*;
 
public class MyServer 
{	
    public static void main(String[] args) throws IOException 
    {
    	
	    int portNumber = 6066;
	    boolean listening = true;
	     
	    try
	    {
	    	ServerSocket serverSocket = new ServerSocket(portNumber);
            while (listening)
            {
	            new ClientHandler(serverSocket.accept()).start();
	        }
	    }
	    catch (IOException IOerror) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
