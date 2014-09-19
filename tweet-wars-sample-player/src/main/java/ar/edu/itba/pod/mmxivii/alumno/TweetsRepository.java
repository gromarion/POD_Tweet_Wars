package ar.edu.itba.pod.mmxivii.alumno;

import java.util.ArrayDeque;
import java.util.Deque;

import ar.edu.itba.pod.mmxivii.tweetwars.Status;

public class TweetsRepository {

	private static TweetsRepository instance = null;
	private Deque<Status> tweets;

	public static TweetsRepository get_instance() {
		if (instance == null)
			instance = new TweetsRepository();
		return instance;
	}

	public void add_tweets(Status[] tweets) {
		synchronized (this.tweets) {
			for (Status status : tweets)
				this.tweets.push(status);
		}
	}

	public Status[] fetch_tweets(int tweet_amount) {
		synchronized (this.tweets) {
			int ans_amount = fetch_tweet_amount(tweet_amount);
			Status[] ans = new Status[ans_amount];
			for (int i = 0; i < ans_amount; i++)
				ans[i++] = tweets.pop();
			return ans;
		}
	}

	private TweetsRepository() {
		tweets = new ArrayDeque<Status>();
	}

	private int fetch_tweet_amount(int tweet_amount) {
		if (tweet_amount > tweets.size())
			return tweets.size();
		else
			return tweet_amount;
	}
}
