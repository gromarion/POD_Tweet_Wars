package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

public class TweetReceivedNotifier extends Thread {

	private GamePlayer player;
	private GameMaster master;

	private static final int TWEETS_AMOUNT = 100;

	public TweetReceivedNotifier(GamePlayer player, GameMaster master) {
		this.player = player;
		this.master = master;
	}

	public void run() {
		while (true) {
			for (Status tweet : fetch_received_tweets(TWEETS_AMOUNT)) {
				try {
					master.tweetReceived(player, tweet);
				} catch (RemoteException e) {
					System.out.println("Something went wrong (ID: "
							+ tweet.getId() + "): '" + tweet.getText()
							+ "' from " + tweet.getSource());
				}
			}
		}
	}

	private Status[] fetch_received_tweets(int amount) {
		return TweetsRepository.get_instance().fetch_valid_players_tweets(
				amount);
	}
}
