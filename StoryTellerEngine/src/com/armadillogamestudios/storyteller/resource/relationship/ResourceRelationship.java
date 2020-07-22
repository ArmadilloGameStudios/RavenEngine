package com.armadillogamestudios.storyteller.resource.relationship;

import com.armadillogamestudios.storyteller.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceRelationship {

    private static Map<Resource, List<ResourceRelationship>> manager = new HashMap<>();

    private Resource a, b;
    private Type type;
    private ResourceRelationship inverse;

    private ResourceRelationship(Resource a, Resource b, Type type) {
        this.a = a;
        this.b = b;
        this.type = type;
    }

    public static ResourceRelationship create(Resource a, Resource b, Type type) {
        ResourceRelationship prime = new ResourceRelationship(a, b, type);
        ResourceRelationship second = new ResourceRelationship(b, a, getInverse(type));

        prime.setInverse(second);
        second.setInverse(prime);

        addToManager(prime);
        addToManager(second);

        return prime;
    }

    private static void addToManager(ResourceRelationship relationship) {
        if (!manager.containsKey(relationship.a)) {
            manager.put(relationship.a, new ArrayList<>());
        }

        manager.get(relationship.a).add(relationship);
    }

    public static Type getInverse(Type type) {
        switch (type) {
            case CONNECTED:
                return Type.CONNECTED;

            case ON:
                return Type.UNDER;
            case UNDER:
                return Type.ON;

            case CONTAINS:
                return Type.PART_OF;
            case PART_OF:
                return Type.CONTAINS;

            case HAS:
                return Type.WITH;
            case WITH:
                return Type.HAS;
        }

        throw new IllegalArgumentException();
    }

    private void setInverse(ResourceRelationship inverse) {
        this.inverse = inverse;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        CONNECTED, ON, UNDER, CONTAINS, PART_OF, HAS, WITH
    }

}
