package fr.cbug.wditarot.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Un tour "passé" -> aucun score à en tirer.
 */
public class PassedDeal extends Deal {
    public PassedDeal(List<Player> players) {
        super(players);
    }

    @Override
    public Map<Player, Integer> getScores() {
        return new HashMap<>();
    }
}
