/* ClientObserver.java
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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer 
{
//	public ArrayList<ArrayList<String>> groups;
//	public String username;
	
	public ClientObserver(OutputStream out) 
	{
		super(out);
	}
	
//	public void setGroups(ArrayList<ArrayList<String>> g)
//	{
//		groups = g;
//	}
//	
//	public void setUN(String un)
//	{
//		username = un;
//	}
	
	@Override
	public void update(Observable o, Object arg) 
	{
		String message = (String) arg;
//		if(message.contains("///COMMAND///&^//TO///SERVER///MSSG"))
//		{
//			String user = message.substring(message.indexOf("<<"));
//			int endidx = user.indexOf(">>");
//			
//			user = user.substring(0,endidx+1);
//		}
//		if(message.contains("////$#@////NEWGROUPASSIGNMENTFORUSER"))
//		{
//			
//		}
		
		
		this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
	}

}
