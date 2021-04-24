package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.reclaim.data.Region;

import java.util.Arrays;
import java.util.List;

public class RegionConnection {

    private Region a, b;

    public RegionConnection(Region a, Region b) {
        this.a = a;
        this.b = b;
    }

    public int getBiomeValue() {
        return a.getBiomeValue() + b.getBiomeValue() + 1;
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

    public List<Region> asList() {
        return Arrays.asList(a, b);
    }
}
