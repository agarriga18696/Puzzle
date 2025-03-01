package main.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.audio.SoundEffects;
import main.model.GameState;
import main.model.Puzzle;
import main.ui.GameWindow;
import main.util.TimeUtils;

public class GameController {

	private GameState gameState;
	private Timer timer; // timer para el cronómetro.
	private GameWindow gameWindow;

	// Intervalo de actualización del timer en milisegundos.
	private final int timerInterval = 1000; // 1 segundo.

	// Constructor.
	public GameController(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	// Método para iniciar una nueva partida.
	public void startNewGame(int rows, int cols, String category, long totalTimeMs) {
		// Generar puzzle.
		Puzzle puzzle = PuzzleGenerator.generatePuzzle(rows, cols, category);

		// Crear GameState con el puzzle y el tiempo total permitido.
		gameState = new GameState(puzzle, totalTimeMs);

		// Mostrar el puzzle en la ventana principal.
		gameWindow.showPuzzle(puzzle);

		// Iniciar el timer para actualizar el cronómetro.
		startTimer();
	}

	// Método para iniciar un timer que cada segundo actualiza el tiempo transcurrido y verifica si se agotó el tiempo.
	private void startTimer() {
		timer = new Timer(timerInterval, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameState.addElapsedTime(timerInterval);

				// Actualizar el cronómetro en la interfaz.
				long timeRemaining = gameState.getTimeRemaining();
				String timeText = "Tiempo restante: " + TimeUtils.formatTime(timeRemaining) + " min";

				// Actualizar el cronómetro en PuzzleBoard.
				if(gameWindow.getCurrentPuzzleBoard() != null) {
					gameWindow.getCurrentPuzzleBoard().updateTimerLabel(timeText);
				}

				// Verificar si el puzzle se completó.
				if(!gameState.isGameOver() && gameState.getCurrentPuzzle().isCompleted()) {
					onPuzzleCompleted();
				}

				// Si se ha agotado el tiempo, parar el timer y notificar la derrota.
				if(gameState.isTimeOver()) {
					timer.stop();
					onTimeOver();
				}
			}
		});

		timer.start();
	}

	// Método para manejar cuando el puzzle se completa.
	public void onPuzzleCompleted() {
		if(timer != null && timer.isRunning()) {
			timer.stop();
		}

		// Marcar la partida como ganada.
		gameState.setPlayerWon();

		// SFX victoria.
		SoundEffects.playSound("win.wav");

		// Mostrar ventana de victoria.
		gameWindow.showResultScreen("¡Puzzle Completado! Tiempo restante: " + TimeUtils.formatTime(gameState.getTimeRemaining()) + " min");
	}

	// Cuando se agota el tiempo.
	public void onTimeOver() {
		gameState.setPlayerLost();

		// SFX derrota.
		SoundEffects.playSound("lose.wav");

		// Mostrar ventana de derrota.
		gameWindow.showResultScreen("Se acabó el tiempo. ¡Has perdido!");
	}

	public GameState getGameState() {
		return gameState;
	}


}
