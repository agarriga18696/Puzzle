package main.ui;

import main.audio.MusicPlayer;
import main.audio.SoundEffects;
import main.controller.GameController;
import main.controller.SettingsManager;
import main.model.Puzzle;
import main.util.BackgroundPanel;
import main.util.UIStyle;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {

	private Puzzle currentPuzzle = null; // Guardar el puzzle actual.
	private PuzzleBoard currentPuzzleBoard; // Guardar el tablero actual.
	private boolean gameStarted = false;
	private GameController gameController;

	// Constructor principal.
	public GameWindow() {
		// Cargar la configuración de la ventana.
		boolean fullscreen = SettingsManager.getBoolean("fullscreen", false);
		boolean borderless = SettingsManager.getBoolean("borderless", false);
		String resolution = SettingsManager.getSetting("resolution", "800x600");

		configWindow(fullscreen, borderless, resolution);
		initUI();

		// Instanciar GameController;
		gameController = new GameController(this);

		// SFX ventana.
		SoundEffects.playSound("game_open.wav");

		// Reproducir la música.
		MusicPlayer.playMusic();
	}

	// Constructor para recrear la ventana con un puzzle ya existente.
	public GameWindow(boolean fullscreen, boolean borderless, String resolution, Puzzle puzzle) {
		this.currentPuzzle = puzzle;
		configWindow(fullscreen, borderless, resolution);
		initUI();

		// Instanciar GameController.
		gameController = new GameController(this);

		// Si ya existe un puzzle se muestra.
		if(puzzle != null) {
			showPuzzle(puzzle);
		}
	}

	// Configurar la ventana.
	private void configWindow(boolean fullscreen, boolean borderless, String resolution) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Puzzles");
		setLayout(new BorderLayout());
		setResizable(false);

		String[] res = resolution.split("x");
		int width = Integer.parseInt(res[0]);
		int height = Integer.parseInt(res[1]);

		if(fullscreen) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);

		} else {
			setExtendedState(JFrame.NORMAL);
			setUndecorated(borderless);
			setSize(width, height);
			setLocationRelativeTo(null);
		}
	}

	// Inicializar la interfaz.
	private void initUI() {
		// Panel de menú de navegación.
		NavigationMenuPanel navPanel = new NavigationMenuPanel(this);
		add(navPanel, BorderLayout.NORTH);

		// Panel CardLayout que servirá para mostrar los demás paneles.
		JPanel cardLayoutPanel = configCardLayoutPanel();
		
		// Panel Bienvenida.
		JPanel welcomePanel = configWelcomePanel();

		// Añadir paneles al CardLayout.
		cardLayoutPanel.add(welcomePanel, "welcomePanel");
		
		// Añadir el panel cardLayout a la ventana.
		add(cardLayoutPanel, BorderLayout.CENTER);
		//add(mainPanel, BorderLayout.CENTER);

		setVisible(true);
	}
	
	// CardLayout.
	private JPanel configCardLayoutPanel() {
		JPanel cardLayoutPanel = new JPanel();
		cardLayoutPanel.setLayout(new CardLayout());
		
		return cardLayoutPanel;
	}

	// Panel de Bienvenida.
	private JPanel configWelcomePanel() {
		// Panel principal de bienvenida.
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		UIStyle.applyPanelStyle(welcomePanel);

		// Label de bienvenida.
		JLabel welcomeLabel = UIStyle.createStyledLabel("¡TE DAMOS LA BIENVENIDA!", UIStyle.TITLE_FONT, UIStyle.SECONDARY_COLOR);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(Box.createVerticalStrut(100));
		welcomePanel.add(welcomeLabel);
		welcomePanel.add(Box.createVerticalStrut(25));

		// Label de bienvenida.
		JLabel welcomeLabel2 = UIStyle.createStyledLabel("Para iniciar una nueva partida pulsa el botón JUGAR", UIStyle.DEFAULT_FONT, UIStyle.TEXT_COLOR);
		welcomeLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(welcomeLabel2);
		welcomePanel.add(Box.createVerticalStrut(25));

		// Botón JUGAR.
		JButton newPuzzleButton = UIStyle.createStyledButton("JUGAR", UIStyle.BUTTON_COLOR);
		newPuzzleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newPuzzleButton.addActionListener(e -> new PuzzleConfigWindow(this, gameController));
		welcomePanel.add(Box.createVerticalStrut(20));
		welcomePanel.add(newPuzzleButton);

		return welcomePanel;
	}

	// Método para mostrar el puzzle.
	public void showPuzzle(Puzzle puzzle) {
		this.currentPuzzle = puzzle; // Guardar el puzzle.
		getContentPane().removeAll();

		// Crear el PuzzleBoard y guardarlo en currentPuzzleBoard.
		currentPuzzleBoard = new PuzzleBoard(puzzle, getWidth(), getHeight());
		add(currentPuzzleBoard, BorderLayout.CENTER);

		// SFX juego.
		SoundEffects.playSound("puzzle_start.wav");

		revalidate();
		repaint();
	}

	// Método para permitir que GameController actualice el cronómetro.
	public PuzzleBoard getCurrentPuzzleBoard() {
		return currentPuzzleBoard;
	}

	// Método para aplicar los cambios de configuración en tiempo real.
	public void applySettings(String resolution, boolean fullscreen, boolean borderless) {
		// Guardar el puzzle actual para volver a mostrarlo al reiniciar la ventana.
		Puzzle puzzleActual = this.currentPuzzle;

		// Cerrar la ventana actual.
		setVisible(false);
		dispose();

		// Crear una nueva ventana con la nueva configuración y el puzzle.
		new GameWindow(fullscreen, borderless, resolution, puzzleActual);
	}

	// Método para mostrar la ventana de final de partida.
	public void showResultScreen(String resultMessage) {
		// Quitar todo el contenido actual.
		getContentPane().removeAll();

		// Crear un panel para la pantalla de resultados.
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.setBackground(new Color(255, 255, 255, 0));

		// Etiqueta con el mensaje de resultado.
		JLabel resultLabel = new JLabel(resultMessage, SwingConstants.CENTER);
		resultLabel.setFont(new Font("Arial", Font.BOLD, 32));
		resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		resultPanel.add(Box.createVerticalStrut(140)); // Espacio superior.
		resultPanel.add(resultLabel);
		resultPanel.add(Box.createVerticalStrut(25));

		// Botón para volver al menú principal.
		JButton backButton = new JButton("Volver al Menú");
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		backButton.addActionListener(e -> {
			// Reinicia la interfaz de GameWindow.
			initUI();
			revalidate();
			repaint();
		});
		resultPanel.add(backButton);

		// Agregar el panel de resultados al centro de la ventana.
		add(resultPanel, BorderLayout.CENTER);

		// Revalidar y repintar la ventana.
		revalidate();
		repaint();
	}


	// Getters y setters.
	public void setGameStarted(boolean started) {
		this.gameStarted = started;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}
}
