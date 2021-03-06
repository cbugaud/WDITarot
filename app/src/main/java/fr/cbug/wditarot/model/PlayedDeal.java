package fr.cbug.wditarot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Un tour joué : quelqu'un a pris un contrat, appelé un roi, des annonces ont été faites, le score final a été calculé.
 */
public class PlayedDeal extends Deal {
    /** Scores minimums pour l'attaque en fonction du nombre de bouts (0, 1, 2, 3) */
    private static final int[] WINNING_FLOORS = {56, 51, 41, 36};
    private static final int BASIC_SCORE = 25;

    private Bid bid;
    private Player taker;
    private CardColor called;
    private Player partner;
    private List<Bonus> bonuses = new ArrayList<>();
    private int takerOudlersCount = -1;
    private int takerCardPoints = -1;
    private Map<Player, Integer> scores;

    public PlayedDeal(List<Player> players) {
        super(players);
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public Player getTaker() {
        return taker;
    }

    public void setTaker(Player taker) {
        this.taker = taker;
    }

    public CardColor getCalled() {
        return called;
    }

    public void setCalled(CardColor called) {
        this.called = called;
    }

    public Player getPartner() {
        return partner;
    }

    public void setPartner(Player partner) {
        this.partner = partner;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public int getTakerOudlersCount() {
        return takerOudlersCount;
    }

    public void setTakerOudlersCount(int takerOudlersCount) {
        this.takerOudlersCount = takerOudlersCount;
    }

    public int getTakerCardPoints() {
        return takerCardPoints;
    }

    public void setTakerCardPoints(int takerCardPoints) {
        this.takerCardPoints = takerCardPoints;
    }

    public Map<Player, Integer> getScores() {
        if (scores == null) {
            if (completeData())
                computeDealScore();
            else
                throw new IllegalStateException("Can't compute scores, data are not complete");
        }
        return scores;
    }

    public boolean completeData() {
        return players != null && players.size() >= MIN_PLAYERS_COUNT && players.size() <= MAX_PLAYERS_COUNT
                && taker != null
                && bid != null
                && (players.size() != 5 || called != null && partner != null)
                && takerOudlersCount >= 0
                && takerCardPoints >= 0;
    }

    public void computeDealScore() {
        scores = new HashMap<>();

        int score = computeBaseScoreForTaker();

        List<Player> defenseTeam = defenseTeam();
        int takerMultiplier = defenseTeam.size();

        if (partner != null && !partner.equals(taker)) {
            scores.put(partner, score);
            takerMultiplier -= 1;
        }

        scores.put(taker, score * takerMultiplier);
        for (Player player : defenseTeam)
            scores.put(player, score * -1);

        applyIndividualBonuses(scores);
    }

    private List<Player> defenseTeam() {
        List<Player> list = new ArrayList<>();
        for (Player p : players) {
            if (!p.equals(taker) && !p.equals(partner)) {
                list.add(p);
            }
        }
        return list;
    }

    private int computeBaseScoreForTaker() {
        int result = BASIC_SCORE;

        if (!takerWins())
            result *= -1;
        result += takerCardPoints - winningFloor();

        result += teamBonus(true);

        result *= bid.getStakeMultiplier();

        result += teamBonus(false);

        return result;
    }

    private int teamBonus(boolean beforeMultiplier) {
        int result = 0;
        for (Bonus bonus : bonuses) {
            if (bonus.isTeamBonus() && (bonus.getType().isMultiplierApplied() == beforeMultiplier))
                result += bonus.getAddedScoreForTaker(takerWins());
        }
        return result;
    }

    private void applyIndividualBonuses(Map<Player, Integer> scores) {
        for (Bonus bonus : bonuses) {
            if (!bonus.isTeamBonus()) {
                Player bonusPlayer = bonus.getPlayer();
                int addedScore = bonus.getType().getBonusScore();
                if (bonus.getType().isMultiplierApplied())
                    addedScore *= bid.getStakeMultiplier();

                for (Player player : players) {
                    if (player.equals(bonusPlayer)) {
                        int scoreForPlayer = addedScore * (players.size() - 1);
                        scores.put(player, scores.get(player) + scoreForPlayer);
                    } else {
                        scores.put(player, scores.get(player) - addedScore);
                    }
                }
            }
        }
    }

    private boolean takerWins() {
        return takerCardPoints >= winningFloor();
    }
    private int winningFloor() {
        return WINNING_FLOORS[takerOudlersCount];
    }

    @Override
    public String toString() {
        return "Players : "+players + "\n"
                + "Taker : "+taker + "\n"
                + "Bid : "+bid + "\n"
                + "Partner : "+partner + "\n"
                + "Called : "+called + "\n"
                + "Oudlers count : "+takerOudlersCount + "\n"
                + "Card points : "+takerCardPoints;
    }
}
