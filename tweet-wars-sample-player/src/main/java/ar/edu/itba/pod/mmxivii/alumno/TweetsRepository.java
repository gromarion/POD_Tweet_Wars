package ar.edu.itba.pod.mmxivii.alumno;

import java.util.ArrayDeque;
import java.util.ArrayList;
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

	public void add_fake_tweet_for_player(Status status) {
		List<Status> tweets;
		if (fake_tweets_map.get(status.getSource()) == null) {
			tweets = new ArrayList<Status>();
			fake_tweets_map.put(status.getSource(), tweets);
		} else
			tweets = fake_tweets_map.get(status.getSource());
		tweets.add(status);
	}

	public void add_valid_tweet(Status tweet) {
		valid_player_tweets.add(tweet);
	}

	public void add_player_tweet(Status tweet) {
		synchronized (this.players_tweets) {
			this.players_tweets.push(tweet);
		}
	}

	public Status[] fetch_master_tweets() {
		synchronized (this.master_tweets) {
			Status[] ans = new Status[master_tweets.size()];
			int i = 0;
			while (!this.master_tweets.isEmpty())
				ans[i++] = master_tweets.pop();
			return ans;
		}
	}

	public Status[] fetch_players_tweets() {
		synchronized (this.players_tweets) {
			Status[] ans = new Status[this.players_tweets.size()];
			int i = 0;
			while (!this.players_tweets.isEmpty())
				ans[i++] = players_tweets.pop();
			return ans;
		}
	}

	public Status[] fetch_valid_players_tweets() {
		synchronized (this.valid_player_tweets) {
			Status[] ans = new Status[this.valid_player_tweets.size()];
			int i = 0;
			while (!this.valid_player_tweets.isEmpty())
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
}
