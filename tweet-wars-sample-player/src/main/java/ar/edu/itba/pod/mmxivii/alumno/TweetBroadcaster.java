package ar.edu.itba.pod.mmxivii.alumno;

import org.jgroups.JChannel;
import org.jgroups.Message;

import ar.edu.itba.pod.mmxivii.tweetwars.Status;

public class TweetBroadcaster extends Thread {

	private TweetsRepository repo;
	private JChannel channel;

	public TweetBroadcaster(JChannel channel) {
		this.channel = channel;
		this.repo = TweetsRepository.get_instance();
	}

	public void start() {
		while (true) {
			for (Status tweet : repo.fetch_master_tweets()) {
				try {
					channel.send(new Message(null, null, tweet));
				} catch (Exception e) {
					System.out
							.println("Something wrong happened while trying to broadcast a tweet");
					e.printStackTrace();
				}
			}
		}
	}
}
