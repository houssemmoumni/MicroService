package com.megaminds.material.handler;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
