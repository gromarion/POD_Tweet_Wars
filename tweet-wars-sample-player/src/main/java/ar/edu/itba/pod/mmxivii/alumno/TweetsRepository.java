package ar.edu.itba.pod.mmxivii.alumno;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.pod.mmxivii.tweetwars.Status;

public class TweetsRepository {

	private static TweetsRepository instance = null;
	private Deque<Status> master_tweets;
	private Deque<Status> players_tweets;
	private Deque<Status> valid_player_tweets;
	private Map<String, List<Status>> fake_tweets_map;

	public static TweetsRepository get_instance() {
		if (instance == null)
			instance = new TweetsRepository();
		return instance;
	}
	
	public List<Status> fake_tweets_for_player(String player_id) {
		return fake_tweets_map.get(player_id);
	}

	public void add_player_tweet(Status tweet) {
		synchronized (this.players_tweets) {
			this.players_tweets.push(tweet);
		}
	}

	public Status[] fetch_master_tweets(int tweet_amount) {
		synchronized (this.master_tweets) {
			int ans_amount = fetch_tweet_amount(tweet_amount);
			Status[] ans = new Status[ans_amount];
			for (int i = 0; i < ans_amount; i++)
				ans[i++] = master_tweets.pop();
			return ans;
		}
	}
	
	public Status[] fetch_players_tweets(int tweet_amount) {
		synchronized (this.players_tweets) {
			if (players_tweets.isEmpty())
				return new Status[0];
			int ans_amount = fetch_tweet_amount(tweet_amount);
			Status[] ans = new Status[ans_amount];
			for (int i = 0; i < ans_amount; i++)
				ans[i++] = players_tweets.pop();
			return ans;
		}
	}
	
	public Status[] fetch_valid_players_tweets(int tweet_amount) {
		synchronized (this.valid_player_tweets) {
			if (valid_player_tweets.isEmpty())
				return new Status[0];
			int ans_amount = fetch_tweet_amount(tweet_amount);
			Status[] ans = new Status[ans_amount];
			for (int i = 0; i < ans_amount; i++)
				ans[i++] = valid_player_tweets.pop();
			return ans;
		}
	}

	public void add_master_tweets(Status[] tweets) {
		synchronized (this.master_tweets) {
			for (Status tweet : tweets)
				this.master_tweets.push(tweet);
		}
	}

	private TweetsRepository() {
		master_tweets = new ArrayDeque<Status>();
		players_tweets = new ArrayDeque<Status>();
		valid_player_tweets = new ArrayDeque<Status>();
		fake_tweets_map = new HashMap<String, List<Status>>();
	}

	private int fetch_tweet_amount(int tweet_amount) {
		if (tweet_amount > master_tweets.size())
			return master_tweets.size();
		else
			return tweet_amount;
	}
}
