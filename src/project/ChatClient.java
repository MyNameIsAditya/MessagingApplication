/* ChatClient.java
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

import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label; 
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage; 


public class ChatClient
{ 
	// IO streams 
	protected Socket socket;
	private PrintWriter toServer = null; 
	private BufferedReader fromServer = null;
	boolean ipReceived;
	InetAddress ip;
	Thread serverUpdate;
	private String username = "Default Client #" + ((int)(Math.random() * 10000 + 1));//TODO: implement username- how to conn to server?
	static int lastclientNum = 0;
	int currentGroupNum = -1;
	
	private GridPane userList;
	private  GridPane chatRoomsGrid;
	private ScrollPane chatScroll;
	private HashMap<Integer, TextArea> chatWindows;
	private HashMap<Integer, Button> chooseWindow;
	private int chatRoomIndex;
	private TextArea broadcastTA;
	
	ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();

	
	public ChatClient(GridPane stp, GridPane crg, ScrollPane cs)
	{
		toServer = null;
		fromServer = null;
		ipReceived = false;
		ip = null;
		serverUpdate = new Thread(new ServerUpdate());
		
		userList = stp;
		chatRoomsGrid = crg;
		chatScroll = cs;
		chatWindows = new HashMap<Integer, TextArea>();
		chooseWindow = new HashMap<Integer, Button>();
		chatRoomIndex = 0;
	}
	
	public void createChat()
	{
		String command = "///create////chat///box/##@!&3341fs#fr32DGA/////:";
		ArrayList<String> usersInChat = new ArrayList<String>();
		
		boolean atLeastOneOther  = false;
		for(CheckBox cb : checkboxes)
		{
			if(cb.isSelected())
			{
				cb.setSelected(false);
				usersInChat.add(cb.getText());
				command = command + cb.getText() + "*";
				atLeastOneOther = true;
			}
		}
		command += username;
		if(atLeastOneOther)
		{
			toServer.println(command);
			toServer.flush();
		}
		
//		//CreateChat
//		//ta.appendText("NEW Chat Room with Users: ");
//		boolean first = true;
//		for(String s : usersInChat)
//		{
//			if(first)
//			{
//				ta.appendText(s);
//			}
//			else
//			{
//				ta.appendText(", " + s);
//			}
//			first = false;
//		}
//		ta.appendText("\n");
		
		
		
	}
	
	public void chatInput(String clientInput)
	{
		if(ipReceived)
		{
			toServer.println("///COMMAND///&^//TO///SERVER///MSSG--GROUP>" + this.currentGroupNum + ":(" + username + ")==>" + clientInput);
			toServer.println(username+":" + clientInput);
			toServer.flush(); 
		}
		else
		{
			//System.out.println("Read Input from User but Not Connected to Server");
		}
	}
	
	public void setUserName(String name)
	{
		username = name;
	}
	
	public void setIPAddress(String ipAddress)
	{
		try 
		{
			this.ip = InetAddress.getByName(ipAddress);
			this.ipReceived = true;			
		} 
		catch (UnknownHostException e1) 
		{
		}
		startClientFX(serverUpdate);
	}

	public GridPane getChatRoomsGrid()
	{
		return chatRoomsGrid;
	}
	
	public void startClientFX(Thread serverUpdate)
	{
		try 
		{ 
			// Create a socket to connect to the server 
			if(ipReceived)
			{
				socket = new Socket(ip, 5251);
				fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				toServer = new PrintWriter(socket.getOutputStream());
				toServer.println("UsernameInputForClientfh2@$37#@$3$4@432UIYO3U2HJF##1670ADFLH8sf3g2858*&rhyyo" + ":"+ username);//TODO: have to synchronize bc one thread might interrupt this!
				toServer.flush();
				
				//FOR BROADCAST
				Button broadcast = new Button("Broadcast Group");
				broadcast.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
				broadcast.setPrefWidth(400);
				chooseWindow.put(0, broadcast);
				broadcastTA = new TextArea();
				broadcastTA.setEditable(false);
				broadcastTA.setPrefSize(2000, 1000);
				chatWindows.put(0, broadcastTA);
				currentGroupNum = 0;
				
				Platform.runLater( () -> { 
					chatScroll.setContent(broadcastTA);					//Update chat window
					ClientMain.members.setText("Broadcast Group");
					chatRoomsGrid.add(broadcast, 0, chatRoomIndex);		//Update ChatRooms and buttons
					chatRoomIndex++;
				});
				
				broadcast.setOnAction(new EventHandler<ActionEvent>() {
		    		@Override
		    		public void handle(ActionEvent event)
		    		{
		    			ClientMain.members.setText("Broadcast Group");
		    			chatScroll.setContent(broadcastTA);
		    			currentGroupNum = 0;
		    		}
		    	});
				
				lastclientNum++;
			}
			 
			//Socket socket = new Socket(Server.ipaddr.getLocalHost(), 6789);
			
			// Create an input stream to receive data from the server 
			

			// Create an output stream to send data to the server 
			//toServer = new DataOutputStream(socket.getOutputStream()); 
			
		} 
		catch (IOException ex) 
		{ 
			//ta.appendText(ex.toString() + '\n');
			ex.printStackTrace();
		}
		
		if(ipReceived) 
		{
			serverUpdate.start();
		}
	}
	
	class ServerUpdate implements Runnable
	{

		@Override
		public void run() 
		{
			String serverInput = "";
			try 
			{
				while((serverInput = fromServer.readLine()) != null)
				{
					
					if(serverInput.contains("///received//$$$#@!username/////"))
					{
						String newUser = serverInput.substring(serverInput.indexOf(':')+1);
						
						String userNum = newUser.substring(newUser.indexOf('*')+1);
						newUser = newUser.substring(0,newUser.indexOf('*'));
						
						
						if(!(newUser.equals(username)) /*== true*/)
						{
							//ta.appendText("New User Added!:==> " + newUser + " User Num: " + userNum + "\n");
							
							CheckBox cb = new CheckBox(newUser);
							cb.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event)
								{
									//System.out.println("Selected " + cb.getText() + " For Chatting with");
									
								}
							});
							chatWindows.get(0).appendText(newUser + " joined GroupmEE!\n");
							checkboxes.add(cb);
							Platform.runLater( () -> { 
								userList.add(cb, 0, (Integer.parseInt(userNum) + 1));
							});
						}
					}
					else if(serverInput.contains("////$#@//ResetGroup--"))
					{
						String inUN = serverInput.substring(serverInput.indexOf(":")+1, serverInput.indexOf("GID:>>"));
						String inGPNUM = serverInput.substring(serverInput.indexOf(">>")+2, serverInput.indexOf("USRLIST:<<"));
						String groupMembers = serverInput.substring(serverInput.indexOf("<<")+2);
						String listOfMembers = groupMembers.substring(1, (groupMembers.length() - 1));
						if(inUN.equals(username))
						{
							//ta.clear();
							
							TextArea textarea = new TextArea();
							textarea.setEditable(false);
							textarea.setPrefSize(2000, 1000);
							textarea.appendText("NEW Chat Room with Users: " + listOfMembers + "\n");
							Button newGroup = new Button();
							newGroup.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
							newGroup.setPrefWidth(400);
							
							newGroup.setOnAction(new EventHandler<ActionEvent>() {
					    		@Override
					    		public void handle(ActionEvent event)
					    		{
					    			int buttonsGroupNum = -1;
					    			for (Entry<Integer, Button> entry : chooseWindow.entrySet()) {
					    		        if (entry.getValue().equals(newGroup)) {
					    		            buttonsGroupNum = entry.getKey();
					    		        }
					    		    }
					    			TextArea taForGroup = chatWindows.get(buttonsGroupNum);
					    			chatScroll.setContent(taForGroup);
					    			ClientMain.members.setText("Group Members: " + listOfMembers);
					    			currentGroupNum = buttonsGroupNum;
					    			//ta = chatWindows.get(currentGroupNum);///////////////////////////////////////////
					    		}
					    	});
							
							
							int groupNumber = -1;
							
							try 
							{	
								groupNumber = Integer.parseInt(inGPNUM);
							}
							catch(NumberFormatException e)
							{
								e.printStackTrace();
							}
							
							chatWindows.put(groupNumber, textarea);
							newGroup.setText("Group " + groupNumber + ": " + listOfMembers);
							chooseWindow.put(groupNumber, newGroup);
							currentGroupNum = groupNumber;
							/////////////////////////////////////////////////////////////////////////////////group members printed///////////////////////////
							
							/////////////////////////////////////UPDATE GUI/////////////////////////////////
							Platform.runLater( () -> { 
								chatScroll.setContent(textarea);					//Update chat window
								chatRoomsGrid.add(newGroup, 0, chatRoomIndex);		//Update ChatRooms and buttons
								ClientMain.members.setText("Group Members: " + listOfMembers);
								chatRoomIndex++;
							});
							
							
						}
					}
					else if(serverInput.contains("////45gd35sfgf455//4df46//MESSAGERECEIVED--"))
					{
						String groupNumber = serverInput.substring(serverInput.indexOf('<')+1, serverInput.indexOf("UN>"));
						String inpUN = serverInput.substring(serverInput.indexOf('>')+1, serverInput.indexOf(':'));
						String inpMssg = serverInput.substring(serverInput.indexOf(':')+1);
						try {
							int grpNum = Integer.parseInt(groupNumber);
							TextArea toUpdate = chatWindows.get(grpNum);
							
							if(inpUN.equals(username))
							{
								toUpdate.appendText(inpMssg+"\n");
								ClientMain.playSound();
							}
						}
						catch(NumberFormatException ex)
						{
							ex.printStackTrace();
						}
						
					}
					else if(serverInput.contains("////asdf3r92357w7dhr8659032023vghhd83REMOVE///--User:"))
					{
						//System.out.println("GOT REMOVE COMMAND");
						String unrm = serverInput.substring(serverInput.indexOf(':')+1);
						//System.out.println("GOT REMOVE COMMAND ======> " + unrm);
						for(CheckBox cb : checkboxes)
						{
							if (cb.getText().equals(unrm))
							{
								Platform.runLater( () -> { 
									cb.setSelected(false);
									cb.setDisable(true);
									cb.setText(unrm + " has dropped out of GroupmEE");
								});
								broadcastTA.appendText(unrm + " has dropped out of GroupmEE.\n");
							}
						}
					}
					else
					{
						//ta.appendText(serverInput + "\n");
					}
				}	
			} 
			catch (IOException e1) 
			{
			}
		}
		
	}
	
	public void quitServer()
	{
		if (toServer != null)
		{
			toServer.println("///COMMAND//QUIT///SERVERhsjfhdj35k3j5j4kfkd--User:" + username);
			toServer.flush();
		}
	}
	
}

