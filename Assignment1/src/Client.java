import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client implements Runnable
{
	Socket socket;
	Scanner input;
	Scanner send = new Scanner(System.in);
	PrintWriter output;
	String ID;
	
	//log in window
	public static JFrame loginWindowRE = new JFrame();
	public static JTextField userNameBoxFieldRE = new JTextField(20);
	private static JButton enterButtonRE = new JButton("Enter");
	private static JLabel enterUserNameLabelRE = new JLabel("Enter username: ");
	private static JPanel loginPaneRE = new JPanel();
	
	
	public Client(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		try
		{
			try
			{
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream());
				output.flush();
				CheckStream();
			}
			finally
			{
				socket.close();
			}
		}
		catch (Exception e)
		{
			System.out.println("error in client run");
			System.out.println(e);
		}
	}
	
	public void CheckStream()
	{
		while (true)
		{
			Receive();
		}
	}
	
	public void Receive()
	{
		if (input.hasNext())
		{
			String message = input.nextLine();
			System.out.println("Client received: " + message);
			String option = message.substring(0, 3);
			
			if (option.equals("#?!"))
			{
				String temp = message.substring(3);
				temp = temp.replace("[", "").trim();
				temp = temp.replace("]", "").trim();
				
				String[] current = temp.split(",");
				
				for (int i = 0; i < current.length; i++)
				{
					current[i] = current[i].trim();
				}
				
				ClientGUI.onlineUsers.setListData(current);
			}
			else if (option.equals("%^&"))
			{
				JOptionPane.showMessageDialog(null, "Username taken, please input a unique username.");
				loginWindowRE.setTitle("Username taken.");
				loginWindowRE.setSize(400, 100);
				loginPaneRE = new JPanel();
				loginPaneRE.add(enterUserNameLabelRE);
				userNameBoxFieldRE.setText("");
				loginPaneRE.add(userNameBoxFieldRE);
				loginPaneRE.add(enterButtonRE);
				loginWindowRE.add(loginPaneRE);
				System.out.println("stared RElogin action");
				Login_Action();
				loginWindowRE.setVisible(true);
			}
			else
			{
				System.out.println("this is the message: " + message);
				ClientGUI.chatArea.append(message + "\n");
			}
		}
	}
	
	public void Disconnect() throws Exception
	{
		output.println(ClientGUI.userName + " disconnected");
		output.flush();
		output.close();
		JOptionPane.showMessageDialog(null, "You disconnected.");
		System.exit(0);
	}
	
	public void Send(String message)
	{
		output.println("[" + ClientGUI.userName + "]: " + message);
		output.flush();
		ClientGUI.messageField.setText("");
	}
	
	public void Login_Action()
	{
		enterButtonRE.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						Action_B_Enter();
					}
				});
	}
	
	public void Action_B_Enter()
	{
		if (!userNameBoxFieldRE.getText().equals(""))
		{		
			String tempName = userNameBoxFieldRE.getText().trim();		
			ClientGUI.userName = tempName;
			//userNameLabel.setText("Username: " + userName);
			//Server.userList.add(userName);
			loginWindowRE.setVisible(false);
			System.out.println("Doing connect");
			output.println(tempName);
			output.flush();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter a username.");
		}
	}
}
