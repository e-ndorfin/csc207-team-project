package com.sketchandguess.api;

import java.io.IOException;

public interface APICaller {
    String call(byte[] imageData) throws IOException, InterruptedException;
}
