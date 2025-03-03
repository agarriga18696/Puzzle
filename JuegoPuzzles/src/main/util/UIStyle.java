package main.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import main.audio.SoundEffects;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public final class UIStyle {

	/*
	 * Diseño de la interfaz: fuentes, esquema de colores, etc.
	 * 
	 */

	// Paleta de colores.
	public static final Color PRIMARY_COLOR = new Color(0x9EB380);
	public static final Color SECONDARY_COLOR = new Color(0x6F7E5B);
	public static final Color BUTTON_COLOR = new Color(0xAC9076);
	public static final Color BUTTON_TEXT_COLOR = new Color(0xFFFFFF);
	public static final Color TEXT_COLOR = new Color(0x2B2822);
	public static final Color BACKGROUND_COLOR = new Color(0xF7F7F7);

	// Fuentes.
	private static Font _REGULAR_FONT;
	private static Font _BOLD_FONT;
	private static Font _EXTRA_BOLD_FONT;
	static {
		try {
			// Cargar las fuentes desde los archivos .ttf.
			File f1 = new File("src/resources/fonts/Anaheim-Medium.ttf");
			File f2 = new File("src/resources/fonts/Anaheim-Bold.ttf");
			File f3 = new File("src/resources/fonts/Anaheim-ExtraBold.ttf");

			Font baseFont1 = Font.createFont(Font.TRUETYPE_FONT, f1);
			Font baseFont2 = Font.createFont(Font.TRUETYPE_FONT, f2);
			Font baseFont3 = Font.createFont(Font.TRUETYPE_FONT, f3);

			_REGULAR_FONT = baseFont1.deriveFont(14f);
			_BOLD_FONT = baseFont2.deriveFont(14f);
			_EXTRA_BOLD_FONT = baseFont3.deriveFont(14f);

		} catch (Exception e) {
			e.printStackTrace();
			// Si falla, usar SansSerif por defecto.
			_REGULAR_FONT = new Font("SansSerif", Font.PLAIN, 14);
		}
	}

	public static final Font EXTRA_BOLD_FONT = _EXTRA_BOLD_FONT.deriveFont(32f);
	public static final Font BOLD_FONT = _BOLD_FONT.deriveFont(18f);
	public static final Font REGULAR_FONT = _REGULAR_FONT.deriveFont(16f);
	

	// Bordes y padding.
	public static final Border NO_BORDER = BorderFactory.createEmptyBorder();
	public static final Border PANEL_PADDING = new EmptyBorder(20, 20, 20, 20);

	public static Border CUSTOM_PADDING(int... padding) {
		int paddings[] = new int[4];

		for(int i = 0; i < paddings.length; i++) {
			for(int j = 0; j < padding.length; j++) {
				paddings[i] = padding[j];
			}
		}

		return new EmptyBorder(paddings[0], paddings[1], paddings[2], paddings[3]);
	}

	/*
	 * 
	 * COMPONENTES
	 * 
	 */

	// Panel.
	public static JPanel defaultPanel() {
		JPanel panel = new BackgroundPanel("background.png", 0.25);
		panel.setBorder(PANEL_PADDING);
		return panel;
	}

	// Botón.
	public static JButton defaultButton(String text, Color bgColor) {
		JButton button = new JButton(text.toUpperCase());
		button.setBackground(bgColor);
		button.setForeground(BUTTON_TEXT_COLOR);
		button.setFont(BOLD_FONT);
		button.setMaximumSize(new Dimension(200, 40));
		button.setFocusable(false);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setContentAreaFilled(false);
		button.setOpaque(true);

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				button.setBackground(new Color(bgColor.getRed() + 20, bgColor.getGreen() + 20, bgColor.getBlue() + 20, bgColor.getAlpha()));

				// SFX click.
				SoundEffects.playSound("button_click.wav");
			};

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(bgColor.getRed() + 10, bgColor.getGreen() + 10, bgColor.getBlue() + 10, bgColor.getAlpha()));

				// SFX hover.
				SoundEffects.playSound("button_hover.wav");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(bgColor);
			}
		});

		return button;
	}

	// Botón de título.
	public static JButton titleButton(String text) {
		JButton titleButton = new JButton(text);
		titleButton.setFont(EXTRA_BOLD_FONT.deriveFont(64f));
		titleButton.setForeground(BUTTON_TEXT_COLOR);
		titleButton.setOpaque(false);
		titleButton.setContentAreaFilled(false);
		titleButton.setBorderPainted(false);
		titleButton.setFocusPainted(false);
		titleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		titleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Color current = titleButton.getForeground();
				int r = Math.min(current.getRed() - 20, 255);
				int g = Math.min(current.getGreen() - 20, 255);
				int b = Math.min(current.getBlue() - 20, 255);
				titleButton.setBackground(new Color(r, g, b, current.getAlpha()));

				// SFX click.
				SoundEffects.playSound("button_click.wav");
			};

			@Override
			public void mouseEntered(MouseEvent e) {
				Color current = titleButton.getForeground();
				int r = Math.min(current.getRed() - 10, 255);
				int g = Math.min(current.getGreen() - 10, 255);
				int b = Math.min(current.getBlue() - 10, 255);
				titleButton.setForeground(new Color(r, g, b, current.getAlpha()));

				// SFX hover.
				SoundEffects.playSound("button_hover.wav");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				titleButton.setForeground(BUTTON_TEXT_COLOR);
				titleButton.setBackground(titleButton.getForeground());
			}
		});

		return titleButton;
	}

	// Label Normal.
	public static JLabel defaultLabel(String text, Font font, Color color) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(font);
		label.setForeground(color);

		return label;
	}

	// Label de Cabecera.
	public static JLabel headingLabel(String text) {
		JLabel label = new JLabel(text.toUpperCase(), SwingConstants.CENTER);
		label.setFont(BOLD_FONT);
		label.setForeground(SECONDARY_COLOR);
		label.setOpaque(false);

		return label;
	}
	
	// Label Cronómetro.
	public static JLabel timerLabel() {
		JLabel timer = new JLabel();
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(_EXTRA_BOLD_FONT.deriveFont(32f));
		timer.setForeground(TEXT_COLOR);
		
		return timer;
	}

	// Slider.
	public static void defaultSlider(JSlider slider) {
		slider.setForeground(TEXT_COLOR);
		slider.setFont(REGULAR_FONT);
		slider.setOpaque(false);
		slider.setBorder(new EmptyBorder(5, 5, 5, 5));
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(10);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}


}
