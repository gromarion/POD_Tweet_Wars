package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;
import java.util.List;

import java.util.Arrays;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

public class FakeTweetsReporter {

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

	public void filter_and_broadcast_real_tweets(Status tweet) {
		try {
			check_if_fake_tweet(tweet);
		} catch (RemoteException e) {
			System.out
					.println("Something wrong happened while checking a tweet");
			e.printStackTrace();
		}
	}

	private void check_if_fake_tweet(Status tweet) throws RemoteException {
		Status suspicious_tweet = tweet_provider.getTweet(tweet.getId());
		if (is_fake(suspicious_tweet, tweet)) {
			report_fake_tweet(tweet);
		} else {
			master.tweetReceived(player, tweet);
		}
	}

	private boolean is_fake(Status suspicious_tweet, Status tweet) {
		return suspicious_tweet == null
				|| !(suspicious_tweet.getCheck().equals(tweet.getCheck())
						&& suspicious_tweet.getText().equals(tweet.getText()) && suspicious_tweet
						.getSource().equals(tweet.getSource()));
	}

	private void report_fake_tweet(Status tweet) throws RemoteException {
		List<Status> fake_tweets_for_player = repo.add_fake_tweet_for_player(tweet);
		if (fake_tweets_for_player != null
				&& fake_tweets_for_player.size() >= GameMaster.MIN_FAKE_TWEETS_BATCH) {
			master.reportFake(player, Arrays.copyOf(fake_tweets_for_player.toArray(), fake_tweets_for_player.size(), Status[].class));
		}
	}
}
