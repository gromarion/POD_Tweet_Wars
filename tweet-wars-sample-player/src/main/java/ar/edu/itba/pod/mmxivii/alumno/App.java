package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class App extends ReceiverAdapter {

	public static final String TWEETS_PROVIDER_NAME = "tweetsProvider";
	public static final String GAME_MASTER_NAME = "gameMaster";
	private JChannel channel;
	private TweetsRepository repo;

	public App(String cluster_name) throws Exception {
		this.channel = new JChannel();
		this.channel.setReceiver(this);
		this.channel.connect(cluster_name);
	}

	public JChannel fetch_channel() {
		return this.channel;
	}
	
	public void viewAccepted(View new_view) {
		System.out.print("\n** view: " + new_view);
	}

	public void receive(Message msg) {
		if (msg.getObject() instanceof Status)
			repo.add_player_tweet((Status) msg.getObject());
	}

	public static void main(String[] args) {
		final GamePlayer player = new GamePlayer(args[2], args[3]);
		final String player_hash = "" + player.hashCode();
		try {
			final Registry registry = connect_to_server(args[0], args[1]);
			final TweetsProvider tweets_provider = fetch_tweets_provider(registry);
			final GameMaster master = fetch_game_master(registry);

			register_player(master, player, player_hash);

			try {
				App app = new App(args[4]);
				new FakeTweetsGenerator(app.fetch_channel(), player,
						player_hash).start();
			} catch (Exception e) {
				System.out
						.println("Something wrong happened while creating the JChannel");
				e.printStackTrace();
			}
			new TweetsFetcher(player, player_hash, tweets_provider).start();
			new TweetReceivedNotifier(player, master).start();
			new FakeTweetsReporter(player, master, tweets_provider).start();

		} catch (RemoteException | NotBoundException e) {
			System.err.println("App Error: " + e.getMessage());
			System.exit(-1);
		}
	}

	private static void register_player(GameMaster master, GamePlayer player,
			String player_hash) throws RemoteException {
		try {
			master.newPlayer(player, player_hash);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private static Registry connect_to_server(String host, String port)
			throws RemoteException {
		return LocateRegistry.getRegistry(host, Integer.parseInt(port));
	}

	private static TweetsProvider fetch_tweets_provider(Registry registry)
			throws AccessException, RemoteException, NotBoundException {
		return (TweetsProvider) registry.lookup(TWEETS_PROVIDER_NAME);
	}

	private static GameMaster fetch_game_master(Registry registry)
			throws AccessException, RemoteException, NotBoundException {
		return (GameMaster) registry.lookup(GAME_MASTER_NAME);
	}
}
