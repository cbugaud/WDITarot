package fr.cbug.wditarot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Un tour de jeu = une distribution.
 */
public abstract class Deal implements Serializable {
    public static final int MAX_PLAYERS_COUNT = 5;
    public static final int MIN_PLAYERS_COUNT = 3;

    protected List<Player> players;
    protected Player dealer;

    public Deal(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

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

    public boolean attackShouldHavePartner() {
        return players.size() == 5;
    }
}
