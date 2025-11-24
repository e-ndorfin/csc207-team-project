package com.sketchandguess;

import com.sketchandguess.api.APIHandler;
import com.sketchandguess.api.HuggingFaceAPICaller;

import java.io.File;
import java.nio.file.Files;

public class ManualAPITest {
    public static void main(String[] args) {
        String inferenceUrl = "https://zachttang-quickdraw.hf.space/predict";

        System.out.println("Using URL: " + inferenceUrl);

        try {
            APIHandler handler = new APIHandler(inferenceUrl);
            HuggingFaceAPICaller caller = new HuggingFaceAPICaller(handler);

            File imageFile = new File("src/main/resources/images/apple.png");
            if (!imageFile.exists()) {
                 System.err.println("Test image not found at: " + imageFile.getAbsolutePath());
                 return;
            }

            byte[] imageData = Files.readAllBytes(imageFile.toPath());
            System.out.println("Sending request to Hugging Face API...");
            String response = caller.call(imageData);
            System.out.println("Response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}