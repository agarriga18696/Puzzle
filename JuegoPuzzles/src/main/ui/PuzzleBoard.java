package main.ui;

import main.model.Puzzle;
import main.model.PuzzlePiece;
import main.util.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleBoard extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel boardPanel;
	private List<PuzzlePieceView> pieceViews = new ArrayList<>();

	private int puzzleW, puzzleH;
	private int pieceW, pieceH;
	private int rows, cols;

	private Puzzle puzzle;

	private JLabel timerLabel;

	public PuzzleBoard(Puzzle puzzle, int parentW, int parentH) {
		super(new BorderLayout());
		this.puzzle = puzzle;

		// Label del cronómetro.
		timerLabel = UIStyle.createStyledLabel("Tiempo restante: ", UIStyle.DEFAULT_FONT, Color.black);
		timerLabel.setOpaque(true);
		add(timerLabel, BorderLayout.NORTH);

		// Panel del tablero.
		JPanel centerPanel = new JPanel(null);
		centerPanel.setBackground(UIStyle.BACKGROUND_COLOR);

		// Determinar la cuadrícula.
		rows = puzzle.getRows();
		cols = puzzle.getCols();
		int boardMaxW = parentW - 100;
		int boardMaxH = parentH - 100;
		boardMaxW = Math.max(boardMaxW, 0);
		boardMaxH = Math.max(boardMaxH, 0);

		PuzzlePiece first = puzzle.getPieces().get(0);
		pieceW = first.getOriginalWidth();
		pieceH = first.getOriginalHeight();
		puzzleW = pieceW * cols;
		puzzleH = pieceH * rows;

		// Panel del tablero.
		boardPanel = new JPanel(null);
		boardPanel.setBackground(UIStyle.BUTTON_COLOR);
		boardPanel.setBorder(UIStyle.NO_BORDER);
		boardPanel.setSize(puzzleW, puzzleH);

		// Posicionar el boardPanel en el centro del centerPanel.
		int offX = (boardMaxW - puzzleW) / 2;
		int offY = (boardMaxH - puzzleH) / 2;
		offX = Math.max(offX, 0);
		offY = Math.max(offY, 0);
		boardPanel.setLocation(offX, offY);

		centerPanel.add(boardPanel);

		// Añadir las piezas del tablero en posiciones aleatorias.
		Random rand = new Random();
		for(PuzzlePiece p : puzzle.getPieces()) {
			PuzzlePieceView view = new PuzzlePieceView(p, boardPanel);
			view.setSize(pieceW, pieceH);

			int maxX = puzzleW - pieceW;
			int maxY = puzzleH - pieceH;
			int x = rand.nextInt(maxX + 1);
			int y = rand.nextInt(maxY + 1);
			view.setLocation(x, y);

			boardPanel.add(view);
			pieceViews.add(view);
		}

		// Agregar el panel central.
		add(centerPanel, BorderLayout.CENTER);
		setPreferredSize(new Dimension(parentW, parentH));

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				repositionAndRescale();
			}
		});

		
	}

	// Método para actualizar el texto del cronómetro.
	public void updateTimerLabel(String text) {
		timerLabel.setText(text);
	}

	// Método para reposicionar y reescalar el tablero.
	private void repositionAndRescale() {
		int newBW = getWidth() - 100;
		int newBH = getHeight() - 100;
		newBW = Math.max(newBW, 0);
		newBH = Math.max(newBH, 0);

		boardPanel.setSize(newBW, newBH);
		boardPanel.setLocation(50,50);

		double factorW = (double)newBW / (double)(pieceW*cols);
		double factorH = (double)newBH / (double)(pieceH*rows);

		for(PuzzlePieceView view : pieceViews) {
			// Reescalar imagen.
			view.rescaleImage(factorW, factorH, view.getPuzzlePiece().getOriginalWidth(), view.getPuzzlePiece().getOriginalHeight());

			// Recolocar.
			Point loc = view.getLocation();
			int newX = (int)(loc.x * factorW);
			int newY = (int)(loc.y * factorH);

			newX = Math.max(0, Math.min(newX, (int)(newBW - view.getWidth())));
			newY = Math.max(0, Math.min(newY, (int)(newBH - view.getHeight())));

			view.setLocation(newX,newY);
		}
		
		revalidate();
		repaint();
	}

	// Método para consultar si el puzzle está completo.
	public boolean isPuzzleCompleted() {
		return puzzle.isCompleted();
	}
}
