package main.util;

public class TimeUtils {

	/**
	 * Convierte milisegundos a un String con formato "MM:SS".
	 *
	 * @param millis	-> tiempo en milisegundos.
	 * @return 			-> un String con el tiempo formateado en minutos y segundos.
	 */

	// Método para formatear el cronómetro.
	public static String formatTime(long millis) {
		long totalSeconds = millis / 1000;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		
		return String.format("%02d:%02d", minutes, seconds);
	}
}
