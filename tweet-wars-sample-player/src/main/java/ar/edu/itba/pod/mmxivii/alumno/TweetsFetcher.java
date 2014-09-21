package ar.edu.itba.pod.mmxivii.alumno;

import org.jgroups.JChannel;
import org.jgroups.Message;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class TweetsFetcher extends Thread {

	private TweetsProvider tweets_provider;
	private GamePlayer player;
	private String player_hash;
	private static final int BATCH_SIZE = 10;
	private JChannel channel;

	public TweetsFetcher(GamePlayer player, String player_hash,
			TweetsProvider tweets_provider, JChannel channel) {
		this.player = player;
		this.player_hash = player_hash;
		this.tweets_provider = tweets_provider;
		this.channel = channel;
	}

	public void run() {
		while (true) {
			try {
				for (Status tweet : tweets_provider.getNewTweets(player,
						player_hash, BATCH_SIZE)) {
					channel.send(new Message(null, null, tweet));
				}
			} catch (Exception e) {
				System.out
						.println("Something went wrong while getting new tweets");
				e.printStackTrace();
			}
		}
	}
}
