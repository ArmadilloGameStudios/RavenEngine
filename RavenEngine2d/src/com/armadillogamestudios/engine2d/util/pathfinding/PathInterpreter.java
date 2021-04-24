package com.armadillogamestudios.engine2d.util.pathfinding;

import java.util.List;

public interface PathInterpreter<N> {
    List<PathAdjacentNode<N>> getAdjacentNodes(N node);
}
