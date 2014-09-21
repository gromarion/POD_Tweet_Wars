package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class TweetsFetcher extends Thread {

	private TweetsProvider tweets_provider;
	private GamePlayer player;
	private String player_hash;
	private TweetsRepository repo;
	private static final int BATCH_SIZE = 10;

	public TweetsFetcher(GamePlayer player, String player_hash,
			TweetsProvider tweets_provider) {
		this.player = player;
		this.player_hash = player_hash;
		this.tweets_provider = tweets_provider;
		this.repo = TweetsRepository.get_instance();
	}

	public void run() {
		while (true) {
			try {
				repo.add_master_tweets(tweets_provider.getNewTweets(player,
						player_hash, BATCH_SIZE));
			} catch (RemoteException e) {
				System.out
						.println("Something went wrong while getting new tweets");
				e.printStackTrace();
			}
		}
	}
}
