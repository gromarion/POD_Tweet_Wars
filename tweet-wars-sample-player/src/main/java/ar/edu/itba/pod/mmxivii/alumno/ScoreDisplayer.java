package ar.edu.itba.pod.mmxivii.alumno;

import java.rmi.RemoteException;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;

public class ScoreDisplayer extends Thread {

	private GamePlayer player;
	private GameMaster master;

	public ScoreDisplayer(GamePlayer player, GameMaster master) {
		this.player = player;
		this.master = master;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
				System.out.println(master.getScore(player));
			} catch (RemoteException | InterruptedException e) {
				System.out
						.println("Something wrong happened while fetching scores");
				e.printStackTrace();
			}
		}
	}
}
