package main.ui;

import main.util.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

@SuppressWarnings("serial")
public class NavigationMenuPanel extends JPanel {

	public NavigationMenuPanel(GameWindow gameWindow) {
		// Usamos BoxLayout en el eje X para disponer los tres subpaneles horizontalmente.
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setPreferredSize(new Dimension(0, 85)); // Altura fija de 85 px.
		setBackground(UIStyle.PRIMARY_COLOR);

		// Panel izquierdo: contendrá el botón OPCIONES alineado a la izquierda.
		JPanel leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setBackground(Color.RED);
		leftPanel.setOpaque(false);
		leftPanel.setMaximumSize(new Dimension(200, 85));
		leftPanel.setPreferredSize(new Dimension(180, 85));
		leftPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
		GridBagConstraints gbcLeft = new GridBagConstraints();
		gbcLeft.gridx = 0;
		gbcLeft.gridy = 0;
		gbcLeft.weightx = 1.0;
		gbcLeft.fill = GridBagConstraints.HORIZONTAL;
		gbcLeft.anchor = GridBagConstraints.WEST;
		JButton optionsButton  = UIStyle.defaultButton("OPCIONES", UIStyle.SECONDARY_COLOR);
		optionsButton.addActionListener(e -> new SettingsWindow(gameWindow));
		leftPanel.add(optionsButton, gbcLeft);

		// Panel central: contendrá el botón TÍTULO "PUZZLE" centrado.
		JPanel centerPanel = new JPanel(new GridBagLayout());
		//centerPanel.setBackground(Color.YELLOW);
		centerPanel.setOpaque(false);
		centerPanel.setMaximumSize(new Dimension(400, 85));
		centerPanel.setPreferredSize(new Dimension(280, 85));
		GridBagConstraints gbcCenter = new GridBagConstraints();
		gbcCenter.gridx = 0;
		gbcCenter.gridy = 0;
		gbcCenter.weightx = 1.0;
		gbcCenter.fill = GridBagConstraints.HORIZONTAL;
		gbcCenter.anchor = GridBagConstraints.CENTER;
		JButton titleButton = UIStyle.titleButton("PUZZLES");
		centerPanel.add(titleButton, gbcCenter);

		// Panel derecho: contendrá el botón SALIR alineado a la derecha.
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.BLUE);
		rightPanel.setOpaque(false);
		rightPanel.setMaximumSize(new Dimension(200, 85));
		rightPanel.setPreferredSize(new Dimension(180, 85));
		rightPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
		GridBagConstraints gbcRight = new GridBagConstraints();
		gbcRight.gridx = 0;
		gbcRight.gridy = 0;
		gbcRight.weightx = 1.0;
		gbcRight.fill = GridBagConstraints.HORIZONTAL;
		gbcRight.anchor = GridBagConstraints.EAST;
		JButton exitButton = UIStyle.defaultButton("SALIR", UIStyle.SECONDARY_COLOR);
		exitButton.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(gameWindow, "¿Seguro que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		});
		rightPanel.add(exitButton, gbcRight);

		// Agregamos los subpaneles al panel principal.
		add(leftPanel);
		add(Box.createHorizontalGlue());
		add(centerPanel);
		add(Box.createHorizontalGlue());
		add(rightPanel);
	}
}
