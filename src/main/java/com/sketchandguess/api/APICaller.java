package com.sketchandguess.api;

import java.util.concurrent.CompletableFuture;

public interface APICaller {
    CompletableFuture<String> call(byte[] imageData);
}
