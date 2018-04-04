package fr.cbug.wditarot.model;

import fr.cbug.wditarot.R;

/**
 * Les différents types de contrat (prise, garde, ...)
 */
public enum Bid {
    TAKE(1, R.string.bid_take),
    GUARD(2, R.string.bid_guard),
    GUARD_WITHOUT(4, R.string.bid_guard_without),
    GUARD_AGAINST(6, R.string.bid_guard_against);

    private int label;
    private int stakeMultiplier;

    Bid(int multiplier, int label) {
      this.stakeMultiplier = multiplier;
      this.label = label;
    }

    public int getStakeMultiplier() {
        return stakeMultiplier;
    }

    public int getLabel() {
        return label;
    }
}
