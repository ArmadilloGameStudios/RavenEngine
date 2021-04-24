package com.armadillogamestudios.engine2d.util.pathfinding;

import java.util.ArrayList;

public class Path<N> extends ArrayList<PathAdjacentNode<N>> {

    public int getCost() {
        return stream().mapToInt(PathAdjacentNode::getCost).sum();
    }

    public PathAdjacentNode<N> getLast() {
        return get(size() - 1);
    }

//    public boolean contains(N o) {
//        return stream().anyMatch(n -> n.getNode() == o);
//    }

    public int getCostTo(N node) {
        int i = 0;

        for (PathAdjacentNode<N> pan : this) {
            i += pan.getCost();

            if (pan.getNode() == node) {
                return i;
            }
        }

        throw new UnsupportedOperationException();
    }
}
