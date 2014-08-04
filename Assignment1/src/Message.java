import java.io.Serializable;


public class Message implements Serializable
{
	private String toUser = "";
	private String fromUser = "";
	private String body = "";
    private static final long serialVersionUID = 42L;
	
	public Message()
    {
        this.toUser = "";
        this.body = "";
        this.fromUser = "";
    }
	
	public Message(String fromUser,String toUser, String body)
    {
		this.fromUser = fromUser;
        this.toUser = toUser;
        this.body = body;
    }
	
	public String getMessage()
	{
		return body;
	}
	
	public String getRecipient()
	{
		return toUser;
	}
	
	public String getOrigin()
	{
		return fromUser;
	}
}
