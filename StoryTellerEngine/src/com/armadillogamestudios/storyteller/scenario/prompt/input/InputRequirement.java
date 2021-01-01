package com.armadillogamestudios.storyteller.scenario.prompt.input;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Targetsable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class InputRequirement {

    protected final Targetsable input;

    protected InputRequirement(Targetsable input) {
        this.input = input;
    }

    public static InputRequirement create(GameData gameData, Targetsable input) {
        gameData.throwExceptionIfInvalid("or");

        if (gameData.has("all")) {
            return new InputRequirementAll(gameData, input);
        } else if (gameData.has("value")) {
            return new InputRequirementValue(gameData, input);
        } else if (gameData.has("min")) {
            return new InputRequirementMinMax(gameData, input);
        } else if (gameData.has("values")) {
            return new InputRequirementValues(gameData, input);
        } else if (gameData.has("any")) {
            return new InputRequirementAny(gameData, input);
        } else {
            return new InputRequirementHas(gameData, input);
        }
    }

    public abstract boolean met();
}

abstract class InputRequirementBasic extends InputRequirement {

    protected final String trait;
    protected int targetIndex;

    protected InputRequirementBasic(GameData gameData, Targetsable input) {
        super(input);

        gameData.ifHas("target",
                i -> this.targetIndex = i.asInteger(),
                () -> this.targetIndex = 0);

        trait = gameData.getString("trait");
    }

}

class InputRequirementValue extends InputRequirementBasic {

    private final Comp comp;
    private int value;

    public InputRequirementValue(GameData gameData, Targetsable input) {
        super(gameData, input);

        gameData.ifHas("value", gd -> value = gd.asInteger());

        switch (gameData.getString("comp")) {
            case "<=":
                comp = Comp.LE;
                break;
            case ">=":
                comp = Comp.GE;
                break;
            case "!":
                comp = Comp.NE;
                break;
            case "=":
            default:
                comp = Comp.EQ;
                break;
        }
    }

    @Override
    public boolean met() {
        Resource target = input.getTargets()[targetIndex];

        if (!target.hasTrait(trait)) return false;

        switch (comp) {
            case LE:
                return target.getTrait(trait).getValue() <= value;
            case GE:
                return target.getTrait(trait).getValue() >= value;
            case NE:
                return target.getTrait(trait).getValue() != value;
            case EQ:
                return target.getTrait(trait).getValue() == value;
        }

        return false;
    }

    private enum Comp {
        EQ, GE, LE, NE
    }
}

class InputRequirementMinMax extends InputRequirementBasic {

    private int min, max;

    protected InputRequirementMinMax(GameData gameData, Targetsable input) {
        super(gameData, input);

        min = gameData.getInteger("min");
        max = gameData.getInteger("max");
    }

    @Override
    public boolean met() {
        Resource target = input.getTargets()[targetIndex];

        int value = target.getTrait(trait).getValue();

        return value <= max && value >= min;
    }
}

class InputRequirementHas extends InputRequirementBasic {

    private final boolean has;

    protected InputRequirementHas(GameData gameData, Targetsable input) {
        super(gameData, input);

        if (gameData.has("comp"))
            has = !gameData.getString("comp").equals("!");
        else
            has = true;
    }

    @Override
    public boolean met() {
        Resource target = input.getTargets()[targetIndex];

        return target.hasTrait(trait) == has;
    }
}

class InputRequirementValues extends InputRequirementBasic {

    private Set<Integer> values;

    protected InputRequirementValues(GameData gameData, Targetsable input) {
        super(gameData, input);

        values = gameData.getList("values").stream().map(GameData::asInteger).collect(Collectors.toSet());
    }

    @Override
    public boolean met() {
        Resource target = input.getTargets()[targetIndex];

        return values.contains(target.getTrait(trait).getValue());
    }
}

class InputRequirementAny extends InputRequirement {

    private List<InputRequirement> values;

    protected InputRequirementAny(GameData gameData, Targetsable input) {
        super(input);

        values = gameData.getList("any").stream().map(gd -> InputRequirement.create(gd, input)).collect(Collectors.toList());
    }

    @Override
    public boolean met() {
        return values.stream().anyMatch(InputRequirement::met);
    }
}

class InputRequirementAll extends InputRequirement {

    private List<InputRequirement> values;

    protected InputRequirementAll(GameData gameData, Targetsable input) {
        super(input);

        values = gameData.getList("all").stream().map(gd -> InputRequirement.create(gd, input)).collect(Collectors.toList());
    }

    @Override
    public boolean met() {
        return values.stream().allMatch(InputRequirement::met);
    }
}
