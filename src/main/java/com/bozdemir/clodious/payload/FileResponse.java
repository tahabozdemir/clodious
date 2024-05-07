package com.bozdemir.clodious.payload;

public record FileResponse(String name, String type, String path, Long userId) {
}
