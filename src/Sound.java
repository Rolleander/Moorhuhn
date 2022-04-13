
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	Clip sounds[] = new Clip[11]; // Anzahl Sounds
	Clip kill[] = new Clip[12]; // Anzahl Sounds

	public Sound() {

		for (int i = 0; i < sounds.length; i++) {

			sounds[i] = loadSound("s" + i + ".wav");

		}
		for (int i = 0; i < kill.length; i++) {

			kill[i] = loadSound("k" + i + ".wav");

		}
	}

	public void playSound(int nr, boolean clear) // nr: soundnummer clear: wenn false nur abspielen wenn sound gerade
													// nicht abgespielt wird
	{
		if (clear) {
			sounds[nr].setMicrosecondPosition(0);
		} else {
			if (sounds[nr].getMicrosecondPosition() >= sounds[nr].getMicrosecondLength()) {
				sounds[nr].setMicrosecondPosition(0);
			}
		}
		sounds[nr].start();
	}

	public void playKillSound(int nr) {
		kill[nr].setMicrosecondPosition(0);
		kill[nr].start();
	}

	public void loopSound(int nr) {
		// sounds[nr].setMicrosecondPosition(0);
		sounds[nr].loop(999);
	}

	public void stopSound(int nr) {

		sounds[nr].stop();

	}

	private Clip loadSound(String name) {
		Clip clip = null;
		try {
			try {
				// Open an audio input stream.
				java.net.URL url = this.getClass().getClassLoader().getResource(name);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				// Open audio clip and load samples from the audio input stream.
				clip.open(audioIn);
				clip.setMicrosecondPosition(0);

			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clip;
	}
}
