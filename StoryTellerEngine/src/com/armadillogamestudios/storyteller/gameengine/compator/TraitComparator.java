package com.armadillogamestudios.storyteller.gameengine.compator;

import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.resource.trait.Trait;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TraitComparator implements Comparator<Trait> {

    private HashMap<String, Integer> map = new HashMap<>();
    private int last;

    public TraitComparator(List<String> order) {

        for (int i = 0; i < order.size(); i++) {
            map.put(order.get(i), i);
        }

        last = order.size();
    }

    @Override
    public int compare(Trait t0, Trait t1) {
        int v0, v1;

        if (map.containsKey(t0.getTraitDescription().getType())) {
            v0 = map.get(t0.getTraitDescription().getType());
        } else {
            v0 = last;
        }

        if (map.containsKey(t1.getTraitDescription().getType())) {
            v1 = map.get(t1.getTraitDescription().getType());
        } else {
            v1 = last;
        }

        if (v0 != v1) {
            return v0 - v1;
        }

        return t0.getTraitDescription().getKey().compareTo(t1.getTraitDescription().getKey());
    }
}
