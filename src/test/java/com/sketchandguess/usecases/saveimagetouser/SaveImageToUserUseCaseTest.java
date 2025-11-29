package com.sketchandguess.usecases.saveimagetouser;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SaveImageToUserUseCaseTest {

    @Test
    void failureImageNotFoundTest() throws IOException {
        // Arrange
        SaveImageToUserUseCase useCase = new SaveImageToUserUseCase();
        String nonExistentPath = "nonexistent/image/path.png";
        
        // Act
        boolean result = useCase.save(nonExistentPath);
        
        // Assert
        assertFalse(result, "Should return false for non-existent image path");
    }

    @Test
    void failureImageReadErrorTest() throws IOException {
        // Arrange
        SaveImageToUserUseCase useCase = new SaveImageToUserUseCase();
        
        // Create a temporary file that is not a valid image
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, "This is not an image".getBytes());
        String invalidImagePath = tempFile.toString();
        
        try {
            // Act
            // Note: This will show a JOptionPane dialog for the read error.
            boolean result = useCase.save(invalidImagePath);
            
            // Assert
            assertFalse(result, "Should return false for unreadable image");
        } finally {
            // Cleanup
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void failureEmptyPathTest() throws IOException {
        // Arrange
        SaveImageToUserUseCase useCase = new SaveImageToUserUseCase();
        String emptyPath = "";
        
        // Act
        boolean result = useCase.save(emptyPath);
        
        // Assert
        assertFalse(result, "Should return false for empty path");
    }

    @Test
    void failureNullPathTest() throws IOException {
        // Arrange
        SaveImageToUserUseCase useCase = new SaveImageToUserUseCase();
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            useCase.save(null);
        }, "Should handle null path appropriately");
    }
}

