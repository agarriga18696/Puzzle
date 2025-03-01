package main.model;

import java.util.Collections;
import java.util.List;

public class Puzzle {

	// Atributos.
	private int rows;
	private int cols;
	private List<PuzzlePiece> pieces;

	// Constructor.
	public Puzzle(int rows, int cols, List<PuzzlePiece> pieces) {
		this.rows = rows;
		this.cols = cols;
		this.pieces = pieces;
		shufflePieces();
	}

	// Mezclar piezas.
	private void shufflePieces() {
		Collections.shuffle(pieces);
	}

	// Método para comprobar si el puzzle está completado correctamente.
	public boolean isCompleted() {
		for(PuzzlePiece piece : pieces) {
			// Comprobar que todas las piezas están bloqueadas (en su posición correcta).
			if(!piece.isLocked()) {
				return false;
			}
		}
		return true;
	}

	public int getRows() {
		return rows;
	}
	public int getCols() {
		return cols;
	}
	public List<PuzzlePiece> getPieces() {
		return pieces;
	}
}
