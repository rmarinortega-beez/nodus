package com.nodus.domain.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Tree(List<TreeEntry> entries) {
    public Tree {
        entries = entries.stream()
                .sorted((left, right) -> left.name().compareTo(right.name()))
                .toList();
        Set<String> names = new HashSet<>();

        for (TreeEntry entry : entries) {
            if (!names.add(entry.name())) {
                throw new IllegalArgumentException("Tree contains duplicate entry name: " + entry.name());
            }
        }
    }
}
