/* ChatServer.java
 * EE422C Project 7 submission by
 * Replace <...> with your actual data.
 * Kedar Raman
 * kvr336
 * 16200
 * Aditya Khanna
 * ak34642
 * 16220
 * Slip days used: <1>
 * Spring 2019
 */
//GitHub: https://github.com/EE422C/project-7-chat-room-pr7-pair-73.git

package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class ChatServer extends Observable 
{
	private ServerSocket serverSock;
	private boolean stillActive;
	
	private int numClients;
	protected HashMap<String, ClientObserver> unames;
	protected ClientObserver lastJoined = null;
	private TextArea textLog;
	private ArrayList<ArrayList<String>> groups;
	
	public ChatServer()
	{		
		numClients = 0;
		groups = new ArrayList<ArrayList<String>>();
		groups.add(new ArrayList<String>());						//Broadcast Group - First Group in List of groups
		unames = new HashMap<String, ClientObserver>();
		
		try 
		{
			serverSock = new ServerSocket(5251);
			stillActive = true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	protected void setUpNetworking(Text numConnections, TextArea log) throws Exception 
	{
		new Thread( () -> { 
			try 
			{  
				textLog = log;
				textLog.appendText("MultiThreadServer started at " + new Date() + "\n");
				textLog.appendText("Server IP Address: " + getIP() + "\n\n");
				
				while (true) 
				{
					if (stillActive)
					{
						Socket clientSocket = null;
						numClients++;
						try
						{
							clientSocket = serverSock.accept();
						}
						catch (SocketException se)
						{
						}
						if (clientSocket != null)
						{
							ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
							//writer.setGroups(groups);
							lastJoined = writer;
							Thread t = new Thread(new ClientHandler(clientSocket));
							t.start();
							this.addObserver(writer);
							//JAVAFX
							numConnections.setText("" + getNumClients() + "\n");
							textLog.appendText("Client " + numClients + " has entered the chat.\n");
						}
					}
				}
			} 
			catch(IOException ex) 
			{ 
				ex.printStackTrace();
			}
		}).start();
	}
	
	public String getIP()
	{
		String address = null;
		
		Enumeration<NetworkInterface> interfaces = null;
		try 
		{
			interfaces = NetworkInterface.getNetworkInterfaces();
		} 
		catch (SocketException e1) 
		{
		}

        while (interfaces.hasMoreElements()) 
        {
            NetworkInterface networkInterface = interfaces.nextElement();

            try 
            {
				if (!networkInterface.isUp()) 
				{
					continue;
				}
			} 
            catch (SocketException e) 
            {
			}

            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) 
            {
                InetAddress addr = addresses.nextElement();
                String name = networkInterface.getDisplayName();
                if ((!(name.equals("Software Loopback Interface 1"))) && (!(name.equals("VirtualBox Host-Only Ethernet Adapter"))))
                {
                	if ((!(addr.getHostAddress().contains(":"))))
                	{
                		address = addr.getHostAddress();
                	}
                }
            }
        }
		
		return address;
	}
	
	public int getNumClients()
	{
		return numClients;
	}
	
	public void closeServerSocket()
	{
		stillActive = false;
		try 
		{
			serverSock.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	class ClientHandler implements Runnable 
	{
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) 
		{
			Socket sock = clientSocket;
			try 
			{
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}

		//@SuppressWarnings("deprecation")
		public void run() 
		{
			String message;
			try 
			{
				while ((message = reader.readLine()) != null) 
				{
					textLog.appendText(message + "\n");////////////////////////////////////////////////////////////////////////////////////
					String username;
					
					if(message.contains("UsernameInputForClientfh2@$37#@$3$4@432UIYO3U2HJF##1670ADFLH8sf3g2858*&rhyyo"))
					{
						username = message.substring(message.indexOf(':') + 1);
						
						//lastJoined.setUN(username);
						unames.put(username, lastJoined);///////////////////////////////////////////////////////
						
						int i = 0;
						for(String s: unames.keySet())
						{
							lastJoined.println("///received//$$$#@!username/////:" + s + "*" + i);
							i++;
						}						
						
						/////////////////////////////////////////////////////////////
						groups.get(0).add(username);		//Every time a new user is added they are added to the broadcast group
						/////////////////////////////////////////////////////////////
						
						
						setChanged();
						notifyObservers("///received//$$$#@!username/////:" + username+ "*" + (i+1));
					}
					else if(message.contains("///create////chat///box/##@!&3341fs#fr32DGA/////"))
					{
						String groupuns = message.substring(message.indexOf(':')+1);
						String[] unlist = groupuns.split("\\*");
						ArrayList<String> group = new ArrayList<String>();
						for(int i = 0; i < unlist.length; i++)
						{
							String s = unlist[i];
							
							group.add(s);
						}
						
						
						groups.add(group);
						
						for(String s: group)
						{
							setChanged();
							notifyObservers("////$#@//ResetGroup--UN:" + s + "GID:>>" + (groups.size()-1) + "USRLIST:<<" + group.toString());
						}
						
					}
					else if(message.contains("///COMMAND///&^//TO///SERVER///MSSG--GROUP"))
					{
						String grpnum = message.substring(message.indexOf('>')+1, message.indexOf(':'));
						String mssg = message.substring(message.indexOf(':')+1);
						int mssgGroup;
						try {
							mssgGroup = Integer.parseInt(grpnum);
							if(mssgGroup != -1) 
							{
								ArrayList<String> inpGroupRef = groups.get(mssgGroup);
								for(String s : inpGroupRef)
								{
									setChanged();
									notifyObservers("////45gd35sfgf455//4df46//MESSAGERECEIVED--GROUPNO<" + grpnum + "UN>" + s + ":" + mssg + "\n");
	
								}
							}
							
						}
						catch(NumberFormatException e)
						{
							e.printStackTrace();
						}
						
						
						
					}
					else if(message.contains("///COMMAND//QUIT///SERVERhsjfhdj35k3j5j4kfkd--User:"))
					{
						
						String unrm = message.substring(message.indexOf(':')+1);
						int i = 0;
						for(ArrayList<String> a : groups)
						{
							Iterator<String> iter = a.iterator();
							while(iter.hasNext())
							{
								
								String s = iter.next();
										
								
								if(s.equals(unrm))
								{
									iter.remove();
								}
								if(a.contains(unrm)) 
								{
									setChanged();
									///////////////////////////////////////////////////////////////
									//notifyObservers("////45gd35sfgf455//4df46//MESSAGERECEIVED--GROUPNO<" + i + "UN>" + s + ":" + unrm + " HAS DROPPED OUT OF GroupmEE" + "\n");
								}
								
							}
							i++;
						}
						setChanged();
						notifyObservers("////asdf3r92357w7dhr8659032023vghhd83REMOVE///--User:" + unrm);						
					}
					else
					{
//						setChanged();
//						notifyObservers(message);
					}
					
				}
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
		}
	}
}
