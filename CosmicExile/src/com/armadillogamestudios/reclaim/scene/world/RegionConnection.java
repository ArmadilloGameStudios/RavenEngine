package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.reclaim.data.Region;

public class RegionConnection {

    private Region a, b;

    public RegionConnection(Region a, Region b) {
        this.a = a;
        this.b = b;
    }

    public int getTravelDifficulty(Region.PathFlag flag) {
        if (flag == Region.PathFlag.Adjacent) {
            return a.getTravelDifficulty() + b.getTravelDifficulty();
        } else {
            throw new IllegalStateException();
        }
    }

    public Region getA() {
        return a;
    }

    public Region getB() {
        return b;
    }

    public Region getOther(Region region) {
        if (a.equals(region)) {
            return b;
        } else {
            return a;
        }
    }
}
