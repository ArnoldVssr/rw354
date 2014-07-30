import java.io.Serializable;

public class Message implements Serializable 
{
	private static final long serialVersionUID = 42213L;
	private String toUser = "";
	private String body = "";
	
	public Message()
    {
        this.toUser = "";
        this.body = "";
    }
	
	public Message(String toUser, String body)
    {
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
}
