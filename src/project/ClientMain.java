/* ClientMain.java
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
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientMain extends Application
{
	
	private ChatClient backend;
	private TextArea textarea;
	private static AudioClip sound;							//Message Sound
	private static AudioClip whoosh;
	private static AudioClip ding;
	protected static Text members;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//whoosh = new AudioClip(getHostServices().getDocumentBase() + "/src/music/whoosh.mp3");
		whoosh = new AudioClip(getClass().getResource("/music/whoosh.mp3").toString());
		//ding = new AudioClip(getHostServices().getDocumentBase() + "/src/music/ding.mp3");
		ding = new AudioClip(getClass().getResource("/music/ding.mp3").toString());
		sound = whoosh;
		
		GridPane users = new GridPane();//////////////////////////////////////////////////////////////
		textarea = new TextArea();
		textarea.setEditable(false);
		//Login Screen
		primaryStage.setTitle("Messaging Application - Client");
    	BorderPane loginBorder = new BorderPane();
    	BorderPane messagingBorder = new BorderPane();
    	Scene loginScene = new Scene (loginBorder, 1500, 800);
    	Scene messagingScene = new Scene (messagingBorder, 1500, 800);
    	//primaryStage.setResizable(false);
    	
    	loginBorder.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));    	
    	messagingBorder.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));    	
    	
    	GridPane logo = new GridPane();
    	logo.setPrefHeight(370);
    	Text name = new Text("GroupmEE");
    	name.setFill(Color.WHITE);
    	name.setFont(Font.font ("Comic Sans MS", FontWeight.BOLD, 70));
    	name.setTextAlignment(TextAlignment.CENTER);
        name.setLineSpacing(100);
        logo.add(name, 0, 0);
        logo.setAlignment(Pos.BOTTOM_CENTER);
        logo.setHgap(30); //horizontal gap in pixels => that's what you are asking for
        logo.setVgap(10); //vertical gap in pixels
        logo.setPadding(new Insets(10, 10, 10, 10));
    	
    	GridPane loginBox = new GridPane();
    	Label username = new Label("Username: ");
    	username.setFont(Font.font ("Verdana", FontWeight.BOLD, 25));
    	username.setTextFill(Color.BLACK);
    	TextField usernameTF = new TextField();
    	usernameTF.setPrefWidth(350);
    	//Label password = new Label("Password: ");
    	//password.setFont(Font.font ("Verdana", FontWeight.BOLD, 25));
    	//password.setTextFill(Color.BLACK);
    	//TextField passwordTF = new TextField();
    	//passwordTF.setPrefWidth(350);
    	Label address = new Label("IP Address: ");
    	address.setFont(Font.font ("Verdana", FontWeight.BOLD, 25));
    	address.setTextFill(Color.BLACK);
    	TextField addressTF = new TextField();
    	addressTF.setPrefWidth(350);
    	
    	loginBox.add(username, 0, 0);
    	loginBox.add(usernameTF, 1, 0);
    	//loginBox.add(password, 0, 1);
    	//loginBox.add(passwordTF, 1, 1);
    	loginBox.add(address, 0, 1);
    	loginBox.add(addressTF, 1, 1);
    	loginBox.setAlignment(Pos.TOP_CENTER);
    	loginBox.setHgap(30); 
    	loginBox.setVgap(10); 
    	loginBox.setPadding(new Insets(10, 10, 10, 10));
    	
    	Button submit = new Button();
    	submit.setText("Login");	
    	submit.setStyle("-fx-background-color: linear-gradient(LightGreen, Green); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-size: 1.5em; -fx-font-weight: 500;");
    	
    	loginBox.add(submit, 0, 3);
    	
    	//Button account = new Button();
    	//account.setText("Create Account");
    	
    	/*account.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			//primaryStage.setScene(messagingScene);
    		}
    	});*/
    	
    	//account.setStyle("-fx-background-color: linear-gradient(LightGreen, Green); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-size: 1.5em; -fx-font-weight: 500;");
    	//loginBox.add(account, 1, 3);
    	
    	
    	//Backgrounds
    	//Image lightBackground = new Image("file:resources/whiteBackground.png");
    	Image lightBackground = new Image(getClass().getResource("/resources/whiteBackground.png").toString());
    	//Image darkBackground = new Image("file:resources/darkBackground.jpg");
    	Image darkBackground = new Image(getClass().getResource("/resources/darkBackground.jpg").toString());
    	//Image blueBackground = new Image("file:resources/blueBackground.jpg");
    	Image blueBackground = new Image(getClass().getResource("/resources/blueBackground.jpg").toString());
    	BackgroundImage lightBackGroundImage = new BackgroundImage(lightBackground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    	Background bg_light = new Background(lightBackGroundImage);
    	BackgroundImage darkBackGroundImage = new BackgroundImage(darkBackground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    	Background bg_dark = new Background(darkBackGroundImage);
    	BackgroundImage blueBackGroundImage = new BackgroundImage(blueBackground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    	Background bg_blue = new Background(blueBackGroundImage);
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	SplitPane splitpane = new SplitPane();
    	TabPane tabpane = new TabPane();
    	Tab activePeople = new Tab("Active Clients");					//See Active Clients and Create Rooms
    	Tab chatRooms = new Tab("ChatRooms");
    	Tab settings = new Tab("Settings");
    	tabpane.getTabs().add(activePeople);
    	tabpane.getTabs().add(chatRooms);
    	tabpane.getTabs().add(settings);
    	tabpane.setSide(Side.TOP);
    	tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    	
    	GridPane chatRoomsGrid = new GridPane();
    	ScrollPane roomScroll = new ScrollPane(chatRoomsGrid);
    	
    	BorderPane chat = new BorderPane();
    	textarea.setPrefSize(2000, 1000);//////////////////////////////////////////////////////
    	ScrollPane chatScroll = new ScrollPane(textarea);
    	chat.setCenter(chatScroll);
    	
    	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		
    		@Override
    		public void handle(WindowEvent event)
    		{
    			backend.quitServer();
//    			try 
//    			{
//					backend.socket.close();
//				} catch (IOException e)
//    			{
//					//e.printStackTrace();
//				}
    			Platform.exit();
    			System.exit(0);
    		}
    		
    	});
    	
    	
    	BorderPane sendMessage = new BorderPane();
    	chat.setBottom(sendMessage);
    	sendMessage.setMinHeight(70);
    	sendMessage.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));    	
    	
		TextField message = new TextField(); 
		message.setAlignment(Pos.CENTER_LEFT); 
		
		/*Button send = new Button();
		send.setText("Send");
    	
		send.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			backend.chatInput(message.getText());	
    			message.clear();
    		}
    	});
    	
		send.setStyle("-fx-background-color: DarkBlue; -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-size: 1.5em; -fx-font-weight: 500;");
    	*/
		
		sendMessage.setPadding(new Insets(10, 10, 10, 10));
		//sendMessage.setLeft(send); 
		sendMessage.setCenter(message); 

		BorderPane groupHeading = new BorderPane();
    	chat.setTop(groupHeading);
    	groupHeading.setMinHeight(50);
    	groupHeading.setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));    	
    	
    	members = new Text();///////////////////////////////////////////////////////////////////////////////////
    	members.setFill(Color.WHITE);
    	members.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
    	members.setTextAlignment(TextAlignment.CENTER);
        
        groupHeading.setCenter(members);
        chatRooms.setContent(roomScroll);
        
        
        Button createChat = new Button("Create Chat");
		users.add(createChat, 0, 0);
		createChat.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
		createChat.setPrefWidth(250);
        activePeople.setContent(users);
        users.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        
        GridPane settingsGrid = new GridPane();
    	Label styles = new Label("Background Styles: ");
    	styles.setUnderline(true);
    	styles.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    	styles.setTextFill(Color.BLACK);
    	settingsGrid.add(styles, 0, 0);
		Button darkMode = new Button("Dark Theme");
		darkMode.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
    	darkMode.setPrefWidth(250);
		settingsGrid.add(darkMode, 0, 1);
    	Button lightMode = new Button("Light Theme");
    	lightMode.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
    	lightMode.setPrefWidth(250);
    	settingsGrid.add(lightMode, 0, 2);
    	Button blueMode = new Button("Blue Theme");
    	blueMode.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
    	blueMode.setPrefWidth(250);
    	settingsGrid.add(blueMode, 0, 3);
    	settingsGrid.add(new Label(), 0, 4);
    	settings.setContent(settingsGrid);
    	Label notificationSound = new Label("Notification Sounds: ");
    	notificationSound.setUnderline(true);
    	notificationSound.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    	notificationSound.setTextFill(Color.BLACK);
    	settingsGrid.add(notificationSound, 0, 5);
    	Button whooshButton = new Button("Whoosh");
    	whooshButton.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
    	whooshButton.setPrefWidth(250);
		settingsGrid.add(whooshButton, 0, 6);
    	Button dingButton = new Button("Ding");
    	dingButton.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768); -fx-background-insets: 0,1,2,3; -fx-background-radius: 2,2,2,2; -fx-padding: 12 30 12 30; -fx-text-fill: white; -fx-font-size: 20px;");
    	dingButton.setPrefWidth(250);
    	settingsGrid.add(dingButton, 0, 7);
        
    	splitpane.getItems().addAll(tabpane, chat);
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//GridPane createAccount = new GridPane();
		backend = new ChatClient(users, chatRoomsGrid, chatScroll);
		users.setBackground(bg_light);
		//backend.chatRoomsGrid.setBackground(bg_light);////////////////////////////////////////////////////////////////
    	settingsGrid.setBackground(bg_light);
    	
    	submit.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			backend.setUserName(usernameTF.getText());
    			primaryStage.setTitle(usernameTF.getText()+"'s Chat");
    			backend.setIPAddress(addressTF.getText());
    			primaryStage.setScene(messagingScene);
    		}
    	});
    	
		message.setOnAction(e -> { 
			backend.chatInput(message.getText());	
			message.clear();
		});
		
		createChat.setOnAction(new EventHandler<ActionEvent>() {///////////////////////////////////////////////////////////////////////////////////
			@Override
			public void handle(ActionEvent event)
			{
				backend.createChat();
				
			}
		});
		
		darkMode.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			users.setBackground(bg_dark);
    	    	chatRoomsGrid.setBackground(bg_dark);
    	    	settingsGrid.setBackground(bg_dark);
    		}
    	});
		
		lightMode.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			users.setBackground(bg_light);
    	    	chatRoomsGrid.setBackground(bg_light);
    	    	settingsGrid.setBackground(bg_light);
    		}
    	});
		
		blueMode.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			users.setBackground(bg_blue);
    	    	chatRoomsGrid.setBackground(bg_blue);
    	    	settingsGrid.setBackground(bg_blue);
    		}
    	});
		
		whooshButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			sound = whoosh;
    		}
    	});
		
		dingButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event)
    		{
    			sound = ding;
    		}
    	});
		
    	
    	loginBorder.setTop(logo);
    	loginBorder.setCenter(loginBox);
    	messagingBorder.setCenter(splitpane);
    	
    	primaryStage.setScene(loginScene);
    	primaryStage.show();    	
    	
    	
    	
    	//SCROLL MENUS FOR EVERYTHING?????????????????????????????????????????????????????/

    	/*
		
		Button createChat = new Button("Create Chat");///////////////////////////////////////////////////////////////////////////////////
		createChat.setOnAction(new EventHandler<ActionEvent>() {///////////////////////////////////////////////////////////////////////////////////
			@Override
			public void handle(ActionEvent event)
			{
				backend.createChat();
				
			}
		});
		users.add(createChat, 10, 0);*/
	}
	
	public static void playSound()
	{
		sound.play();
	}
}

