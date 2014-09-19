package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class App {
	
	public static final String TWEETS_PROVIDER_NAME = "tweetsProvider";
	public static final String GAME_MASTER_NAME = "gameMaster";

	public static void main(String[] args) {
		final GamePlayer player = new GamePlayer("51296", "");
		final String hash = "" + player.hashCode();
		try {
			final Registry registry = connect_to_server(args[0], args[1]);
			final TweetsProvider tweetsProvider = fetch_tweets_provider(registry);
			final GameMaster master = fetch_game_master(registry);

			try {
				master.newPlayer(player, hash);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			new TweetReceivedNotifier(player, master).start();

		} catch (RemoteException | NotBoundException e) {
			System.err.println("App Error: " + e.getMessage());
			System.exit(-1);
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
