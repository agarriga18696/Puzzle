package main.ui;

import main.controller.GameController;
import main.model.PuzzleType;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class PuzzleConfigWindow extends JFrame {

	private JComboBox<PuzzleType> puzzleTypeComboBox;
	private JComboBox<String> pieceCountComboBox;
	private final Map<String, int[]> pieceOptions;
	private GameWindow mainWindow;
	private GameController gameController;

	public PuzzleConfigWindow(GameWindow mainWindow, GameController gameController) {
		super("Nuevo Puzzle");
		this.mainWindow = mainWindow;
		this.gameController = gameController;
		//setSize(400, 250);
		setMinimumSize(new Dimension(400, 250));
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(4, 1));

		// Panel tipo de puzzle.
		JPanel puzzleTypePanel = new JPanel(new GridLayout(1, 2));
		puzzleTypePanel.add(new JLabel("Tipo de Puzzle:"));
		puzzleTypeComboBox = new JComboBox<>(PuzzleType.values());
		puzzleTypeComboBox.setSelectedItem(PuzzleType.MARVEL); // valor por defecto.
		puzzleTypePanel.add(puzzleTypeComboBox);
		add(puzzleTypePanel);

		// Opciones de número de piezas.
		pieceOptions = new LinkedHashMap<>();
		pieceOptions.put("2 piezas (1x2)", new int[]{1, 2});
		pieceOptions.put("4 piezas (2x2)", new int[]{2, 2});
		pieceOptions.put("9 piezas (3x3)", new int[]{3, 3});
		pieceOptions.put("16 piezas (4x4)", new int[]{4, 4});
		pieceOptions.put("25 piezas (5x5)", new int[]{5, 5});
		pieceOptions.put("36 piezas (6x6)", new int[]{6, 6});

		// Panel número de piezas.
		JPanel pieceCountPanel = new JPanel(new GridLayout(1, 2));
		pieceCountPanel.add(new JLabel("Número de Piezas:"));
		pieceCountComboBox = new JComboBox<>(pieceOptions.keySet().toArray(new String[0]));
		pieceCountComboBox.setSelectedItem("25 piezas (5x5)"); // valor por defecto
		pieceCountPanel.add(pieceCountComboBox);
		add(pieceCountPanel);

		// Espaciador.
		add(Box.createVerticalStrut(20));

		// Panel botón JUGAR que ocupará todo el ancho.
        JButton playButton = new JButton("JUGAR");
        playButton.addActionListener(e -> startPuzzle());
        add(playButton);

        pack();
		setVisible(true);
	}

	// Método para inicializar el puzzle.
	private void startPuzzle() {
		PuzzleType selectedType = (PuzzleType) puzzleTypeComboBox.getSelectedItem();
		String selectedPieceCount = (String) pieceCountComboBox.getSelectedItem();
		int[] gridSize = pieceOptions.get(selectedPieceCount);
		int rows = gridSize[0];
		int cols = gridSize[1];

		// Llamar a GameController para inciar la partida.
		gameController.startNewGame(rows, cols, selectedType.name().toLowerCase(), 1200000);

		// Marcar que se ha iniciado una partida.
		mainWindow.setGameStarted(true);

		dispose();
	}

}
