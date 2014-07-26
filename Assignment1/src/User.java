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
}