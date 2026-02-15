package com.yahia.ideaflow.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory store for demo.
 * Easy to replace with file or DB later.
 */
public final class IdeaStore {

    private final List<Idea> ideas = new ArrayList<>();

    public IdeaStore() {
        seed();
    }

    public List<Idea> all() {
        return Collections.unmodifiableList(ideas);
    }

    public void addIdea(Idea idea) {
        ideas.add(0, idea);
    }

    public void removeIdea(String id) {
        ideas.removeIf(i -> i.id.equals(id));
    }

    public Idea getById(String id) {
        for (Idea i : ideas) if (i.id.equals(id)) return i;
        return null;
    }

    public List<Idea> search(String query) {
        if (query == null) return new ArrayList<>(ideas);
        String q = query.trim().toLowerCase(Locale.ROOT);
        if (q.isEmpty()) return new ArrayList<>(ideas);
        return ideas.stream()
                .filter(i -> i.title.toLowerCase(Locale.ROOT).contains(q)
                          || i.body.toLowerCase(Locale.ROOT).contains(q))
                .collect(Collectors.toList());
    }

    public List<Idea> byStatus(Idea.Status status) {
        return ideas.stream().filter(i -> i.status == status).collect(Collectors.toList());
    }

    private void seed() {
        Idea a = new Idea("Build Java Swing IdeaFlow", "Modern UI, smooth cards, sidebar, search.");
        a.urgency = 2; a.impact = 3; a.effort = 2;
        ideas.add(a);

        Idea b = new Idea("Math revision plan", "Split chapters into small steps and do daily picks.");
        b.urgency = 3; b.impact = 2; b.effort = 1;
        ideas.add(b);

        Idea c = new Idea("Game prototype: 2D platformer", "Core movement + 1 level + polish.");
        c.urgency = 1; c.impact = 3; c.effort = 3;
        ideas.add(c);

        Idea d = new Idea("Sorting Session feature", "5-minute session: keep/delete/category/next action.");
        d.urgency = 2; d.impact = 2; d.effort = 1;
        ideas.add(d);
    }
}
