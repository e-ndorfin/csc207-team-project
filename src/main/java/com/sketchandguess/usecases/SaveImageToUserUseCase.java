package com.sketchandguess.usecases;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveImageToUserUseCase {
    /**
     * @param imagePath the path where the gallery save the image
     * @return true if saved, false if failed to save the image
     */
    public boolean save (String imagePath) throws IOException {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
            if (image == null) {
                JOptionPane.showMessageDialog(null, "Image not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Image cannot be read!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Image");
        chooser.setSelectedFile(new File("image.png"));
        int result = chooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        File file = chooser.getSelectedFile();

        if (!file.getName().endsWith(".png")) {
            file = new File(file.getAbsolutePath() + ".png");
        }
        try {
            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(null, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Image cannot be saved!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}