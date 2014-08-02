import java.io.Serializable;
import java.net.*;

public class User implements Serializable 
{
    private String name;
    private InetAddress address;
    private int port;
    private static final long serialVersionUID = 42L;

    public User(String name,InetAddress address, int port)
    {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public User()
    {
        this.name = "";
        this.address = null;
        this.port = -1;
    }
    
    public String getName()
    {
        return name;
    }
    
    public InetAddress getAddress()
    {
    	return address;
    }
    
    public int getPort()
    {
    	return port;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override 
    public boolean equals(Object ob)
    {
    	if (ob == this)
    	{
    		return true;
    	}
    	if (ob == null || ob.getClass() != this.getClass())
    	{
    		return false;
    	}
    	
    	User user = (User) ob;
    	return user.getName().equalsIgnoreCase(this.getName());
    }
    
    @Override 
    public int hashCode(){
    	String test = name.toLowerCase() + "_";
		return test.hashCode();
	}
}