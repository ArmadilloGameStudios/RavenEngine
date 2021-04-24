package com.armadillogamestudios.engine2d.util.pathfinding;

import javax.swing.text.html.Option;
import java.util.*;

public class PathFinder<N> {

    private Optional<N> target = Optional.empty();

    public HashMap<N, Path<N>> findDistance(N start, Collection<N> targets, int dist, PathInterpreter<N> interpreter) {

        HashMap<N, Path<N>> nodeMap = new HashMap<>();
        HashMap<N, Path<N>> unresolvedPaths = new HashMap<>();

        List<PathAdjacentNode<N>> neighbors = interpreter.getAdjacentNodes(start);

        for (PathAdjacentNode<N> n : neighbors) {
            Path<N> path = new Path<>();
            path.add(new PathAdjacentNode<>(start, 0));
            path.add(n);

            if (targets != null && targets.contains(n.getNode())) {
                dist = path.getCost();
                target = Optional.of(n.getNode());
            }

            if (path.getCost() == dist) {
                nodeMap.put(n.getNode(), path);
            } else if (path.getCost() < dist) {
                nodeMap.put(n.getNode(), path);
                unresolvedPaths.put(n.getNode(), path);
            }
        }

        findPaths(nodeMap, unresolvedPaths, targets, dist, interpreter);

        return nodeMap;
    }

    private void findPaths(HashMap<N, Path<N>> nodeMap, HashMap<N, Path<N>> unresolvedPaths, Collection<N> targets, int dist, PathInterpreter<N> interpreter) {

        HashMap<N, Path<N>> nextUnresolvedPaths = new HashMap<>();

        for (Path<N> unresolvedPath : unresolvedPaths.values()) {
            List<PathAdjacentNode<N>> neighbors = interpreter.getAdjacentNodes(unresolvedPath.getLast().getNode());

            for (PathAdjacentNode<N> n : neighbors) {
                Path<N> oldPath = nodeMap.get(n.getNode());

                Path<N> path = new Path<>();
                path.addAll(unresolvedPath);
                path.add(n);

                int cost = path.getCost();

                if (oldPath == null || oldPath.getCost() > cost) {

                    if (targets != null && targets.contains(n.getNode()) && path.getCost() < dist) {
                        dist = path.getCost();
                        target = Optional.of(n.getNode());
                    }

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
            findPaths(nodeMap, nextUnresolvedPaths, targets, dist, interpreter);
        }
    }

    // TODO fix this shitty code - but does it work?
    // TODO needs early termination
    public Path<N> findTarget(N start, Collection<N> targets, PathInterpreter<N> interpreter) {
        HashMap<N, Path<N>> catMap = findDistance(start, targets, 10000, interpreter);

        if (target.isEmpty()) {
            target = targets.stream()
                    .min(Comparator.comparingInt(t -> catMap.get(t).getCost()));
        }

        Path<N> cat = null;
        if (target.isPresent()) {
            cat = catMap.get(target.get());
        }

        if (target.isPresent() && cat == null) {

            System.out.println("not found");

            Optional<Path<N>> maybeCat = interpreter.getAdjacentNodes(target.get()).stream()
                    .map(an -> catMap.get(an.getNode()))
                    .filter(Objects::nonNull)
                    .min(Comparator.comparingInt(Path::getCost));

            if (maybeCat.isPresent())
                cat = maybeCat.get();
        }

        return cat;
    }
}
