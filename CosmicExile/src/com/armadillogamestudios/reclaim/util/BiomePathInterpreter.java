package com.armadillogamestudios.reclaim.util;

import com.armadillogamestudios.engine2d.util.pathfinding.PathAdjacentNode;
import com.armadillogamestudios.engine2d.util.pathfinding.PathInterpreter;
import com.armadillogamestudios.reclaim.data.Region;

import java.util.List;
import java.util.stream.Collectors;

public class BiomePathInterpreter implements PathInterpreter<Region> {
    @Override
    public List<PathAdjacentNode<Region>> getAdjacentNodes(Region node) {
        return node.getRegionConnections().stream()
                .map(rc -> new PathAdjacentNode<>(rc.getOther(node), rc.getBiomeValue()))
                .collect(Collectors.toList());
    }
}
