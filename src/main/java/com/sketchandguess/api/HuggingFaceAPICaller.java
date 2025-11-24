package com.sketchandguess.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HuggingFaceAPICaller implements APICaller {
    private final APIHandler apiHandler;

    public HuggingFaceAPICaller(APIHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @Override
    public String call(byte[] imageData) throws IOException, InterruptedException {
        String boundary = "Boundary-" + UUID.randomUUID().toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiHandler.getInferenceUrl()))
                .header("Authorization", "Bearer " + apiHandler.getApiKey())
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(imageData, boundary))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status code: " + response.statusCode() + " and body: " + response.body());
        }

        return response.body();
    }

    private HttpRequest.BodyPublisher ofMimeMultipartData(byte[] imageData, String boundary) {
        List<byte[]> byteArrays = new ArrayList<>();

        String partHeader = "--" + boundary + "\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"\n" +
                "Content-Type: image/png\n\n";
        byteArrays.add(partHeader.getBytes(StandardCharsets.UTF_8));
        byteArrays.add(imageData);
        byteArrays.add("\n".getBytes(StandardCharsets.UTF_8));

        String partFooter = "--" + boundary + "--\n";
        byteArrays.add(partFooter.getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
