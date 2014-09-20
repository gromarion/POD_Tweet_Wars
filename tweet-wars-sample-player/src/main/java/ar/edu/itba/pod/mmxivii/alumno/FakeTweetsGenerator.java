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

	public FakeTweetsGenerator(JChannel channel, GamePlayer player,
			String player_hash) {
		this.channel = channel;
		this.player = player;
		this.random = new Random();
		this.player_hash = player_hash;
	}

	public void run() {
		while (true) {
			Status fake_tweet = generate_fake_tweet();
			try {
				channel.send(new Message(null, null, fake_tweet));
			} catch (Exception e) {
				System.out
						.println("Something wrong happened while trying to send a fake tweet");
				e.printStackTrace();
			}
		}
	}

	private Status generate_fake_tweet() {
		return new Status(random.nextLong(),
				new BigInteger(140, random).toString(140), player.getId(),
				player_hash);
	}
}
