import java.net.*;
import java.io.*;

public class ClientHandler extends Thread
{
	private Socket newClient = null;
		 
    public ClientHandler(Socket socket) {
        super("ClientHandler");
        this.newClient = socket;
    }
	     
	    public void run() {
	 
	        try (
	            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(
	                new InputStreamReader(
	                    socket.getInputStream()));
	        ) {
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
	        }
	    }

	}

}
