package com.ruchij.web.responses;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public record DelayedResponse(long delay, TimeUnit timeUnit, Instant start, Instant end, long actualDelay) {
}
