import java.net.*;
import java.util.HashSet;
import java.io.*;
 
public class ClientHandler extends Thread 
{
    private Socket socket = null;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
     
    public void run() {
 
        try
        {
        	System.out.println("remote Socket Address "
                    + socket.getRemoteSocketAddress());        	
        	/*PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	
            String inputLine, outputLine;
            Protocol kkp = new Protocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);
 
            while ((inputLine = in.readLine()) != null) 
            {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye"))
                    break;
            }*/
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
