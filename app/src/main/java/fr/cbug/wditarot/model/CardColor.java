package fr.cbug.wditarot.model;

import fr.cbug.wditarot.R;

/**
 * Couleurs des cartes (pique, coeur, ...)
 * TODO : images au lieu des labels ...
 */
public enum CardColor {
    HEARTS(R.string.heart),
    DIAMONDS(R.string.diamond),
    SPADES(R.string.spade),
    CLUBS(R.string.club);

    private int labelRes;

    CardColor(int labelRes) {
        this.labelRes = labelRes;
    }

    public int getLabelRes() {
        return labelRes;
    }
}
