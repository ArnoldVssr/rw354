
public class Message
{
	private String toUser = "";
	private String body = "";
	
	public Message(String toUser, String body)
    {
        this.toUser = toUser;
        this.body = body;
    }
	
	public String getMessage()
	{
		return body;
	}
	
	public String getReceiver()
	{
		return toUser;
	}
}
