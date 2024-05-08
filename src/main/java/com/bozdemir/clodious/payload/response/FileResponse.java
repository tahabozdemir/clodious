package com.bozdemir.clodious.payload.response;

public record FileResponse(String name, String type, String path, Long userId) {
}
