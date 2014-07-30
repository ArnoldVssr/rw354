import java.io.Serializable;
import java.net.*;

public class User implements Serializable 
{
    private String name;
    private SocketAddress address;
    private static final long serialVersionUID = 42L;

    public User(String name, SocketAddress address)
    {
        this.name = name;
        this.address = address;
    }

    public User()
    {
        this.name = "";
        this.address = null;
    }
    
    public String getName()
    {
        return name;
    }
    
    public SocketAddress getAddress()
    {
    	return address;
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