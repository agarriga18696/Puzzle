package main.model;

import java.awt.image.BufferedImage;

public class PuzzlePiece {

	private int id;
	private int correctRow;
	private int correctCol;

	private BufferedImage originalImage; // para reescalar.
	private BufferedImage currentImage;  // la imagen actual (subimagen).
	private int originalWidth;
	private int originalHeight;

	// Posicion relativa de cada pieza.
	private double relativeX;
	private double relativeY;

	private boolean locked = false;

	// Constructor.
	public PuzzlePiece(int id, int correctRow, int correctCol) {
		this.id = id;
		this.correctRow = correctRow;
		this.correctCol = correctCol;
		this.relativeX = 0.0;
		this.relativeY = 0.0;
	}

	// Método para comprobar si el puzzle está bien montado segun filas y columnas.
	public boolean isInCorrectPosition(int totalRows, int totalCols) {
		// Convertir relativeX y relativeY en celdas.
		int rowPos = (int)Math.round(relativeY * totalRows);
		int colPos = (int)Math.round(relativeX * totalCols);
		return (rowPos == correctRow && colPos == correctCol);
	}

	public void setOriginalImage(BufferedImage img) {
		this.originalImage = img;
		this.originalWidth = img.getWidth();
		this.originalHeight = img.getHeight();
	}

	public BufferedImage getOriginalImage() {
		return originalImage;
	}

	public int getOriginalWidth() {
		return originalWidth;
	}
	
	public int getOriginalHeight() {
		return originalHeight;
	}

	public BufferedImage getImage() {
		return currentImage;
	}
	
	public void setImage(BufferedImage img) {
		this.currentImage = img;
	}

	public int getId() {
		return id;
	}
	
	public int getCorrectRow() {
		return correctRow;
	}
	
	public int getCorrectCol() {
		return correctCol;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	// Relative positions (opcional)
	public double getRelativeX() { return relativeX; }
	public double getRelativeY() { return relativeY; }
	public void setRelativeX(double rx) { this.relativeX = rx; }
	public void setRelativeY(double ry) { this.relativeY = ry; }
}
