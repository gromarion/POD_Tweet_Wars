package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;
import java.util.List;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class FakeTweetsReporter extends Thread {

	private GamePlayer player;
	private GameMaster master;
	private TweetsProvider tweet_provider;
	private TweetsRepository repo;

	public FakeTweetsReporter(GamePlayer player, GameMaster master,
			TweetsProvider tweet_provider) {
		this.player = player;
		this.master = master;
		this.tweet_provider = tweet_provider;
		this.repo = TweetsRepository.get_instance();
	}

	public void run() {
		while (true) {
			report_fake_tweets();
		}
	}

	private void report_fake_tweets() {
		Status[] tweets = repo.fetch_players_tweets();
		if (tweets.length == 0)
			return;
		for (Status tweet : tweets) {
			try {
				check_if_fake_tweet(tweet);
			} catch (RemoteException e) {
				System.out
						.println("Something wrong happened while checking a tweet");
				e.printStackTrace();
			}
		}
	}

	private void check_if_fake_tweet(Status tweet)
			throws RemoteException {
		Status suspicious_tweet = tweet_provider.getTweet(tweet.getId());
		if (suspicious_tweet == null) {
			report_fake_tweet(tweet);
		} else if (!(suspicious_tweet.getCheck().equals(tweet.getCheck())
				&& suspicious_tweet.getText().equals(tweet.getText()) && suspicious_tweet
				.getSource().equals(tweet.getSource()))) {
			report_fake_tweet(tweet);
		} else {
			repo.add_valid_tweet(tweet);
		}
	}

	private void report_fake_tweet(Status tweet)
			throws RemoteException {
		List<Status> fake_tweets_for_player = repo.fake_tweets_for_player(tweet
				.getSource());
		if (fake_tweets_for_player != null
				&& fake_tweets_for_player.size() >= GameMaster.MIN_FAKE_TWEETS_BATCH) {
			master.reportFake(player,
					((Status[]) fake_tweets_for_player.toArray()));
		} else {
			repo.add_fake_tweet_for_player(tweet);
		}
	}
}
