package com.sketchandguess.usecases.gameplay;

import com.sketchandguess.api.APICaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameplayUseCaseTest {

    private MockAPICaller mockAPICaller;
    private MockGameplayOutputBoundary mockOutputBoundary;
    private GameplayUseCase gameplayUseCase;
    private BufferedImage testImage;

    @BeforeEach
    void setUp() {
        mockAPICaller = new MockAPICaller();
        mockOutputBoundary = new MockGameplayOutputBoundary();
        gameplayUseCase = new GameplayUseCase(mockAPICaller, mockOutputBoundary);
        
        // Create a simple test image (white 100x100 image)
        testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                testImage.setRGB(x, y, 0xFFFFFF); // White
            }
        }
    }

    @Test
    void testWinCondition_WhenTopPredictionMatches() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API response with matching top prediction
        String jsonResponse = "{\"predictions\": [{\"label\": \"cat\", \"score\": 0.95}, {\"label\": \"dog\", \"score\": 0.05}]}";
        mockAPICaller.setResponse(jsonResponse);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion (max 5 seconds)
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertTrue(mockOutputBoundary.lastOutputData.hasWon(), "Should have won when top prediction matches");
        assertEquals(2, mockOutputBoundary.lastOutputData.getPredictions().size());
        assertEquals("cat", mockOutputBoundary.lastOutputData.getPredictions().get(0).getLabel());
    }

    @Test
    void testLossCondition_WhenTopPredictionDoesNotMatch() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API response with non-matching top prediction
        String jsonResponse = "{\"predictions\": [{\"label\": \"dog\", \"score\": 0.95}, {\"label\": \"bird\", \"score\": 0.05}]}";
        mockAPICaller.setResponse(jsonResponse);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertFalse(mockOutputBoundary.lastOutputData.hasWon(), "Should not have won when top prediction doesn't match");
        assertEquals(2, mockOutputBoundary.lastOutputData.getPredictions().size());
        assertEquals("dog", mockOutputBoundary.lastOutputData.getPredictions().get(0).getLabel());
    }

    @Test
    void testCaseInsensitiveMatching() throws InterruptedException {
        // Arrange
        String targetPrompt = "CAT"; // Uppercase
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API response with lowercase matching prediction
        String jsonResponse = "{\"predictions\": [{\"label\": \"cat\", \"score\": 0.95}]}";
        mockAPICaller.setResponse(jsonResponse);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertTrue(mockOutputBoundary.lastOutputData.hasWon(), "Should have won with case-insensitive matching");
    }

    @Test
    void testEmptyPredictions_ResultsInLoss() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API response with empty predictions
        String jsonResponse = "{\"predictions\": []}";
        mockAPICaller.setResponse(jsonResponse);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertFalse(mockOutputBoundary.lastOutputData.hasWon(), "Should not have won with empty predictions");
        assertTrue(mockOutputBoundary.lastOutputData.getPredictions().isEmpty());
    }

    @Test
    void testMultiplePredictions_ParsesTopFive() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API response with multiple predictions
        String jsonResponse = "{\"predictions\": [{\"label\": \"cat\", \"score\": 0.95}, " +
                              "{\"label\": \"dog\", \"score\": 0.80}, " +
                              "{\"label\": \"bird\", \"score\": 0.70}, " +
                              "{\"label\": \"fish\", \"score\": 0.60}, " +
                              "{\"label\": \"mouse\", \"score\": 0.50}, " +
                              "{\"label\": \"rabbit\", \"score\": 0.40}]}";
        mockAPICaller.setResponse(jsonResponse);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        List<com.sketchandguess.entities.Prediction> predictions = mockOutputBoundary.lastOutputData.getPredictions();
        assertEquals(5, predictions.size(), "Should parse top 5 predictions");
        assertEquals("cat", predictions.get(0).getLabel());
        assertEquals("dog", predictions.get(1).getLabel());
        assertEquals("bird", predictions.get(2).getLabel());
        assertEquals("fish", predictions.get(3).getLabel());
        assertEquals("mouse", predictions.get(4).getLabel());
        assertTrue(mockOutputBoundary.lastOutputData.hasWon(), "Should have won when first prediction matches");
    }

    @Test
    void testJsonObjectFormat_WithPredictionsKey() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API response in object format with "predictions" key
        String jsonResponse = "{\"predictions\": [{\"label\": \"cat\", \"score\": 0.95}]}";
        mockAPICaller.setResponse(jsonResponse);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertEquals(1, mockOutputBoundary.lastOutputData.getPredictions().size());
        assertEquals("cat", mockOutputBoundary.lastOutputData.getPredictions().get(0).getLabel());
        assertTrue(mockOutputBoundary.lastOutputData.hasWon());
    }

    @Test
    void testApiError_HandlesGracefully() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API to throw an exception
        mockAPICaller.setShouldThrowException(true);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertFalse(mockOutputBoundary.lastOutputData.hasWon(), "Should not have won on API error");
        assertTrue(mockOutputBoundary.lastOutputData.getPredictions().isEmpty(), "Should have empty predictions on error");
    }

    @Test
    void testNullResponse_HandlesGracefully() throws InterruptedException {
        // Arrange
        String targetPrompt = "cat";
        GameplayInputData inputData = new GameplayInputData(testImage, targetPrompt);
        
        // Mock API to return null
        mockAPICaller.setResponse(null);
        
        CountDownLatch latch = new CountDownLatch(1);
        mockOutputBoundary.setLatch(latch);

        // Act
        gameplayUseCase.execute(inputData);
        
        // Wait for async completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Test timed out waiting for async completion");

        // Assert
        assertNotNull(mockOutputBoundary.lastOutputData);
        assertFalse(mockOutputBoundary.lastOutputData.hasWon(), "Should not have won with null response");
        assertTrue(mockOutputBoundary.lastOutputData.getPredictions().isEmpty());
    }

    // Mock classes
    static class MockAPICaller implements APICaller {
        private String response;
        private boolean shouldThrowException = false;

        public void setResponse(String response) {
            this.response = response;
        }

        public void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        @Override
        public CompletableFuture<String> call(byte[] imageData) {
            CompletableFuture<String> future = new CompletableFuture<>();
            
            if (shouldThrowException) {
                future.completeExceptionally(new RuntimeException("API call failed"));
            } else {
                future.complete(response);
            }
            
            return future;
        }
    }

    static class MockGameplayOutputBoundary implements GameplayOutputBoundary {
        private GameplayOutputData lastOutputData;
        private CountDownLatch latch;

        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void present(GameplayOutputData outputData) {
            this.lastOutputData = outputData;
            if (latch != null) {
                latch.countDown();
            }
        }

        public GameplayOutputData getLastOutputData() {
            return lastOutputData;
        }
    }
}

