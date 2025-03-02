package main.ui;

import main.audio.MusicPlayer;
import main.controller.SettingsManager;
import main.util.UIStyle;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class SettingsWindow extends JFrame {

	private GameWindow mainWindow;
	private JComboBox<String> resolutionCombo;
	private JCheckBox fullscreenCheck;
	private JCheckBox borderlessCheck;
	private JSlider musicVolumeSlider;
	private JSlider sfxVolumeSlider;

	// Constructor.
	public SettingsWindow(GameWindow mainWindow) {
		super("Configuración");
		this.mainWindow = mainWindow;
		setMinimumSize(new Dimension(400, 350));
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(9,1));
		getContentPane().setBackground(UIStyle.BACKGROUND_COLOR);

		// Selector de resoluciones.
		JLabel resolutionLabel = UIStyle.createStyledHeadingLabel("Pantalla");
		add(resolutionLabel);	

		resolutionCombo = new JComboBox<>(new String[]{"800x600", "1024x768", "1280x720", "1920x1080", "2560x1440", "3840x2160"});
		resolutionCombo.setSelectedItem(SettingsManager.getSetting("resolution","800x600"));
		resolutionCombo.setFont(UIStyle.DEFAULT_FONT);
		resolutionCombo.setFocusable(false);
		resolutionCombo.setBorder(UIStyle.NO_BORDER);
		resolutionCombo.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(UIStyle.DEFAULT_FONT);

				return lbl;
			}
		});
		add(resolutionCombo);

		// Panel para los 2 checkboxes en una misma fila.
		JPanel checkPanel = new JPanel(new GridLayout(1,2));	
		
		// Checkbox Pantalla Completa.
		fullscreenCheck = new JCheckBox("Pantalla Completa", SettingsManager.getBoolean("fullscreen",false));
		fullscreenCheck.setHorizontalAlignment(SwingConstants.CENTER);
		fullscreenCheck.setFont(UIStyle.DEFAULT_FONT);
		fullscreenCheck.setBackground(getContentPane().getBackground());
		fullscreenCheck.setFocusable(false);
		fullscreenCheck.setFocusPainted(false);
		checkPanel.add(fullscreenCheck);

		// Checkbox Ventana sin bordes.
		borderlessCheck = new JCheckBox("Ventana sin bordes", SettingsManager.getBoolean("borderless",false));
		borderlessCheck.setHorizontalAlignment(SwingConstants.CENTER);
		borderlessCheck.setFont(UIStyle.DEFAULT_FONT);
		borderlessCheck.setBackground(getContentPane().getBackground());
		borderlessCheck.setFocusable(false);
		borderlessCheck.setFocusPainted(false);
		checkPanel.add(borderlessCheck);
		
		add(checkPanel);

		// Bloquear configuración si se ha iniciado una partida.
		if(mainWindow.isGameStarted()){
			resolutionCombo.setEnabled(false);
			fullscreenCheck.setEnabled(false);
			borderlessCheck.setEnabled(false);
		}

		// Label Volumen Música.
		JLabel musicLabel = UIStyle.createStyledHeadingLabel("Volumen Música");
		musicLabel.setFont(UIStyle.HEADING_FONT);
		add(musicLabel);

		// Slider Música.
		musicVolumeSlider = new JSlider(0,100, SettingsManager.getInt("musicVolume",50));
		musicVolumeSlider.setFont(UIStyle.DEFAULT_FONT);
		UIStyle.createStyledSlider(musicVolumeSlider);
		add(musicVolumeSlider);

		// Label Volumen SFX.
		JLabel sfxLabel = UIStyle.createStyledHeadingLabel("Volumen Efectos");
		sfxLabel.setFont(UIStyle.HEADING_FONT);
		add(sfxLabel);

		// Slider SFX.
		sfxVolumeSlider = new JSlider(0,100, SettingsManager.getInt("sfxVolume",50));
		sfxVolumeSlider.setFont(UIStyle.DEFAULT_FONT);
		UIStyle.createStyledSlider(sfxVolumeSlider);
		add(sfxVolumeSlider);

		// Espaciador.
		add(Box.createVerticalStrut(20));

		// Botón Aplicar.
		JButton applyBtn = UIStyle.createStyledButton("Aplicar", UIStyle.BUTTON_COLOR);
		applyBtn.addActionListener(e -> applySettings());
		add(applyBtn);

		pack();
		setVisible(true);
	}

	// Método para aplicar la configuración de la ventana.
	private void applySettings() {
		String newResolution = (String) resolutionCombo.getSelectedItem();
		boolean newFullscreen = fullscreenCheck.isSelected();
		boolean newBorderless = borderlessCheck.isSelected();
		int newMusicVol = musicVolumeSlider.getValue();
		int newSfxVol = sfxVolumeSlider.getValue();

		// Leer valores actuales de SettingsManager.
		String currentResolution = SettingsManager.getSetting("resolution", "800x600");
		boolean currentFullscreen = SettingsManager.getBoolean("fullscreen", false);
		boolean currentBorderless = SettingsManager.getBoolean("borderless", false);

		// Guardar nuevos valores.
		SettingsManager.setSetting("resolution", newResolution);
		SettingsManager.setBoolean("fullscreen", newFullscreen);
		SettingsManager.setBoolean("borderless", newBorderless);
		SettingsManager.setInt("musicVolume", newMusicVol);
		SettingsManager.setInt("sfxVolume", newSfxVol);

		// Actualizar el volumen de la música en tiempo real.
		MusicPlayer.updateVolume();

		// Verificar si las configuraciones de la ventana han cambiado.
		boolean windowSettingsChanged = !newResolution.equals(currentResolution) || newFullscreen != currentFullscreen || newBorderless != currentBorderless;

		// Recrear la ventana si se han cambiado los valores de ventana.
		if(windowSettingsChanged) {
			mainWindow.applySettings(newResolution, newFullscreen, newBorderless);
		}

		dispose();
	}
}
