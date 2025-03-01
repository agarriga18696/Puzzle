package main.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager {

	private static final String SETTINGS_FILE = "settings.properties";
	private static Properties properties = new Properties();

	static {
		loadSettings();
	}

	// Método para cargar el archivo de configuración.
	public static void loadSettings() {
		try(FileInputStream fis = new FileInputStream(SETTINGS_FILE)) {
			properties.load(fis);

		} catch(IOException e) {
			System.out.println("No se ha encontrado ningún archivo de configuración previo, se usarán valores predeterminados.");
		}
	}

	// Método para guardar el archivo de configuración.
	public static void saveSettings() {
		try(FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)){
			properties.store(fos, "Game Settings");

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSetting(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static void setSetting(String key, String value) {
		properties.setProperty(key, value);
		saveSettings();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
	}

	public static void setBoolean(String key, boolean value) {
		setSetting(key, String.valueOf(value));
	}

	public static int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));

		} catch(NumberFormatException e) {
			return defaultValue;
		}
	}

	public static void setInt(String key, int value) {
		setSetting(key, String.valueOf(value));
	}

}
