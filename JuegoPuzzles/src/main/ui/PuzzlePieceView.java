package main.ui;

import main.audio.SoundEffects;
import main.model.PuzzlePiece;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class PuzzlePieceView extends JLabel {

	private PuzzlePiece piece;
	private Point initialMousePos;
	private Color overlayColor = null;

	public PuzzlePieceView(PuzzlePiece piece, JPanel boardPanel) {
		super(new ImageIcon(piece.getImage()));
		this.piece = piece;
		setFocusable(true);
		setOpaque(false);

		addMouseListener(new MouseAdapter() {
			// Clicar en la pieza.
			@Override
			public void mousePressed(MouseEvent e) {
				// Si la pieza está bloqueada.
				if(piece.isLocked()) {
					// SFX pieza bloqueada.
					SoundEffects.playSound("piece_locked.wav");
					return;
				}

				initialMousePos = e.getPoint();
				if(getParent() != null) {
					getParent().setComponentZOrder(PuzzlePieceView.this, 0);
				}

				// Aplicar un filtro y borde.
				setOverlayColor(new Color(255, 255, 255, 80));
				setBorder(new LineBorder(new Color(255, 255, 255, 80), 2));

				// SFX coger la pieza.
				SoundEffects.playSound("piece_grab.wav");
			}

			// Soltar la pieza.
			@Override
			public void mouseReleased(MouseEvent e) {
				// Si la pieza está bloqueada, no permitir arrastrarla.
				if(piece.isLocked()){
					return;
				}

				// Elimina el filtro y borde al soltar.
				setOverlayColor(null);
				setBorder(null);

				// Obtener la posición actual (esquina superior izquierda) de la pieza.
				Point loc = getLocation();
				int pieceWidth = getWidth();
				int pieceHeight = getHeight();

				// Calcular el centro de la pieza.
				int centerX = loc.x + pieceWidth / 2;
				int centerY = loc.y + pieceHeight / 2;

				// Determinar la celda ideal usando división entera.
				int colIndex = centerX / pieceWidth;
				int rowIndex = centerY / pieceHeight;

				// Calcular el centro de la celda ideal.
				int cellCenterX = colIndex * pieceWidth + pieceWidth / 2;
				int cellCenterY = rowIndex * pieceHeight + pieceHeight / 2;

				// Calcular la distancia del centro de la pieza al centro de la celda.
				double dist = Point.distance(centerX, centerY, cellCenterX, cellCenterY);

				// Calcular la diagonal de la celda y definir un umbral.
				double cellDiagonal = Math.sqrt(pieceWidth * pieceWidth + pieceHeight * pieceHeight);
				double threshold = 0.20 * cellDiagonal; // x% umbral.

				if(dist < threshold) {
					// Posicionar la pieza en la celda.
					int snappedX = colIndex * pieceWidth;
					int snappedY = rowIndex * pieceHeight;
					setLocation(snappedX, snappedY);

					// Si la celda coincide con la posición correcta, bloquear la pieza.
					if(colIndex == piece.getCorrectCol() && rowIndex == piece.getCorrectRow()) {
						// SFX encajar la pieza.
						SoundEffects.playSound("piece_snap.wav");

						// Marcar la pieza como bloqueada.
						piece.setLocked(true);

						// Enviar la pieza al fondo para no tapar las demás.
						boardPanel.setComponentZOrder(PuzzlePieceView.this, boardPanel.getComponentCount() - 1);

						// Aplicar nuevo color y borde.
						//setOverlayColor(new Color(255, 255, 255, 80));
						setBorder(new LineBorder(new Color(255, 255, 255, 90), 2));
					}
				}

				// SFX soltar la pieza.
				SoundEffects.playSound("piece_release.wav");

				// Actualizar la posición relativa para reubicar la pieza en redimensionamientos futuros.
				double rx = (double) getX() / boardPanel.getWidth();
				double ry = (double) getY() / boardPanel.getHeight();
				piece.setRelativeX(rx);
				piece.setRelativeY(ry);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			// Arrastrar la pieza.
			@Override
			public void mouseDragged(MouseEvent e) {
				if(piece.isLocked()) {
					return; // no permitir arrastrar si la pieza está bloqueada.
				}

				int dx = e.getX() - initialMousePos.x;
				int dy = e.getY() - initialMousePos.y;
				Point currLoc = getLocation();
				int newX = currLoc.x + dx;
				int newY = currLoc.y + dy;

				newX = Math.max(0, Math.min(newX, boardPanel.getWidth() - getWidth()));
				newY = Math.max(0, Math.min(newY, boardPanel.getHeight() - getHeight()));

				setLocation(newX, newY);
			}
		});
	}

	// Método para aplicar un filtro de color a la pieza y repintarla.
	public void setOverlayColor(Color color) {
		this.overlayColor = color;
		repaint();
	}

	// Sobreescribir paintComponent para dibujar la imagen y luego el filtro.
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(overlayColor != null) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(overlayColor);
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.dispose();
		}
	}

	// Reescala la imagen en base a factor.
	public void rescaleImage(double factorW, double factorH, int originalW, int originalH) {
		if(piece.getOriginalImage() == null) {
			return; // Evitar null pointer.
		}

		int newW = (int)(originalW * factorW);
		int newH = (int)(originalH * factorH);

		if(newW < 1 || newH < 1) {
			return;
		}

		Image scaled = piece.getOriginalImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		
		setIcon(new ImageIcon(scaled));
		setSize(newW, newH);
	}

	public PuzzlePiece getPuzzlePiece() {
		return piece;
	}

}
