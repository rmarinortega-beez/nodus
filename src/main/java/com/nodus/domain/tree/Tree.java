package com.nodus.domain.tree;

import java.util.List;

public record Tree(List<TreeEntry> entries) {
    public Tree {
        entries = List.copyOf(entries);
    }
}
