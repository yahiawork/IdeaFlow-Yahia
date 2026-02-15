package com.yahia.ideaflow.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Minimal idea model.
 */
public final class Idea {
    public final String id;
    public String title;
    public String body;

    public Status status = Status.INBOX;

    public int urgency = 1; // 0..3
    public int impact = 1;  // 0..3
    public int effort = 1;  // 0..3
    public Energy energy = Energy.MED;

    public final LocalDateTime createdAt = LocalDateTime.now();

    public Idea(String title, String body) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.body = body;
    }

    public int score() {
        // Simple priority: urgency*2 + impact*3 - effort
        return urgency * 2 + impact * 3 - effort;
    }

    public enum Status { INBOX, PLANNING, DOING, DONE }
    public enum Energy { LOW, MED, HIGH }
}
