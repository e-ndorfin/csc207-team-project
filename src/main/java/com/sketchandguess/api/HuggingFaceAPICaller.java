package com.sketchandguess.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HuggingFaceAPICaller implements APICaller {
    private final APIHandler apiHandler;

    public HuggingFaceAPICaller(APIHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @Override
    public CompletableFuture<String> call(byte[] imageData) {
        String boundary = "Boundary-" + UUID.randomUUID().toString();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiHandler.getInferenceUrl()))
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(imageData, boundary))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("Request failed with status code: " + response.statusCode() + " and body: " + response.body());
                    }
                    return response.body();
                })
                .exceptionally(e -> {
                    // System.err.println("API call failed: " + e.getMessage());
                    return "[]";
                });
    }

    private HttpRequest.BodyPublisher ofMimeMultipartData(byte[] imageData, String boundary) {
        List<byte[]> byteArrays = new ArrayList<>();
        String CRLF = "\r\n";

        String partHeader = "--" + boundary + CRLF +
                "Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"" + CRLF +
                "Content-Type: image/png" + CRLF + CRLF;
        
        byteArrays.add(partHeader.getBytes(StandardCharsets.UTF_8));
        byteArrays.add(imageData);
        byteArrays.add(CRLF.getBytes(StandardCharsets.UTF_8));

        String partFooter = "--" + boundary + "--" + CRLF;
        byteArrays.add(partFooter.getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
