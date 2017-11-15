package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.*;
import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

import io.github.awsdcrafting.commands.CommandManager;
import io.github.awsdcrafting.configSystem.ConfigManager;
import io.github.awsdcrafting.configSystem.DefaultConfig;
import io.github.awsdcrafting.configSystem.Store;
import io.github.awsdcrafting.ui.Fenster;
import io.github.awsdcrafting.utils.WarnSystem;

public class Main
{
	public static final int SIZE_X = 600;
	public static final int SIZE_Y = 500;
	private Fenster fenster;
	public static volatile int botIDm;
	static int DebugLevel = 4;
	static String clientName;
	static String vote;
	static ArrayList<String> votes = new ArrayList<String>();
	static ArrayList<Integer> a_votes = new ArrayList<Integer>();

	//move to store
	public static ArrayList<Integer> notInvited;
	static ArrayList<String> alWarnungen;
	static ArrayList<String> commands;
	static ArrayList<Integer> guestIDS;

	static ArrayList<String> altsAL = io.github.awsdcrafting.utils.DateiLeser.leseDateiAsArrayList("alts.txt");
	static ArrayList<String> usedAltsAL = new ArrayList<String>();
	public static Main MAIN;
	static volatile boolean funktionAktiv;

	public static boolean sendServer = false;

	public static Store store;

	public static CommandManager commandManager;
	public static ConfigManager configManager;
	public static ArrayList<String> adminIDS;
	public static ArrayList<Integer> adminGroups = new ArrayList<Integer>();
	public static ArrayList<Integer> modGroups = new ArrayList<Integer>();

	public Main()
	{
		if (MAIN == null)
		{
			MAIN = this;
		}
		commandManager = new CommandManager();
		configManager = new ConfigManager();

		store = new Store();
		guestIDS = new ArrayList<>();
		notInvited = new ArrayList<>();

		//fenster
		fenster = new Fenster("TS3Bot - by scisneromam", SIZE_X, SIZE_Y);

		fenster.setResizable(true);
		fenster.setDefaultCloseOperation(fenster.EXIT_ON_CLOSE);
		fenster.initialize();
	}

	public static InputStream gibResourceStream(String resource)
	{
		return MAIN.getClass().getResourceAsStream(resource);
	}

	public static void setConfig()
	{
		System.out.println("config");
		for (int i = 0; i < configManager.getConfig().size(); i++)
		{
			switch (configManager.getConfig().get(i).split("=")[0])
			{
			case "admin_groups":
				System.out.println("admin_Groups");
				String[] groups = configManager.getConfig().get(i).split("=")[1].split(",");
				for (int x = 0; x < groups.length; x++)
				{
					adminGroups.add(Integer.parseInt(groups[x]));
				}
				break;

			case "test":
				System.out.println("test-config");
			default:
				break;
			}
		}
	}

	public static void main(String[] args)
	{
		if (MAIN == null)
		{
			MAIN = new Main();
		}
		File configDir = new File("Config");
		if (!configDir.exists())
		{
			configDir.mkdir();
		}
		File configFile = new File("Config/Config.cfg");
		if (!configFile.exists())
		{
			try
			{
				configFile.createNewFile();
				configManager.setDefaultConfig();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		configManager.load();
		setConfig();

		File asciiDir = new File("ascii");
		if (!asciiDir.exists())
		{
			asciiDir.mkdir();
		}

		File textDir = new File("Text");
		if (!textDir.exists())
		{
			textDir.mkdir();
		}

		final TS3Config tsconfig = new TS3Config();
		tsconfig.setHost("31.214.227.53"); // Die IP-Adresse des Servers, ohne
		// Port
		tsconfig.setFloodRate(FloodRate.UNLIMITED);
		tsconfig.setDebugLevel(Level.ALL);
		commands = io.github.awsdcrafting.utils.DateiLeser.leseDateiAsArrayList("commands.txt");
		adminIDS = io.github.awsdcrafting.utils.DateiLeser.leseDateiAsArrayList("admins.txt");

		// Use default exponential backoff reconnect strategy
		tsconfig.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());

		// Make stuff run every time the query (re)connects
		tsconfig.setConnectionHandler(new ConnectionHandler()
		{

			@Override
			public void onConnect(TS3Query ts3Query)
			{
				stuffThatNeedsToRunEveryTimeTheQueryConnects(ts3Query.getApi());
			}

			@Override
			public void onDisconnect(TS3Query ts3Query)
			{
				// nothing
			}
		});
		final TS3Query query = new TS3Query(tsconfig);
		query.connect(); // Verbinden

		// Register the event listener
		final BotTS3EventAdapter adapter = new BotTS3EventAdapter(query.getApi(), botIDm, query);
		query.getApi().addTS3Listeners(adapter);
		alWarnungen = io.github.awsdcrafting.utils.WarnSystem.leseWarnungenAsArrayList();

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				query.getApi().logout();
				query.exit();
			}
		});
	}

	public static void stuffThatNeedsToRunEveryTimeTheQueryConnects(TS3Api api)
	{
		// Logging in, selecting the virtual server, selecting a channel
		// and setting a nickname needs to be done every time we reconnect
		api.login("scissiV2", "ziAz99n2");
		api.selectVirtualServerByPort(12200);
		api.moveQuery(52360);
		String name = "BOT";
		for (Client client : api.getClients())
		{
			System.out.println("apiClientName: " + client.getNickname());
			String apiClientName = client.getNickname();

			if (apiClientName.equals(name))
			{
				String fname = "";
				try
				{
					fname = ((int) (Integer.parseInt(name.substring(3, apiClientName.length())) + 1)) + "";
				} catch (NumberFormatException e)
				{
					fname = "2";
				}

				name = "BOT" + fname;
			}

		}

		api.setNickname(name);

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

		public void onClientJoin(ClientJoinEvent e)
		{

			if (e.getClientServerGroups().contains("21782"))
			{
				guestIDS.add(e.getClientId());
				if (store.getJoinMode().equalsIgnoreCase("inviteOnly"))
				{
					notInvited.add(e.getClientId());
					api.sendPrivateMessage(e.getClientId(),
										   "Um dich zu registrieren schreibe den bot mit !join <invitecode> an den invitecode erhältst du von der person die dich auf den ts invited hat");
				}
			}

		}

		@Override
		public void onClientLeave(ClientLeaveEvent e)
		{
			guestIDS.remove(new Integer(e.getClientId()));

			notInvited.remove(new Integer(e.getClientId()));
		}

		@Override
		public void onClientMoved(ClientMovedEvent e)
		{
			System.out.println("moved");
			if (store.getJoinMode().equalsIgnoreCase("inviteOnly"))
			{
				for (int i = 0; i < notInvited.size(); i++)
				{
					System.out.println(notInvited.get(i) + ":" + e.getClientId());
					if ((int) notInvited.get(i) == e.getClientId())
					{
						System.out.println("test-vorher");
						notInvited.remove(new Integer(e.getClientId()));
						System.out.println("test");
						api.kickClientFromServer("Der Server ist momentan im invite only mode.", e.getClientId());
						System.out.println("test-nachher");
					} else
					{
						System.out.println("Fail");
					}
				}
			}
		}

		@Override
		public void onTextMessage(TextMessageEvent e)
		{

			if (e.getInvokerId() != botID)
			{
				String message = e.getMessage();
				if (DebugLevel == 4)
				{
					clientName = e.getInvokerName();
					System.out.println(clientName + ": " + message);
				}
				if (message.startsWith("!"))
				{
					if (DebugLevel >= 3)
					{
						System.out.println("" + message);
					}
				}
				commandManager.executeTextMessageEvent(api, e, message);

				if (e.getTargetMode() != TextMessageTargetMode.SERVER)
				{

					if (message.equals("!ckick*"))
					{
						api.sendPrivateMessage(e.getInvokerId(), "Command: !ckick name grund");
					} else
					{
						if (message.startsWith("!ckick* "))
						{
							String[] subString = message.split(" ", 3);
							String befehl = subString[0];
							clientName = subString[1];
							System.out.println("clientName: " + clientName);
							String kickGrund = subString[2];

							if (kickGrund.isEmpty() || kickGrund == "" || kickGrund == " ")
							{
								kickGrund = "Du wurdest gekickt!";
							}

							for (Client client : api.getClients())
							{
								System.out.println("apiClientName: " + client.getNickname());
								String apiClientName = client.getNickname();

								if (apiClientName.startsWith(clientName))
								{
									int clientID = client.getId();
									System.out.println("ClientID: " + clientID);
									api.kickClientFromChannel(kickGrund, clientID);
									api.sendChannelMessage(clientName + " wurde gekickt!");
								}
							}
						}
					}

					if (message.equals("!ascii"))
					{
						api.sendPrivateMessage(e.getInvokerId(), "Command: !ascii .txt Datei");
					} else
					{
						if (message.startsWith("!ascii "))
						{
							funktionAktiv = true;
							String[] subString = message.split(" ", 2);
							String befehl = subString[0];
							String datei = subString[1];
							ArrayList<String> aAL = io.github.awsdcrafting.utils.DateiLeser.leseDateiAsArrayList("ascii/" + datei);
							for (int i = 0; i < aAL.size(); i++)
							{
								System.out.println("spam");
								api.sendChannelMessage(aAL.get(i));
								if (!funktionAktiv)
								{
									break;
								}
							}
						}
					}
					if (message.equals("!altgen"))
					{
						api.sendPrivateMessage(e.getInvokerId(), "Command: !altgen amount");
					} else
					{
						if (message.startsWith("!altgen "))
						{
							funktionAktiv = true;
							String[] subString = message.split(" ", 2);
							String befehl = subString[0];
							int amount = 0;
							if (subString[1].equalsIgnoreCase("all"))
							{
								amount = altsAL.size();
							} else
							{
								amount = Integer.parseInt(subString[1]);
							}
							for (int i = 0; i < amount; i++)
							{
								System.out.println("spam");
								if (altsAL.size() > 0)
								{
									if (usedAltsAL.contains(altsAL.get(0)))
									{
										altsAL.remove(0);
										i--;
									} else
									{
										api.sendChannelMessage(altsAL.get(0));
										usedAltsAL.add(altsAL.get(0));
										io.github.awsdcrafting.utils.DateiSchreiber.schreibeArrayList(usedAltsAL, "usedAlts.txt");
										altsAL.remove(0);
									}
								} else
								{
									api.sendChannelMessage("no alts left! :C");
								}
								if (!funktionAktiv)
								{
									break;
								}
							}
						}
						io.github.awsdcrafting.utils.DateiSchreiber.schreibeArrayList(usedAltsAL, "usedAlts.txt");
					}
					if (message.equalsIgnoreCase("!alts"))
					{
						api.sendChannelMessage(altsAL.size() + " alts left!");
					}
					if (message.equalsIgnoreCase("!generatedAlts") || message.equalsIgnoreCase("!usedAlts"))
					{
						api.sendChannelMessage(usedAltsAL.size() + " alts generated!");
					}
					if (message.equals("!stop"))
					{
						if (!funktionAktiv && e.getTargetMode() == TextMessageTargetMode.CLIENT)
						{
							System.exit(0);
						} else
						{
							funktionAktiv = false;
						}
					}

					if (e.getTargetMode() == TextMessageTargetMode.CLIENT)
					{
						if (message.equals("!adminGroups"))
						{
							String ausgabe = "";
							for (int i = 0; i < adminGroups.size(); i++)
							{
								ausgabe += adminGroups.get(i);
							}
							if (ausgabe.equals(""))
							{
								api.sendPrivateMessage(e.getInvokerId(), "nothing :C");
							}
							api.sendPrivateMessage(e.getInvokerId(), ausgabe);
						}
						if (message.equals("!getConfig"))
						{
							String ausgabe = "";
							for (int i = 0; i < configManager.getConfig().size(); i++)
							{
								ausgabe += configManager.getConfig().get(i);
							}
							if (ausgabe.equals(""))
							{
								api.sendPrivateMessage(e.getInvokerId(), "nothing :C");
							}
							api.sendPrivateMessage(e.getInvokerId(), ausgabe);
						}

						if (message.equals("!komm"))
						{
							for (Client client : api.getClients())
							{
								int apiClientID = client.getId();
								int clientID = e.getInvokerId();

								if (clientID == apiClientID)
								{
									int channelID = client.getChannelId();
									System.out.println("ClientID: " + clientID);
									api.moveClient(botID, channelID);
								}
							}
						}

						if (message.startsWith("!rename "))
						{
							String[] subString = message.split(" ", 2);
							String befehl = subString[0];
							String nickname = subString[1];
							api.setNickname(nickname);
						}

						if (message.equals("!ban"))
						{
							api.sendPrivateMessage(e.getInvokerId(), "Command: !ban name zeit grund");
						} else
						{
							if (message.startsWith("!ban "))
							{
								String[] subString = message.split(" ", 4);
								String befehl = subString[0];
								clientName = subString[1];
								System.out.println("clientName: " + clientName);
								String banZeit = subString[2];
								String banGrund = subString[3];

								if (banGrund.isEmpty())
								{
									banGrund = "Du wurdest gebannt!";
								}
								long BanZeit = Long.parseLong(banZeit);

								for (Client client : api.getClients())
								{
									System.out.println("apiClientName: " + client.getNickname());
									String apiClientName = client.getNickname();

									if (apiClientName.equals(clientName))
									{
										int clientID = client.getId();
										System.out.println("ClientID: " + clientID);
										api.banClient(clientID, BanZeit, banGrund);
										api.sendChannelMessage(clientName + " wurde gebannt!");
									}
								}
							}
						}

						if (message.equals("!ban*"))
						{
							api.sendPrivateMessage(e.getInvokerId(), "Command: !ban name zeit grund");
						} else
						{
							if (message.startsWith("!ban* "))
							{
								String[] subString = message.split(" ", 4);
								String befehl = subString[0];
								clientName = subString[1];
								System.out.println("clientName: " + clientName);
								String banZeit = subString[2];
								String banGrund = subString[3];

								if (banGrund.isEmpty())
								{
									banGrund = "Du wurdest gebannt!";
								}
								long BanZeit = Long.parseLong(banZeit);

								for (Client client : api.getClients())
								{
									System.out.println("apiClientName: " + client.getNickname());
									String apiClientName = client.getNickname();

									if (apiClientName.startsWith(clientName))
									{
										int clientID = client.getId();
										System.out.println("ClientID: " + clientID);
										api.banClient(clientID, BanZeit, banGrund);
										api.sendChannelMessage(clientName + " wurde gebannt!");
									}
								}
							}
						}

						if (message.equals("!votestart"))
						{
							api.sendPrivateMessage(e.getInvokerId(), "Command: !votestart name m�glichkeit1 m�glichkeit2 m�glichkeit3 m�glichkeit4");
						} else
						{
							if (message.startsWith("!votestart "))
							{
								String[] subString = message.split(" ", 0); // f�rs
								// n�chste
								// update:
								// beliebig
								// viele
								// m�glichkeiten
								// sollen
								// m�glich
								// sein
								String befehl = subString[0];
								vote = subString[1];

								for (int i = 2; i < subString.length; i++)
									votes.add(subString[i].replace("_", " "));
								a_votes.add(0);
							}
							vote = vote.replace("_", " ");
							for (Client client : api.getClients())
							{
								int apiClientID = client.getId();

								if (botID == apiClientID)
								{
									int channelID = client.getChannelId();
									String channelName = api.getChannelInfo(channelID).getName();
									api.sendServerMessage("Der BOT h�lt sich im: " + channelName + " channel auf!");
								}
							}
							String vote_message = vote;
							for (int i = 0; i < votes.size(); i++)
							{
								vote_message += (" " + votes.get(i));
							}
							api.sendServerMessage("votet mit !vote (votem�glichkeit) f�r: " + vote_message);
							api.sendServerMessage("votet entweder indem ihr den name der m�glichkeit eingebt oder indem ihr vote1/vote2/vote3/vote4 eingebt");
							api.sendServerMessage(
									"antwortet im private chat (rechtsklick auf den bot und dann: text chat �ffnen oder schreibt im serverchat oder channelchat: !msgme)");

						}

						if (message.startsWith("!vote "))
						{
							String[] subString = message.split(" ", 2);
							String befehl = subString[0];
							String vote = subString[1];

							for (int i = 0; i < votes.size(); i++)
							{
								if ((vote.equals(votes.get(i))) || (vote.equals("vote" + (i + 1))))
								{
									a_votes.set(i, a_votes.get(i) + 1);
								}
							}

						}

						if (message.equals("!voteend"))
						{
							api.sendPrivateMessage(e.getInvokerId(), "vote ergebnisse f�r: " + vote);
							for (int i = 0; i < votes.size(); i++)
							{
								api.sendPrivateMessage(e.getInvokerId(), votes.get(i) + ": " + a_votes.get(i));
							}
							int a_vote_gewinner = Collections.max(a_votes);

							String vote_gewinner = "";

							for (int i = 0; i < votes.size(); i++)
							{
								if (a_vote_gewinner == a_votes.get(i))
								{
									vote_gewinner = votes.get(i);
								}
							}

							int serverID = api.getServerIdByPort(12200);
							String vote_message = "";
							if (a_vote_gewinner == 1)
							{
								vote_message = "vote gewinner: " + vote_gewinner + " mit: " + a_vote_gewinner + " Stimme!";
							} else
							{
								vote_message = "vote gewinner: " + vote_gewinner + " mit: " + a_vote_gewinner + " Stimmen!";
							}
							api.sendPrivateMessage(e.getInvokerId(), vote_message);
							api.sendServerMessage("vote ist beendet!");
							api.sendServerMessage("vote ergebnisse f�r: " + vote);
							api.sendServerMessage(vote_message);
						}
						if (message.equals("!getallwarns"))
						{
							for (int i = 0; i < alWarnungen.size(); i++)
							{
								api.sendPrivateMessage(e.getInvokerId(), "" + alWarnungen.get(i));
							}
						}
						if (message.equals("!updatewarns"))
						{
							alWarnungen = io.github.awsdcrafting.utils.WarnSystem.leseWarnungenAsArrayList();
							for (int i = 0; i < alWarnungen.size(); i++)
							{
								api.sendPrivateMessage(e.getInvokerId(), "" + alWarnungen.get(i));
							}
						}
						if (message.equals("!warn"))
						{
							api.sendPrivateMessage(e.getInvokerId(), "Command: !warn name [anzahlwarnungen]");
						} else
						{
							if (message.startsWith("!warn "))
							{
								int anzahlWarnungen = 0;
								String[] subString = message.split(" ", 3);
								String befehl = subString[0];
								String name = subString[1];
								String clientUID;
								String clientUID_ = "fehlt";
								;
								if (subString.length == 2)
								{
									subString = new String[4];
									subString[0] = befehl;
									subString[1] = name;
									subString[2] = "";
									subString[3] = "1";
								}
								String grund = subString[2];
								if (subString.length == 3)
								{
									subString = new String[4];
									subString[0] = befehl;
									subString[1] = name;
									subString[2] = grund;
									subString[3] = "1";
								}
								anzahlWarnungen = Integer.parseInt(subString[3]);
								boolean warnungExistiertNicht = true;
								boolean clientUIDErhalten = false;
								for (Client client : api.getClients())
								{
									System.out.println("apiClientName: " + client.getNickname());
									String apiClientName = client.getNickname();

									if (apiClientName.equals(name))
									{
										clientUID = client.getUniqueIdentifier();
										clientUID_ = clientUID;
										clientUIDErhalten = true;
									}
									if (!clientUIDErhalten)
									{
										for (int i = 0; i < alWarnungen.size(); i++)
										{
											String[] alWarnungenSubString = alWarnungen.get(i).toString().split(" ");
											if (alWarnungenSubString[1].equals(name))
											{
												if (!alWarnungenSubString[0].equals("fehlt"))
												{
													clientUID_ = alWarnungenSubString[0];
													clientUIDErhalten = true;
												}
											}
										}
									}
								}
								clientUID = clientUID_;
								for (int i = 0; i < alWarnungen.size(); i++)
								{
									String[] alWarnungenSubString = alWarnungen.get(i).toString().split(" ");
									if (name.equals(alWarnungenSubString[1]) || clientUID.equals(alWarnungenSubString[0]))
									{
										int alAnzahlWarnungen = Integer.parseInt(alWarnungenSubString[2]);
										anzahlWarnungen += alAnzahlWarnungen;
										alWarnungen.set(i, clientUID + " " + name + " " + anzahlWarnungen);
										warnungExistiertNicht = false;
									}
								}
								if (warnungExistiertNicht)
								{
									alWarnungen.add(clientUID + " " + name + " " + anzahlWarnungen);
								}
								warnungExistiertNicht = true;
								io.github.awsdcrafting.utils.WarnSystem.SchreibeWarnung(alWarnungen);
								if (anzahlWarnungen == 1)
								{
									for (Client client : api.getClients())
									{
										System.out.println("apiClientName: " + client.getNickname());
										String apiClientName = client.getNickname();
										String apiClientUID = client.getUniqueIdentifier();
										if (apiClientName.equals(name) || apiClientUID.equals(clientUID))
										{
											String kickGrund = "1. Verwarnung: " + grund;
											int clientID = client.getId();
											System.out.println("ClientID: " + clientID);
											api.kickClientFromServer(kickGrund, clientID);
										}
									}
								}
								if (anzahlWarnungen >= 2)
								{
									boolean gebannt = false;
									for (Client client : api.getClients())
									{
										System.out.println("apiClientName: " + client.getNickname());
										String apiClientName = client.getNickname();
										String apiClientUID = client.getUniqueIdentifier();
										if (apiClientName.equals(name) || apiClientUID.equals(clientUID))
										{
											String banGrund = anzahlWarnungen + ". Verwarnung: " + grund;
											long banZeit = 3600 * (int) Math.pow(2, anzahlWarnungen - 1);
											if (banZeit > 2678400)
											{
												banZeit = 0;
											}
											int clientID = client.getId();
											System.out.println("ClientID: " + clientID);
											api.banClient(clientID, banZeit, banGrund);
											gebannt = true;
										}
									}
									if (!gebannt)
									{
										String banGrund = anzahlWarnungen + ". Verwarnung: " + grund;
										long banZeit = 3600 * (int) Math.pow(2, anzahlWarnungen - 1);
										if (banZeit > 2678400)
										{
											banZeit = 0;
										}
										api.addBan("", name, clientUID, banZeit, banGrund);
									}
								}

							}
						}
						if (message.equals("!removewarn"))
						{
							api.sendPrivateMessage(e.getInvokerId(), "Command: !removewarn name [anzahlwarnungen]");
						} else
						{
							if (message.startsWith("!removewarn "))
							{
								int anzahlWarnungen = 0;
								String[] subString = message.split(" ", 3);
								String befehl = subString[0];
								String name = subString[1];
								String clientUID = "";
								if (subString.length == 2)
								{
									subString = new String[3];
									subString[0] = befehl;
									subString[1] = name;
									subString[2] = "1";
								}
								boolean warnungExistiertNicht = true;
								anzahlWarnungen = Integer.parseInt(subString[2]);
								for (int i = 0; i < alWarnungen.size(); i++)
								{
									String[] alWarnungenSubString = alWarnungen.get(i).toString().split(" ");
									if (name.equals(alWarnungenSubString[1]))
									{
										clientUID = alWarnungenSubString[0];
										int alAnzahlWarnungen = Integer.parseInt(alWarnungenSubString[2]);
										if (anzahlWarnungen >= alAnzahlWarnungen)
										{
											anzahlWarnungen = 0;
											alWarnungen.remove(i);
										} else
										{
											anzahlWarnungen = alAnzahlWarnungen - anzahlWarnungen;
											alWarnungen.set(i, clientUID + " " + name + " " + anzahlWarnungen);
										}
										warnungExistiertNicht = false;
									}
								}
								if (warnungExistiertNicht)
								{
									api.sendPrivateMessage(e.getInvokerId(), name + " hat keine Verwarnungen!");
								}
								warnungExistiertNicht = true;
								io.github.awsdcrafting.utils.WarnSystem.SchreibeWarnung(alWarnungen);
							}
						}
						if (message.equals("!help"))
						{
							for (int i = 0; i < commands.size(); i++)
							{
								String help = commands.get(i);
								api.sendPrivateMessage(e.getInvokerId(), help);
							}
						}

						// alle offenen klammern schliessen
					}

				}
			}
		}
	}
}
