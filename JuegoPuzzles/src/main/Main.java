package main;

import javax.swing.*;

import main.ui.GameWindow;

public class Main {

	public static void main(String[] args) {
		// Crear ventana de juego.
		SwingUtilities.invokeLater(GameWindow::new);
		
	}

}
