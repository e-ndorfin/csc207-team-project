package com.sketchandguess.usecases.gameplay;

import com.sketchandguess.api.APICaller;
import com.sketchandguess.entities.Prediction;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameplayUseCase implements GameplayInputBoundary {
    private final APICaller apiCaller;
    private final GameplayOutputBoundary outputBoundary;

    public GameplayUseCase(APICaller apiCaller, GameplayOutputBoundary outputBoundary) {
        this.apiCaller = apiCaller;
        this.outputBoundary = outputBoundary;
    }

    @Override
    /**
     * Executes the gameplay use case.
     * 
     * @param inputData The input data containing the image and prompt (input data is 28x28)
     */

    public void execute(GameplayInputData inputData) {
        try {
            // Save original image (debugging)
            // BufferedImage originalImage = inputData.getImage();
            // String uuid = UUID.randomUUID().toString();
            // saveImageToFile(originalImage, "original", uuid, "ORIGINAL");
            
            // Use the image directly (canvas already exports at 28x28)
            BufferedImage resizedImage = inputData.getImage();
            
            // Invert colors (QuickDraw expects White strokes on Black background)
            BufferedImage invertedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < 28; x++) {
                for (int y = 0; y < 28; y++) {
                    int rgb = resizedImage.getRGB(x, y);  // output is in the form of ARGB (Alpha, Red, Green, Blue)

                    // Extract the RGB components
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    int a = (rgb >> 24) & 0xFF;
                    
                    // Invert each channel
                    int invertedR = 255 - r;
                    int invertedG = 255 - g;
                    int invertedB = 255 - b;
                    
                    // Merge back into ARGB format after inversion
                    int invertedPixel = (a << 24) | (invertedR << 16) | (invertedG << 8) | invertedB; 
                    invertedImage.setRGB(x, y, invertedPixel);
                }
            }

            // Convert to PNG byte array (API only accepts byte format)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(invertedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Call API asynchronously
            apiCaller.call(imageBytes)
                .thenApplyAsync(response -> {
                    // System.out.println("[DEBUG] API Response: " + response);

                    // Create list of Predictions from the API response
                    List<Prediction> predictions = parsePredictions(response);

                    // Check Win Condition
                    boolean hasWon = false;
                    if (!predictions.isEmpty()) {
                        String topPrediction = predictions.get(0).getLabel();
                        if (topPrediction.equalsIgnoreCase(inputData.getTargetPrompt())) {
                            hasWon = true;
                        }
                    }
                    // System.out.println("[DEBUG] Predictions: " + predictions);
                    // System.out.println("[DEBUG] Has Won: " + hasWon);
                    return new GameplayOutputData(predictions, hasWon);
                })
                .thenAcceptAsync(outputData -> {
                    outputBoundary.present(outputData);
                })
                .exceptionally(e -> {
                    System.err.println("GameplayUseCase error: " + e.getMessage());
                    GameplayOutputData outputData = new GameplayOutputData(new ArrayList<Prediction>(), false);
                    outputBoundary.present(outputData);
                    return null;
                });

        } catch (Exception e) {
            System.err.println("Error preparing API call: " + e.getMessage());
            GameplayOutputData outputData = new GameplayOutputData(new ArrayList<Prediction>(), false);
            outputBoundary.present(outputData);
        }
    }

    // private void saveImageToFile(BufferedImage image, String filenamePrefix, String uuid, String description) {
    //     try {
    //         File debugDir = new File("src/main/resources/debugging");
    //         if (!debugDir.exists()) {
    //             debugDir.mkdirs();
    //         }
    //         String fileName = filenamePrefix + "_" + uuid + ".png";
    //         File outputFile = new File(debugDir, fileName);
    //         ImageIO.write(image, "png", outputFile);
    //         System.out.println("[DEBUG] Saved " + description + " image to: " + outputFile.getAbsolutePath());
    //     } catch (Exception e) {
    //         System.err.println("[DEBUG] Error saving " + description + " image: " + e.getMessage());
    //     }
    // }

    /**
     * Parses the predictions from the JSON response and output list of Prediction objects
     * The JSON response is in the form of:
     * {
     *   "predictions": [
     *     {
     *       "label": "prediction",
     *       "confidence": 0.5
     *     },
     *     {
     *       "label": "prediction2",
     *       "confidence": 0.3
     *     },
     *     {
     *       "label": "prediction3",
     *       "confidence": 0.2
     *     }
     *   ]
     * }
     * @param jsonResponse The JSON response from the API
     * @return A list of predictions
     */

    private List<Prediction> parsePredictions(String jsonResponse) {
        List<Prediction> predictions = new ArrayList<>();
        try {
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                return predictions;
            }
            
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has("predictions")) {
                JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                
                // Parsethrough top 5 predictions
                for (int i = 0; i < Math.min(5, jsonArray.length()); i++) {  
                    JSONObject prediction = jsonArray.getJSONObject(i);
                    String label = null;
                    double confidence = 0.0;
                    
                    // Extract guess from prediction
                    if (prediction.has("label")) {
                        label = prediction.getString("label");
                    } else if (prediction.has("prediction")) {
                        label = prediction.getString("prediction");
                    }
                    
                    // Extract confidence from prediction 
                    if (prediction.has("confidence")) {
                        confidence = prediction.getDouble("confidence");
                    } else if (prediction.has("score")) {
                        confidence = prediction.getDouble("score");
                    }
                    
                    // Add prediction to list if label is not null
                    if (label != null) {
                        predictions.add(new Prediction(label, confidence));
                    }
                }
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return predictions;
    }
}