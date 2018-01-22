package com.raven.engine.util.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinder<N extends PathNode<N>> {

    public HashMap<N, Path<N>> findDistance(N start, int dist) {
        HashMap<N, Path<N>> nodeMap = new HashMap<>();
        List<Path<N>> unresolvedPaths = new ArrayList<>();

        List<PathAdjacentNode<N>> neighbors = start.getAdjacentNodes();

        for (PathAdjacentNode<N> n : neighbors) {
            Path<N> path = new Path<>();
            path.add(n);

            if (path.getCost() == dist) {
                nodeMap.put(n.getNode(), path);
            } else if (path.getCost() < dist) {
                nodeMap.put(n.getNode(), path);
                unresolvedPaths.add(path);
            }
        }

        findPaths(nodeMap, unresolvedPaths, dist);

        return nodeMap;
    }

    private void findPaths(HashMap<N, Path<N>> nodeMap, List<Path<N>> unresolvedPaths, int dist) {
        List<Path<N>> nextUnresolvedPaths = new ArrayList<>();

        for (Path<N> unresolvedPath : unresolvedPaths) {
            List<PathAdjacentNode<N>> neighbors = unresolvedPath.getLast().getNode().getAdjacentNodes();

            for (PathAdjacentNode<N> n : neighbors) {
                Path<N> oldPath = nodeMap.get(n);

                Path<N> path = new Path<>();
                path.addAll(unresolvedPath);
                path.add(n);

                int cost = path.getCost();

                if (oldPath == null || oldPath.getCost() > cost) {
                    if (cost == dist) {
                        nodeMap.put(n.getNode(), path);
                    } else if (cost < dist) {
                        nodeMap.put(n.getNode(), path);
                        nextUnresolvedPaths.add(path);
                    }
                }
            }
        }

        if (nextUnresolvedPaths.size() > 0) {
            findPaths(nodeMap, nextUnresolvedPaths, dist);
        }
    }

    public List<N> findTarget(N start, N target) {
        List<N> path = new ArrayList<>();

        return path;
    }
}
