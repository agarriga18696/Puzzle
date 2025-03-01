package main.audio;

import java.io.File;
import javax.sound.sampled.*;

import main.controller.SettingsManager;

public class SoundEffects {

	private static final String SFX_PATH = "src/resources/audio/sfx/";

	// Método para reproducir un efecto de sonido.
	public static void playSound(String fileName) {
		try {
			File soundFile = new File(SFX_PATH + fileName);

			if(!soundFile.exists()){
				System.err.println("No se ha encontrado el sonido: " + SFX_PATH + fileName);
				return;
			}

			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);

			// Obtener el volumen de SFX desde SettingsManager (valor entre 0 y 100).
			int sfxVolume = SettingsManager.getInt("sfxVolume", 50);

			// Ajustar la ganancia del clip.
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float min = gainControl.getMinimum();
			float max = gainControl.getMaximum();
			float range = max - min;

			// Ajustar ganancia de manera exponencial para una transición de volumen más suave.
			float gain = (float) (min + (range * Math.pow(sfxVolume / 150.0, 0.15)));

			// Asegurar que el volumen no caiga fuera del rango permitido.
			gain = Math.max(min, Math.min(max, gain));

			gainControl.setValue(gain);

			// Reproducir.
			clip.start();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
