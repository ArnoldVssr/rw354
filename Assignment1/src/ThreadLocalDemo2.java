import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ThreadLocalDemo2
{
	public static void main (String [] args)
	{
		/*MyThread mt1 = new MyThread ("A");
      mt1.start();
      /*MyThread mt2 = new MyThread ("B");
      MyThread mt3 = new MyThread ("C");
      mt1.start ();
      mt2.start ();
      mt3.start ();*/
	}
}
class MyThread extends Thread
{
	//private MyClient test;
	//private static ThreadLocal tl = new ThreadLocal ();
	private static boolean connected = true;
	MyThread (String name)
	{
		super (name);
		//this.test = a;
	}
	public void run ()
	{
		//System.out.println(MyClitest);
		String inputName = "";
		Object choice = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while(connected)
		{
			try 
			{
				connected = false;
				synchronized(MyClient.lock)
				{
					System.out.println("User Connected.");
					System.out.print("Username: ");
					inputName = reader.readLine();
					MyClient.user.setName(inputName);
					MyClient.readBusy = false;	
					MyClient.lock.notify();
				}
				while(!MyClient.readBusy)
				{
					MyThread.sleep(200);
				}
				
				while (MyClient.notUnique)
				{
					synchronized(MyClient.lock)
					{
						System.out.println("TLD2 says username was: " + MyClient.user.getName());
						System.out.println("Name already taken.");
						System.out.print("Username: ");
						inputName = reader.readLine();
						MyClient.username = inputName;
						System.out.println("TLD2 says username is: " + MyClient.user.getName());
						MyClient.readBusy = false;
						MyClient.lock.notifyAll();
					}
					while(!MyClient.readBusy)
					{
						MyThread.sleep(200);
					}
				}
				//System.out.println("TLD out of unique name");
				String tchoice = "";
				try
				{
					tchoice = (String) choice;
				}
				finally
				{
					
				}
				while(!tchoice.equalsIgnoreCase("bye"))
				{
					//System.out.println("TLD entered bye loop");
					//synchronized(MyClient.lock)
					//{
					System.out.println("What you want?");
					choice = reader.readLine();
					try
					{
						tchoice = (String) choice;
					}
					finally
					{
						if (tchoice != (String) choice)
							System.out.println(((Message) choice).getMessage());
					}
					if (tchoice.equalsIgnoreCase("whisper"))
                	{
                		System.out.print("User: ");
                		String username = reader.readLine();
                		System.out.println("Message: ");
                		String message = reader.readLine();
                		Message tm = new Message(username, message);
                		MyClient.outToServer.writeObject(1);
						MyClient.outToServer.writeObject(tm);
                	}
					/*MyClient.readBusy = false;
						//MyClient.lock.notifyAll();
					//}
					while(!MyClient.readBusy)
					{
						MyThread.sleep(200);
					}*/
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}