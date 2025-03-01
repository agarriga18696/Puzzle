package main.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.controller.SettingsManager;

public class MusicPlayer {

	private static Clip currentClip;
	private static boolean isPlaying = false;
	private static final String MUSIC_PATH = "src/resources/audio/music/";

	// Método para reproducir la playlist de música.
	public static void playMusic() {
		if(isPlaying) return;

		List<String> songList = getAllSongs();
		if(songList.isEmpty()) {
			System.out.println("No se han encontrado archivos de música.");
			return;
		}

		isPlaying = true;
		Thread musicThread = new Thread(() -> {
			try {
				Random rand = new Random();
				while(isPlaying) {
					String song = songList.get(rand.nextInt(songList.size()));
					play(song);
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		musicThread.start();
	}

	// Método para obtener todas las canciones del directorio de música.
	private static List<String> getAllSongs(){
		List<String> songs = new ArrayList<>();
		File musicFolder = new File(MUSIC_PATH);

		if(musicFolder.exists() && musicFolder.isDirectory()) {
			File[] files = musicFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
			if(files != null) {
				for(File file : files) {
					songs.add(file.getAbsolutePath()); // Guardar la ruta compelta de cada archivo.
				}
			}
		}

		return songs;
	}

	// Método para reproducir la canción.
	private static void play(String filePath) {
		try {
			stopMusic();

			File audioFile = new File(filePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			currentClip = AudioSystem.getClip();
			currentClip.open(audioStream);

			// Volumen inicial.
			setVolume(SettingsManager.getInt("musicVolume", 50));

			currentClip.start();
			currentClip.loop(Clip.LOOP_CONTINUOUSLY); // Reproducir en bucle.

		} catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Método para parar la música.
	public static void stopMusic() {
		if(currentClip != null && currentClip.isRunning()) {
			currentClip.stop();
			currentClip.close();
		}

		isPlaying = false;
	}

	// Método para ajustar el volumen de la música.
	public static void setVolume(float newVolume) {
		if(currentClip != null) {
			FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
			float min = gainControl.getMinimum();
			float max = gainControl.getMaximum();
			float range = max - min;

			// Ajustar ganancia de manera exponencial para una transición de volumen más suave.
			float gain = (float) (min + (range * Math.pow(newVolume / 150.0, 0.15)));

			// Asegurar que el volumen no caiga fuera del rango permitido.
			gain = Math.max(min, Math.min(max, gain));

			gainControl.setValue(gain);
		}
	}

	// Método para actualizar el volumen de la música.
	public static void updateVolume() {	
		float newVolume = SettingsManager.getInt("musicVolume", 50);
		setVolume(newVolume);
	}
}
