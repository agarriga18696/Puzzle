package main.model;

public class GameState {

	/*
	 * Almacena datos sobre el estado global de una partida.
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Atributos.
	private Puzzle currentPuzzle; // puzzle actual.
	private long totalTimeAllowed; // tiempo total permitido en milisegundos.
	private long elapsedTime; // tiempo transcurrido en milisegundos.
	private boolean gameOver; // partida terminada.
	private boolean playerWon; // si el jugador ha ganado.

	// Constructor.
	public GameState(Puzzle puzzle, long totalTimeAllowedMs) {
		this.currentPuzzle = puzzle;
		this.totalTimeAllowed = totalTimeAllowedMs;
		this.elapsedTime = 0;
		this.gameOver = false;
		this.playerWon = false;
	}

	// Getters y setters.
	public Puzzle getCurrentPuzzle() {
		return currentPuzzle;
	}

	public void setCurrentPuzzle(Puzzle currentPuzzle) {
		this.currentPuzzle = currentPuzzle;
	}

	public long getTotalTimeAllowed() {
		return totalTimeAllowed;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	// Método para aumentar el tiempo transcurrido.
	public void addElapsedTime(long deltaMs) {
		if(!gameOver) {
			this.elapsedTime += deltaMs;

			// Comprobar si se ha excedido el tiempo.
			if(this.elapsedTime >= totalTimeAllowed) {
				// Se acabó el tiempo y el jugador pierde.
				this.gameOver = true;
				this.playerWon = false;
			}
		}
	}

	// Saber si se acabó el tiempo.
	public boolean isTimeOver() {
		return elapsedTime >= totalTimeAllowed;
	}

	// Saber cuánto tiempo queda.
	public long getTimeRemaining() {
		long remaining = totalTimeAllowed - elapsedTime;
		return (remaining < 0) ? 0 : remaining;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isPlayerWon() {
		return playerWon;
	}

	// Método para marcar la partida como ganada o perdida.
	public void setPlayerWon() {
		this.gameOver = true;
		this.playerWon = true;
	}

	public void setPlayerLost() {
		this.gameOver = true;
		this.playerWon = false;
	}


}
