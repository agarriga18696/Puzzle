package main.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel {

    private Image backgroundImage;
    private double zoom; // Factor de zoom; 1.0 = tamaño original.
    private final String BASE_PATH = "src/resources/images/ui/";

    /**
     * Constructor.
     * 
     * @param backgroundImage Imagen base para el patrón.
     * @param zoom Factor de zoom (por ejemplo, 1.0 para tamaño original, 0.5 para la mitad, 2.0 para el doble, etc.)
     */
    public BackgroundPanel(String imageFileName, double zoom) {
        try {
        	backgroundImage = ImageIO.read(new File(BASE_PATH + imageFileName));
        	
        } catch(IOException e) {
        	e.printStackTrace();
        }
        
        this.zoom = zoom;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    public void setZoom(double zoom) {
        this.zoom = zoom;
        repaint();
    }
    
    public double getZoom() {
        return zoom;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int iw = backgroundImage.getWidth(this);
            int ih = backgroundImage.getHeight(this);
            if(iw > 0 && ih > 0) {
                // Calcular dimensiones escaladas según el factor de zoom.
                int scaledW = (int)(iw * zoom);
                int scaledH = (int)(ih * zoom);
                // Repetir la imagen escalada en X e Y.
                for (int x = 0; x < getWidth(); x += scaledW) {
                    for (int y = 0; y < getHeight(); y += scaledH) {
                        g.drawImage(backgroundImage, x, y, scaledW, scaledH, this);
                    }
                }
            }
        }
    }
}
