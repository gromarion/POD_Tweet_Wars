package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;

public class FakeTweetsReporter {

	private static final int MAX_REPORTED_TWEET_AMOUNT = 10;
	private GamePlayer player;
	private GameMaster master;
	private TweetsProvider tweet_provider;

	public FakeTweetsReporter(GamePlayer player, GameMaster master,
			TweetsProviderImpl tweet_provider) {
		this.player = player;
		this.master = master;
		this.tweet_provider = tweet_provider;
	}

	public void run() {
		while (true) {
			Status[] false_tweets = fetch_fake_tweets(TweetsRepository
					.get_instance().fetch_tweets(MAX_REPORTED_TWEET_AMOUNT));
			try {
				master.reportFake(player, false_tweets);
			} catch (RemoteException e) {
				System.out
						.println("Something wrong happened while reporting a fake tweet");
				e.printStackTrace();
			}
		}
	}

	private Status[] fetch_fake_tweets(Status[] tweets) {
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
