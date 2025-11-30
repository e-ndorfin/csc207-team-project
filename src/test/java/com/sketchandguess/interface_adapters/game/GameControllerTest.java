package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.usecases.gameplay.GameplayInputBoundary;
import com.sketchandguess.usecases.recordgame.RecordGameInputBoundary;
import com.sketchandguess.usecases.recordgame.RecordGameInputData;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void testExecuteGameResultCallsInteractor() {
        // Arrange
        MockRecordGameInputBoundary recordInteractor = new MockRecordGameInputBoundary();
        MockGameplayInputBoundary gameplayInteractor = new MockGameplayInputBoundary();
        GameController controller = new GameController(recordInteractor, gameplayInteractor);

        BufferedImage dummyFullSizeImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        BufferedImage dummyDownsampledImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        String prompt = "Test Prompt";
        Difficulty difficulty = new Difficulty("Hard");
        double timeTaken = 30.0;
        double timeLimit = 60.0;
        boolean hasWon = true;

        // Act
        controller.executeGameResult(dummyFullSizeImage, dummyDownsampledImage, prompt, difficulty, timeTaken, timeLimit, hasWon);

        // Assert
        assertNotNull(recordInteractor.capturedInputData);
        assertEquals(prompt, recordInteractor.capturedInputData.prompt);
        assertEquals(difficulty, recordInteractor.capturedInputData.difficulty);
        assertEquals(timeTaken, recordInteractor.capturedInputData.timeTaken);
        assertEquals(timeLimit, recordInteractor.capturedInputData.timeLimit);
        assertEquals(hasWon, recordInteractor.capturedInputData.hasWon);
        assertNotNull(recordInteractor.capturedInputData.fullSizeImage);
        assertNotNull(recordInteractor.capturedInputData.downsampledImage);
        assertEquals(dummyFullSizeImage, recordInteractor.capturedInputData.fullSizeImage);
        assertEquals(dummyDownsampledImage, recordInteractor.capturedInputData.downsampledImage);
    }

    static class MockRecordGameInputBoundary implements RecordGameInputBoundary {
        RecordGameInputData capturedInputData;

        @Override
        public void execute(RecordGameInputData inputData) {
            this.capturedInputData = inputData;
        }
    }

    static class MockGameplayInputBoundary implements GameplayInputBoundary {
        @Override
        public void execute(com.sketchandguess.usecases.gameplay.GameplayInputData inputData) {
            // Mock implementation - not used in this test
        }
    }
}
