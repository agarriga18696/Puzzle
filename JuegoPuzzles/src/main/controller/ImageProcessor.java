package main.controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageProcessor {

    private static final String IMAGE_PATH = "src/resources/images/puzzle/";

    // Carga una imagen aleatoria.
    public static BufferedImage loadRandomImage(String category) {
        File dir = new File(IMAGE_PATH + category);
        File[] images = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if(images == null || images.length == 0) {
            throw new RuntimeException("No se han encontrado imágenes en " + IMAGE_PATH + category);
        }

        File selectedImage = images[new Random().nextInt(images.length)];
        try {
            return ImageIO.read(selectedImage);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la imagen: " + selectedImage.getName(), e);
        }
    }

}
