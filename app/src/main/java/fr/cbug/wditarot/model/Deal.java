package fr.cbug.wditarot.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Un tour de jeu = une distribution.
 */
public abstract class Deal implements Serializable {
    protected List<Player> players;
    protected Player dealer;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getDealer() {
        return dealer;
    }

    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    public abstract Map<Player, Integer> getScores();
}
