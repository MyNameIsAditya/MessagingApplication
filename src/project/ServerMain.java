/* ServerMain.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerMain extends Application
{
	private static ChatServer server;
	private TextArea textLog;
	private static Text numConnections = new Text("");
	
	
	public static void main(String[] args) 
	{
		server = new ChatServer();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) 
	{
		primaryStage.setTitle("Messaging Application - Server");
    	BorderPane brd = new BorderPane();
    	TabPane tabpane = new TabPane();
    	StackPane pane = new StackPane();
    	Scene scn = new Scene(brd, 1100, 400);
    	
    	Tab information = new Tab("Information");
    	Tab log = new Tab("Log");
    	tabpane.getTabs().add(information);
    	tabpane.getTabs().add(log);
    	
    	//ta = new TextArea();
    	//scene = new Scene(new ScrollPane(ta), 450, 200);
    	
    	brd.setCenter(tabpane);
    	
    	pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    	
    	Text time = new Text("MultiThreadServer Started: " + new Date() + "\n");
        time.setFill(Color.BLACK);
        time.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        time.setLineSpacing(83);
        time.setTextAlignment(TextAlignment.CENTER);
    	
    	TextFlow textFlow = new TextFlow();        
        Text s = new Text("Server IP Address: ");
        s.setFill(Color.BLACK);
        s.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        s.setTextAlignment(TextAlignment.CENTER);
        s.setLineSpacing(100);
        Text serverIP = new Text("" + server.getIP() + "\n");
        serverIP.setFill(Color.CORNFLOWERBLUE);
        serverIP.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        serverIP.setTextAlignment(TextAlignment.CENTER);
        serverIP.setLineSpacing(100);
        textFlow.getChildren().addAll(s, serverIP);
        textFlow.getChildren().add(new Text(System.lineSeparator()));
        Group group = new Group(textFlow);
        
        TextFlow connectionsTF = new TextFlow();        
        Text connections = new Text("\n\n" + "Number of Connection: ");
        connections.setFill(Color.BLACK);
        connections.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        connections.setTextAlignment(TextAlignment.CENTER);
        connections.setLineSpacing(100);
        numConnections = new Text("" + server.getNumClients() + "\n");
        numConnections.setFill(Color.CORNFLOWERBLUE);
        numConnections.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        numConnections.setLineSpacing(100);
        numConnections.setTextAlignment(TextAlignment.CENTER);
        connectionsTF.getChildren().addAll(connections, numConnections);
        connectionsTF.getChildren().add(new Text(System.lineSeparator()));
        Group connectionsGroup = new Group(connectionsTF);
        
        //TODO: Update Number of Connections
       
        pane.getChildren().add(time);
    	pane.getChildren().add(group);
    	pane.getChildren().add(connectionsGroup);
        
    	pane.setAlignment(Pos.CENTER);
    	
    	textLog = new TextArea();
    	textLog.setEditable(false);
    	
    	information.setContent(pane);
    	log.setContent(textLog);
    	tabpane.setSide(Side.TOP);
    	tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    	
    	primaryStage.setScene(scn);
    	primaryStage.show();
    	
    	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
    	{
		    @Override
		    public void handle(WindowEvent t) 
		    {
		    	server.closeServerSocket();
		        Platform.exit();
		        System.exit(0);
		    }
		});
    	
		try
		{
			server.setUpNetworking(numConnections, textLog);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
