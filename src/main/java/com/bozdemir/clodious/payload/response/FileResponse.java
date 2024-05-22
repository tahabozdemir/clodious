package com.bozdemir.clodious.payload.response;

import java.util.Date;
import java.util.UUID;

public record FileResponse(String name, String type, Date creationDate, UUID id) {
}
