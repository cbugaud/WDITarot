package fr.cbug.wditarot.model;

/**
 * Les différents types de contrat (prise, garde, ...)
 */
public enum Bid {
    TAKE(1), GUARD(2), GUARD_WITHOUT(4), GUARD_AGAINST(6);

    private int stakeMultiplier;

    Bid(int multiplier) {
      this.stakeMultiplier = multiplier;
    }

    public int getStakeMultiplier() {
        return stakeMultiplier;
    }
}
