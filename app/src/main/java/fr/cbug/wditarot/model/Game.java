package fr.cbug.wditarot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Une partie de tarot.
 * Des joueurs, un certain nombre de donnes, des scores.
 */
public class Game implements Serializable {
    private List<Player> players;
    private List<Deal> deals;

    public Game(List<Player> players) {
        this.players = players;
        this.deals = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public Map<Player, Integer> totalScores() {
        Map<Player, Integer> result = new HashMap<>();

        // Que c'est triste de pas pouvoir utiliser les streams :'(
        for (Player player : players) {
            int score = 0;
            for (Deal deal : deals) {
                Integer dealScore = deal.getScores().get(player);
                if (dealScore != null)
                    score += dealScore;
            }
            result.put(player, score);
        }

        return result;
    }

    public void addDeal(int i, Deal newDeal) {
        deals.add(newDeal);
    }
}
