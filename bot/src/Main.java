

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.*;
import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import io.github.awsdcrafting.WarnSystem;

public class Main 
{
	static volatile int botIDm;
	static int DebugLevel=4;
	static String clientName;
	static String vote;
	static int a_vote_m1 = 0;
	static int a_vote_m2 = 0;
	static int a_vote_m3 = 0;
	static int a_vote_m4 = 0;
	static String vote_m1;
	static String vote_m2;
	static String vote_m3;
	static String vote_m4;
	static ArrayList<String> alWarnungen;

	public static void	main(String[] args)
	{
		final TS3Config config = new TS3Config();
		config.setHost("31.214.227.53"); // Die IP-Adresse des Servers, ohne Port
		config.setFloodRate(FloodRate.UNLIMITED);
		config.setDebugLevel(Level.ALL);
		
			// Use default exponential backoff reconnect strategy
				config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());

				// Make stuff run every time the query (re)connects
				config.setConnectionHandler(new ConnectionHandler() {

					@Override
					public void onConnect(TS3Query ts3Query) {
						stuffThatNeedsToRunEveryTimeTheQueryConnects(ts3Query.getApi());
					}

					@Override
					public void onDisconnect(TS3Query ts3Query) {
						// Nothing
					}
				});
			final TS3Query query = new TS3Query(config);
			query.connect(); // Verbinden
				
		
		
		
		// Register the event listener
		final BotTS3EventAdapter adapter = new BotTS3EventAdapter(query.getApi(), botIDm,query);
		query.getApi().addTS3Listeners(adapter);
		alWarnungen = io.github.awsdcrafting.WarnSystem.leseWarnungenAsArrayList();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				query.getApi().logout();
				query.exit();}});
	}
	
	public static void stuffThatNeedsToRunEveryTimeTheQueryConnects(TS3Api api){
		// Logging in, selecting the virtual server, selecting a channel
		// and setting a nickname needs to be done every time we reconnect
		api.login("scissiV2", "e5WqptB8");
		api.selectVirtualServerByPort(12200);
		api.moveQuery(52360);
		api.setNickname("BOT");

		// What events we listen to also resets
		api.registerAllEvents();

		// Out clientID changes every time we connect and we need it
		// for our event listener, so we need to store the ID in a field
		botIDm = api.whoAmI().getId();
	}
	
	private static class BotTS3EventAdapter extends TS3EventAdapter
	{
		TS3Api api;
		int botID;
		TS3Query query2;
		
		BotTS3EventAdapter(TS3Api api_, int id, TS3Query query_)
		{
			api = api_;
			botID = id;
			query2 = query_;
		}
		
		public void onTextMessage(TextMessageEvent e)
		{

			if (e.getTargetMode() != TextMessageTargetMode.SERVER && e.getInvokerId() != botID) 
			{
				String message = e.getMessage();
				if(DebugLevel==4)
				{
					clientName = e.getInvokerName();
					System.out.println(clientName +": " + message);
				}
				if(message.startsWith("!"))
				{
					if(DebugLevel>=3)
					{
						System.out.println("" + message);
					}
				}

				if(message.equals("!kick"))
				{
					api.sendChannelMessage("Command: !kick name grund");
				}
				else
				{
					if(message.startsWith("!kick "))
					{
						String[] subString = message.split(" ",3);
						String befehl = subString[0];
						clientName = subString[1];
						System.out.println("clientName: " +clientName);
						String kickGrund = subString[2];
						
						if(kickGrund.isEmpty())
						{
							kickGrund = "Du wurdest gekickt!";
						}
						for(Client client : api.getClients()){
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							
							if(apiClientName.equals(clientName))
							{
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromServer(kickGrund, clientID);
								api.sendChannelMessage(clientName + " wurde gekickt!");
							}
						}
					}
				}

				if(message.equals("!kick*"))
				{
					api.sendChannelMessage("Command: !kick name grund");
				}
				else
				{
					if(message.startsWith("!kick* "))
					{
						String[] subString = message.split(" ",3);
						String befehl = subString[0];
						clientName = subString[1];
						System.out.println("clientName: " +clientName);
						String kickGrund = subString[2];
						
						if(kickGrund.isEmpty())
						{
							kickGrund = "Du wurdest gekickt!";
						}
						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							
							if(apiClientName.startsWith(clientName))
							{
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromServer(kickGrund, clientID);
								api.sendChannelMessage(clientName + " wurde gekickt!");
							}
						}
					}
				}

				if(message.equals("!ckick"))
				{
					api.sendChannelMessage("Command: !ckick name grund");
				}
				else
				{
					if(message.startsWith("!ckick "))
					{
						String[] subString = message.split(" ",3);
						String befehl = subString[0];
						clientName = subString[1];
						System.out.println("clientName: " +clientName);
						String kickGrund = subString[2];
						
						if(kickGrund.isEmpty()){kickGrund = "Du wurdest gekickt!";
						}

						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							
							if(apiClientName.equals(clientName))
							{
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromChannel(kickGrund, clientID);
								api.sendChannelMessage(clientName + " wurde gekickt!");
							}
						}
					}
				}

				if(message.equals("!ckick*"))
				{
					api.sendChannelMessage("Command: !ckick name grund");
				}
				else
				{
					if(message.startsWith("!ckick* "))
					{
						String[] subString = message.split(" ",3);
						String befehl = subString[0];
						clientName = subString[1];
						System.out.println("clientName: " +clientName);
						String kickGrund = subString[2];
						
						if(kickGrund.isEmpty() || kickGrund == "" || kickGrund == " ")
						{
							kickGrund = "Du wurdest gekickt!";
						}

						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							
							if(apiClientName.startsWith(clientName))
							{
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromChannel(kickGrund, clientID);
								api.sendChannelMessage(clientName + " wurde gekickt!");
							}
						}
					}
				}

				if(message.equals("!msg"))
				{
					api.sendChannelMessage("Command: !msg name nachricht");
				}
				else
				{
					if(message.startsWith("!msg "))
					{
						String[] subString = message.split(" ",3);
						String befehl = subString[0];
						clientName = subString[1];
						System.out.println("clientName: " +clientName);
						String msgMSG = subString[2];

						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							
							if(apiClientName.equals(clientName))
							{
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.sendPrivateMessage(clientID,e.getInvokerName() + " hat dich angeschrieben: " + msgMSG);
							}
						}
					}
				}

				if(message.equals("!poke"))
				{
					api.sendChannelMessage("Command: !poke name nachricht");
				}
				else
				{
					if(message.startsWith("!poke "))
					{
						String[] subString = message.split(" ",3);
						String befehl = subString[0];
						clientName = subString[1];
						System.out.println("clientName: " +clientName);
						String msgMSG = subString[2];

						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							
							if(apiClientName.equals(clientName))
							{
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.pokeClient(clientID,clientName + " hat dich angestupst: " + msgMSG);
								api.sendPrivateMessage(clientID,clientName + " hat dich angestupst: " + msgMSG);
							}
						}
					}
				}
				if(message.equals("!spam"))
				{
					api.sendPrivateMessage(e.getInvokerId(),"Command: !spam Anzahl nachricht");
				}
				else
				{
					if(message.startsWith("!spam "))
					{
						String[] subString = message.split(" ", 3);
						String befehl = subString[0];
						String anzahl_S = subString[1];
						String nachricht = subString[2];
						int anzahl_I = Integer.parseInt(anzahl_S);
						
						for(int i=0;i<anzahl_I;i++){
							System.out.println("spam" + i);
							api.sendServerMessage(nachricht);
						}
					}	
				}
				if(message.equals("!cspam"))
				{
					api.sendPrivateMessage(e.getInvokerId(),"Command: !cspam Anzahl nachricht");
				}
				else
				{
					if(message.startsWith("!cspam "))
					{
						String[] subString = message.split(" ", 3);
						String befehl = subString[0];
						String anzahl_S = subString[1];
						String nachricht = subString[2];
						int anzahl_I = Integer.parseInt(anzahl_S);
						
						for(int i=0;i<anzahl_I;i++){
							System.out.println("spam" + i);
							api.sendChannelMessage(nachricht);
						}
					}	
				}
				if(message.equals("!msgme")){
					api.sendPrivateMessage(e.getInvokerId(), "Hallo :D");
				}
				if(e.getTargetMode() == TextMessageTargetMode.CLIENT)
				{

					if(message.equals("!komm"))
					{
						for(Client client : api.getClients())
						{
							int apiClientID = client.getId();
							int clientID = e.getInvokerId();
							
							if(clientID==apiClientID)
							{
								int channelID = client.getChannelId();
								System.out.println("ClientID: " + clientID);
								api.moveClient(botID, channelID);
							}
						}
					}
					
					if(message.startsWith("!rename ")){
						String[] subString = message.split(" ",2);
						String befehl = subString[0];
						String nickname = subString[1];
						api.setNickname(nickname);
					}
					
					if(message.equals("!stop")||message.equals("!quit")||message.equals("!botquit"))
					{
						System.exit(0);
					}

					if(message.equals("!ban"))
					{
						api.sendPrivateMessage(e.getInvokerId(),"Command: !ban name zeit grund");
					}
					else
					{
						if(message.startsWith("!ban "))
						{
							String[] subString = message.split(" ",4);
							String befehl = subString[0];
							clientName = subString[1];
							System.out.println("clientName: " +clientName);
							String banZeit = subString[2];
							String banGrund = subString[3];
							
							if(banGrund.isEmpty())
							{
								banGrund = "Du wurdest gebannt!";
							}
							long BanZeit=Long.parseLong(banZeit);

							for(Client client : api.getClients())
							{
								System.out.println("apiClientName: " + client.getNickname());
								String apiClientName = client.getNickname();
								
								if(apiClientName.equals(clientName))
								{
									int clientID=client.getId();
									System.out.println("ClientID: " + clientID);
									api.banClient(clientID, BanZeit, banGrund);
									api.sendChannelMessage(clientName + " wurde gebannt!");
								}
							}
						}
					}

					if(message.equals("!ban*"))
					{
						api.sendPrivateMessage(e.getInvokerId(),"Command: !ban name zeit grund");
					}
					else
					{
						if(message.startsWith("!ban* "))
						{
							String[] subString = message.split(" ",4);
							String befehl = subString[0];
							clientName = subString[1];
							System.out.println("clientName: " +clientName);
							String banZeit = subString[2];
							String banGrund = subString[3];
							
							if(banGrund.isEmpty())
							{
								banGrund = "Du wurdest gebannt!";
							}
							long BanZeit=Long.parseLong(banZeit);

							for(Client client : api.getClients())
							{
								System.out.println("apiClientName: " + client.getNickname());
								String apiClientName = client.getNickname();
								
								if(apiClientName.startsWith(clientName))
								{
									int clientID=client.getId();
									System.out.println("ClientID: " + clientID);
									api.banClient(clientID, BanZeit, banGrund);
									api.sendChannelMessage(clientName + " wurde gebannt!");
								}
							}
						}
					}
					

				if(message.equals("!votestart"))
				{
					api.sendPrivateMessage(e.getInvokerId(),"Command: !votestart name möglichkeit1 möglichkeit2 möglichkeit3 möglichkeit4");
				}
				else
				{
					if(message.startsWith("!votestart "))
					{
						String[] subString = message.split(" ",0);
						String befehl = subString[0];
						vote = subString[1];
						
						if(subString[2].isEmpty())
						{
							subString[2]=" ";
						}
						vote_m1 = subString[2];
						if(subString[3].isEmpty())
						{
							subString[3]=" ";
						}
						vote_m2 = subString[3];
						if(subString[4].isEmpty())
						{
							subString[4]=" ";
						}
						vote_m3 = subString[4];
						if(subString[5].isEmpty())
						{
							subString[5]=" ";
						}
						vote_m4 = subString[5];
						api.sendServerMessage("votet mit !vote (votemöglichkeit) für: " + vote + "  " + vote_m1 + "  "  + vote_m2 + "  "  + vote_m3 + "  "  + vote_m4);
						api.sendServerMessage("antwortet im private chat (rechtsklick auf den bot und dann: text chat öffnen)");
					}
				}
				
				if(message.startsWith("!vote "))
				{
					String[] subString = message.split(" ",2);
					String befehl = subString[0];
					String vote = subString[1];
					
					if(vote.equals(vote_m1))
					{
						a_vote_m1 +=1;
					}
					if(vote.equals(vote_m2))
					{
						a_vote_m2 +=1;
					}
					if(vote.equals(vote_m3))
					{
						a_vote_m3 +=1;
					}
					if(vote.equals(vote_m4))
					{
						a_vote_m4 +=1;
					}
				}
				

				if(message.equals("!voteend"))
				{
					api.sendPrivateMessage(e.getInvokerId(), "vote ergebnisse für: " + vote);
					api.sendPrivateMessage(e.getInvokerId(), vote_m1 + ": " + a_vote_m1);
					api.sendPrivateMessage(e.getInvokerId(), vote_m2 + ": " + a_vote_m2);
					api.sendPrivateMessage(e.getInvokerId(), vote_m3 + ": " + a_vote_m3);
					api.sendPrivateMessage(e.getInvokerId(), vote_m4 + ": " + a_vote_m4);
					int a_vote_gewinner = Math.max(Math.max(Math.max(a_vote_m1, a_vote_m2), a_vote_m3), a_vote_m4);
					String vote_gewinner = "";
					
					if(a_vote_gewinner == a_vote_m1)
					{
						vote_gewinner = vote_m1;
					}
					if(a_vote_gewinner == a_vote_m2)
					{
						vote_gewinner = vote_m2;
					}
					if(a_vote_gewinner == a_vote_m3)
					{
						vote_gewinner = vote_m3;
					}
					if(a_vote_gewinner == a_vote_m4)
					{
						vote_gewinner = vote_m4;
					}
					int serverID = api.getServerIdByPort(12200);
					api.sendPrivateMessage(e.getInvokerId(), "vote gewinner: " + vote_gewinner + " mit: " + a_vote_gewinner + " Stimmen!");
					api.sendServerMessage("vote ist beendet!");
					api.sendServerMessage("vote gewinner: " + vote_gewinner + " mit: " + a_vote_gewinner + " Stimmen!");
					a_vote_m1 = 0;
					a_vote_m2 = 0;
					a_vote_m3 = 0;
					a_vote_m4 = 0;
				}
				if(message.equals("!getallwarns")){
					for(int i = 0;i<alWarnungen.size();i++){
						api.sendPrivateMessage(e.getInvokerId(),"" + alWarnungen.get(i));
					}
				}
				if(message.equals("!updatewarns")){
					alWarnungen = io.github.awsdcrafting.WarnSystem.leseWarnungenAsArrayList();
					for(int i = 0;i<alWarnungen.size();i++){
						api.sendPrivateMessage(e.getInvokerId(),"" + alWarnungen.get(i));
					}
				}
				if(message.equals("!warn")){
					api.sendPrivateMessage(e.getInvokerId(),"Command: !warn name [anzahlwarnungen]");
				}
				else {
					if(message.startsWith("!warn ")){
					int anzahlWarnungen = 0;
					String[] subString = message.split(" ",3);
					String befehl = subString[0];
					String name = subString[1];
					String clientUID;
					String clientUID_ = "fehlt";;
					if(subString.length==2){
						subString = new String[3];
						subString[0] = befehl;
						subString[1] = name;
						subString[2] = "1";
					}
					boolean warnungExistiertNicht = true;
					anzahlWarnungen = Integer.parseInt(subString[2]);
					boolean clientUIDErhalten = false;
					for(Client client : api.getClients())
					{
						System.out.println("apiClientName: " + client.getNickname());
						String apiClientName = client.getNickname();
						
						if(apiClientName.equals(name))
						{
							clientUID=client.getUniqueIdentifier();
							clientUID_ = clientUID;
							clientUIDErhalten = true;
						}
						if(!clientUIDErhalten){
							for(int i = 0;i<alWarnungen.size();i++){
							String[] alWarnungenSubString = alWarnungen.get(i).toString().split(" ");
							if(alWarnungenSubString[1].equals(name)){
							if(!alWarnungenSubString[0].equals("fehlt")){
							clientUID_ = alWarnungenSubString[0];
							clientUIDErhalten = true;
							}
							}
							}
						}
					}
					clientUID = clientUID_;
					for(int i = 0;i<alWarnungen.size();i++){
						String[] alWarnungenSubString = alWarnungen.get(i).toString().split(" ");
						if(name.equals(alWarnungenSubString[1])||clientUID.equals(alWarnungenSubString[0])){
							int alAnzahlWarnungen = Integer.parseInt(alWarnungenSubString[2]);
							anzahlWarnungen += alAnzahlWarnungen;
							alWarnungen.set(i,clientUID + " " + name + " " + anzahlWarnungen);
							warnungExistiertNicht = false;
						}
					}
					if(warnungExistiertNicht){
						alWarnungen.add(clientUID + " " + name + " " + anzahlWarnungen);
					}
					warnungExistiertNicht = true;
					io.github.awsdcrafting.WarnSystem.SchreibeWarnung(alWarnungen);
					if(anzahlWarnungen==1){
						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							String apiClientUID = client.getUniqueIdentifier();
							if(apiClientName.equals(name)||apiClientUID.equals(clientUID))
							{
								String kickGrund = "1. Verwarnung";
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromServer(kickGrund, clientID);
							}
						}
					}
					if(anzahlWarnungen>=2){
						boolean gebannt = false;
						for(Client client : api.getClients())
						{
							System.out.println("apiClientName: " + client.getNickname());
							String apiClientName = client.getNickname();
							String apiClientUID = client.getUniqueIdentifier();
							if(apiClientName.equals(name)||apiClientUID.equals(clientUID))
							{
								String banGrund = anzahlWarnungen + ". Verwarnung";
								long banZeit = 3600 * (int)Math.pow(2, anzahlWarnungen-1);
								if(banZeit >2678400){
									banZeit = 0;
								}
								int clientID=client.getId();
								System.out.println("ClientID: " + clientID);
								api.banClient(clientID, banZeit, banGrund);
								gebannt = true;
							}
						}
						if(!gebannt){
							String banGrund = anzahlWarnungen + ". Verwarnung";
							long banZeit = 3600 * (int)Math.pow(2, anzahlWarnungen-1);
							if(banZeit >2678400){
								banZeit = 0;
							}
							api.addBan("", name, clientUID, banZeit, banGrund);
						}
					}
					
					}
				}
				if(message.equals("!removewarn")){
					api.sendPrivateMessage(e.getInvokerId(),"Command: !removewarn name [anzahlwarnungen]");
				}
				else {
					if(message.startsWith("!removewarn ")){
					int anzahlWarnungen = 0;
					String[] subString = message.split(" ",3);
					String befehl = subString[0];
					String name = subString[1];
					String clientUID = "";
					if(subString.length==2){
						subString = new String[3];
						subString[0] = befehl;
						subString[1] = name;
						subString[2] = "1";
					}
					boolean warnungExistiertNicht = true;
					anzahlWarnungen = Integer.parseInt(subString[2]);
					for(int i = 0;i<alWarnungen.size();i++){
						String[] alWarnungenSubString = alWarnungen.get(i).toString().split(" ");
						if(name.equals(alWarnungenSubString[1])){
							clientUID = alWarnungenSubString[0];
							int alAnzahlWarnungen = Integer.parseInt(alWarnungenSubString[2]);
							if(anzahlWarnungen >= alAnzahlWarnungen){
								anzahlWarnungen = 0;
								alWarnungen.remove(i);
							}
							else{
							anzahlWarnungen = alAnzahlWarnungen - anzahlWarnungen;
							alWarnungen.set(i,clientUID + " " + name + " " + anzahlWarnungen);
							}
							warnungExistiertNicht = false;
						}
					}
					if(warnungExistiertNicht){
						api.sendPrivateMessage(e.getInvokerId(), name + " hat keine Verwarnungen!");
					}
					warnungExistiertNicht = true;
					io.github.awsdcrafting.WarnSystem.SchreibeWarnung(alWarnungen);
					}
				}
				
				
			}
		}
	}
}
}
