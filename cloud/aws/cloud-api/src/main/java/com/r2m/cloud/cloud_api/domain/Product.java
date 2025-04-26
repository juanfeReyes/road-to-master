package com.r2m.cloud.cloud_api.domain;

import java.util.UUID;

public record Product(UUID id, String name, int amount, double price) {
}
