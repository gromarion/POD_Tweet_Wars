package ar.edu.itba.pod.mmxivii.alumno;

import java.util.Random;

import org.jgroups.JChannel;
import org.jgroups.Message;

public class RandomBullshitGenerator extends Thread {

	private JChannel channel;
	private Random random;

	public RandomBullshitGenerator(JChannel channel) {
		this.channel = channel;
		random = new Random();
	}

	public void start() {
		while (true) {
			float r = random.nextFloat();
			System.out.println("generating bullshit...");
			try {
				if (r > 0.5) {
					channel.send(new Message(null, null, null));
				} else {
					channel.send(new Message(null, null, new Bullshit()));
				}
			} catch (Exception e) {
				System.out
						.println("Something went wrong while generating bullshit");
				e.printStackTrace();
			}
		}
	}

	private static class Bullshit {
	}
}
