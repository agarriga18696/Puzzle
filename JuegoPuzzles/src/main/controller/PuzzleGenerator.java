package main.controller;

import main.model.Puzzle;
import main.model.PuzzlePiece;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PuzzleGenerator {

    // Método para recortar la imagen para que sus dimensiones sean múltiplos exactos de rows y cols.
    private static BufferedImage cropToMultiple(BufferedImage image, int rows, int cols) {
        int width = image.getWidth();
        int height = image.getHeight();
        int newWidth = (width / cols) * cols;
        int newHeight = (height / rows) * rows;
        return image.getSubimage(0, 0, newWidth, newHeight);
    }

    // Método para generar el puzzle con una imagen aleatoria.
    public static Puzzle generatePuzzle(int rows, int cols, String category) {
        // Cargar la imagen.
        BufferedImage fullImage = ImageProcessor.loadRandomImage(category);

        // Recortar la imagen para que sus dimensiones sean exactas.
        BufferedImage croppedImage = cropToMultiple(fullImage, rows, cols);

        // Crear la lista de piezas.
        List<PuzzlePiece> pieces = new ArrayList<>();
        int id = 0;
        
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                PuzzlePiece piece = new PuzzlePiece(id++, r, c);
                pieces.add(piece);
            }
        }

        // Segmentar la imagen en subimágenes rectangulares.
        segmentImage(croppedImage, rows, cols, pieces);

        // Crear y devolver el Puzzle.
        return new Puzzle(rows, cols, pieces);
    }

    // Método para segmentar la imagen en subimágenes rectangulares.
    private static void segmentImage(BufferedImage image, int rows, int cols, List<PuzzlePiece> pieces) {
        // Tamaño base de cada pieza.
        int w = image.getWidth() / cols;
        int h = image.getHeight() / rows;

        // Sobrantes (en caso de que la imagen no sea perfectamente divisible).
        int leftoverW = image.getWidth() % cols;
        int leftoverH = image.getHeight() % rows;

        // Asignar la subimagen rectangular a cada pieza.
        for(PuzzlePiece piece : pieces) {
            int r = piece.getCorrectRow();
            int c = piece.getCorrectCol();

            // Coordenadas de la subimagen en la imagen.
            int x = c * w;
            int y = r * h;

            // Si es la última columna o fila, sumar los sobrantes.
            int subW = (c == cols - 1) ? (w + leftoverW) : w;
            int subH = (r == rows - 1) ? (h + leftoverH) : h;

            // Asegurarse de no exceder los límites de la imagen.
            subW = Math.min(subW, image.getWidth() - x);
            subH = Math.min(subH, image.getHeight() - y);

            // Extraer la subimagen rectangular.
            BufferedImage subImage = image.getSubimage(x, y, subW, subH);

            // Asignar la subimagen como imagen original y actual de la pieza.
            piece.setOriginalImage(subImage);
            piece.setImage(subImage);
        }
    }
}
