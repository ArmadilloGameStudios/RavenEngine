package com.raven.engine2d.util.pathfinding;

import java.util.*;

public class PathFinder<N extends com.raven.engine2d.util.pathfinding.PathNode<N>> {

    public HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> findDistance(N start, int dist) {
//        dist += 1; // take the starting cost into account

        HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> nodeMap = new HashMap<>();
        HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> unresolvedPaths = new HashMap<>();

        List<PathAdjacentNode<N>> neighbors = start.getAdjacentNodes();

        for (PathAdjacentNode<N> n : neighbors) {
            com.raven.engine2d.util.pathfinding.Path<N> path = new com.raven.engine2d.util.pathfinding.Path<>();
            path.add(new PathAdjacentNode<>(start, 0));
            path.add(n);

            if (path.getCost() == dist) {
                nodeMap.put(n.getNode(), path);
            } else if (path.getCost() < dist) {
                nodeMap.put(n.getNode(), path);
                unresolvedPaths.put(n.getNode(), path);
            }
        }

        findPaths(nodeMap, unresolvedPaths, dist);

        return nodeMap;
    }

    private void findPaths(HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> nodeMap, HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> unresolvedPaths, int dist) {

        HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> nextUnresolvedPaths = new HashMap<>();

        for (com.raven.engine2d.util.pathfinding.Path<N> unresolvedPath : unresolvedPaths.values()) {
            List<PathAdjacentNode<N>> neighbors = unresolvedPath.getLast().getNode().getAdjacentNodes();

            for (PathAdjacentNode<N> n : neighbors) {
                com.raven.engine2d.util.pathfinding.Path<N> oldPath = nodeMap.get(n.getNode());

                com.raven.engine2d.util.pathfinding.Path<N> path = new com.raven.engine2d.util.pathfinding.Path<>();
                path.addAll(unresolvedPath);
                path.add(n);

                int cost = path.getCost();

                if (oldPath == null || oldPath.getCost() > cost) {

                    if (cost == dist) {
                        nodeMap.put(n.getNode(), path);
                    } else if (cost < dist) {
                        nodeMap.put(n.getNode(), path);
                        nextUnresolvedPaths.put(n.getNode(), path);
                    }
                }
            }
        }

        if (nextUnresolvedPaths.size() > 0) {
            findPaths(nodeMap, nextUnresolvedPaths, dist);
        }
    }

    // TODO fix this shitty code
    public com.raven.engine2d.util.pathfinding.Path<N> findTarget(N start, N target) {
        HashMap<N, com.raven.engine2d.util.pathfinding.Path<N>> catMap = findDistance(start, 100);

        com.raven.engine2d.util.pathfinding.Path<N> cat = catMap.get(target);

        if (cat == null) {
            Optional<com.raven.engine2d.util.pathfinding.Path<N>> maybeCat = target.getAdjacentNodes().stream()
                    .map(an -> catMap.get(an.getNode()))
                    .filter(Objects::nonNull)
                    .min(Comparator.comparingInt(com.raven.engine2d.util.pathfinding.Path::getCost));

            if (maybeCat.isPresent())
                cat = maybeCat.get();
        }

        return cat;
    }
}
