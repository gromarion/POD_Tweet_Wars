package ar.edu.itba.pod.mmxivii.alumno;

import java.math.BigInteger;
import java.util.Random;

import org.jgroups.JChannel;
import org.jgroups.Message;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

public class FakeTweetsGenerator extends Thread {

	private JChannel channel;
	private GamePlayer player;
	private Random random;
	private String player_hash;
	private static final int TWEET_CHARACTERS_AMOUNT = 140;

	public FakeTweetsGenerator(JChannel channel, GamePlayer player,
			String player_hash) {
		this.channel = channel;
		this.player = player;
		this.random = new Random();
		this.player_hash = player_hash;
	}

	public void run() {
		while (true) {
			try {
				channel.send(new Message(null, null,
						generate_fake_tweet_or_trash()));
			} catch (Exception e) {
				System.out
						.println("Something wrong happened while trying to send a fake tweet");
				e.printStackTrace();
			}
		}
	}

	private Object generate_fake_tweet_or_trash() {
		return new Status(random.nextLong(),
				new BigInteger(TWEET_CHARACTERS_AMOUNT, random)
						.toString(TWEET_CHARACTERS_AMOUNT), player.getId(),
				player_hash);
	}
}
