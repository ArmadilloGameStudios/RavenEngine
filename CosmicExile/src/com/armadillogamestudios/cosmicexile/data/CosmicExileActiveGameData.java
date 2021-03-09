package com.armadillogamestudios.cosmicexile.data;

import com.armadillogamestudios.cosmicexile.data.contract.Contract;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

import java.util.ArrayList;
import java.util.List;

public class CosmicExileActiveGameData implements GameDatable {
    private final List<Contract> contracts = new ArrayList<>();

    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    @Override
    public GameData toGameData() {
        return null;
    }
}
