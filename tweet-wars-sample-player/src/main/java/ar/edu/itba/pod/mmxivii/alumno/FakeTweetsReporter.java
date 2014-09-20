package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class FakeTweetsReporter extends Thread {

	private static final int MAX_REPORTED_TWEET_AMOUNT = 10;
	private GamePlayer player;
	private GameMaster master;
	private TweetsProvider tweet_provider;

	public FakeTweetsReporter(GamePlayer player, GameMaster master,
			TweetsProvider tweet_provider) {
		this.player = player;
		this.master = master;
		this.tweet_provider = tweet_provider;
	}

	public void run() {
		while (true) {
			Status[] false_tweets = fetch_fake_tweets();
			try {
				if (false_tweets.length > 0)
					master.reportFake(player, false_tweets);
			} catch (RemoteException e) {
				System.out
						.println("Something wrong happened while reporting a fake tweet");
				e.printStackTrace();
			}
		}
	}

	private Status[] fetch_fake_tweets() {
		TweetsRepository repo = TweetsRepository.get_instance();
		Status[] tweets = repo.fetch_players_tweets(
				MAX_REPORTED_TWEET_AMOUNT);
		if (tweets.length == 0)
			return new Status[0];
		List<Status> ans = new ArrayList<Status>();
		for (Status status : tweets) {
			try {
				if (tweet_provider.getTweet(status.getId()) == null)
					ans.add(status);
			} catch (RemoteException e) {
				System.out
						.println("Something wrong happened while checking a tweet");
				e.printStackTrace();
			}
		}
		return (Status[]) ans.toArray();
	}
}
