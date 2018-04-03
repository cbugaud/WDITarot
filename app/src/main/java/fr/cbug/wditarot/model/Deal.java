package fr.cbug.wditarot.model;

import java.util.List;

/**
 * Un tour de jeu = une distribution.
 */
public abstract class Deal {
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
}
