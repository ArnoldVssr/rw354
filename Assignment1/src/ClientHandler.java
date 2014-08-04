import java.net.*;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.*;
 
public class ClientHandler extends Thread 
{
	
	private Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
    private static HashSet<User> users = new HashSet<User>();
    private static Map<String,Socket> Maptest = new HashMap<String,Socket>();
    private static User cur_user = new User();
    
    public ClientHandler(Socket socket)
    {
    	super();
    	this.socket = socket;
    }
    
    public void run() {
 
        try
        {
        	int state;
        	boolean unique = false;
        	System.out.println("remote Socket Address " + socket.getRemoteSocketAddress());
        	
        	sendbuf = new byte[socket.getSendBufferSize()];
        	recbuf = new byte[socket.getReceiveBufferSize()];
        	
        	while (!unique)
        	{
        		state = socket.getInputStream().read();
        		if (state == Message.USER)
        		{
        			socket.getInputStream().read(recbuf);
        			cur_user = (User) toObject(recbuf);
        			
        			if (users.contains(cur_user))
        			{
        				System.out.println("Not unique, notifying client");
        				sendbuf = toByteArray(true);
        				socket.getOutputStream().write(sendbuf);
        			}
        			else
        			{
        				System.out.println("Unique, notifying client");
        				unique = true;
        				users.add(cur_user);
        				Maptest.put(cur_user.getName(),socket);
        				sendbuf = toByteArray(false);
        				socket.getOutputStream().write(sendbuf);
        			}
        		}
        	}
        	
        	while(true)
        	{
        		state = socket.getInputStream().read();
        		if (state == Message.WHISPER)
        		{
        			socket.getInputStream().read(recbuf);
        			Message Temp = (Message) toObject(recbuf);
        			Socket rec = Maptest.get(Temp.getRecipient());
        			if (rec != null)
        			{
        				sendbuf = toByteArray(Temp);
        				rec.getOutputStream().write(sendbuf);
        				rec.getOutputStream().flush();
        			}
        			rec = Maptest.get(Temp.getOrigin());
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();
        		}
        		else if (state == Message.LOBBY)
        		{
        			socket.getInputStream().read(recbuf);
        			Message Temp = (Message) toObject(recbuf);
    				for(Map.Entry<String, Socket> entry: Maptest.entrySet())
    				{
    					sendbuf = toByteArray(Temp);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
    				}
        		}
        		else if (state == Message.HASHSET)
        		{
        			
        		}
        		else if (state == Message.BYE)
        		{
        			System.out.println("Exiting, notifying client");
        			Message send = new Message("server", cur_user.getName(),"closing");
            		sendbuf = ClientHandler.toByteArray(send);
            		socket.getOutputStream().write(sendbuf);
            		socket.getOutputStream().close();
            		socket.getInputStream().close();
            		users.remove(cur_user);
            		Maptest.remove(cur_user);
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
    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }
}