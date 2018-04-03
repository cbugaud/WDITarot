package fr.cbug.wditarot.model;

/**
 * Les annonces (misère, poignées, ...
 */
class Bonus {
    public enum BonusType {
        LACK /** Misère */ (false, false, true, 10), // TODO : muoltiplier du contrat ?
        HANDFUL /** Poignée */ (true, true, false, 20),
        DOUBLE_HANDFUL /** Double poignée */ (true, true, false, 40),
        TRIPLE_HANDFUL /** Triple poignée */ (true, true, false, 60),
        ONE_AT_END /** Petit au bout */ (true, false, true, 10),
        SIMONETTE (false, true, false, 0);
//        SLAM /** Grand chelem */,
//        LITTLE_SLAM /** Petit chelem*/;

        private boolean team;
        private boolean forWinnerTeam;
        private boolean multiplierApplied;
        private int bonusScore;

        BonusType(boolean team, boolean forWinnerTeam, boolean multiplierApplied, int bonusScore) {
            this.team = team;
            this.forWinnerTeam = forWinnerTeam;
            this.multiplierApplied = multiplierApplied;
            this.bonusScore = bonusScore;
        }

        public boolean isTeam() {
            return team;
        }

        public boolean isForWinnerTeam() {
            return forWinnerTeam;
        }

        public int getBonusScore() {
            return bonusScore;
        }

        public boolean isMultiplierApplied() {
            return multiplierApplied;
        }
    }

    private BonusType type;
    private Player player;

    public Bonus(BonusType type, boolean taker) {
        this.type = type;
        this.taker = taker;
    }

    private boolean taker;

    public boolean isTeamBonus() {
        return type.isTeam();
    }

    public int getAddedScoreForTaker(boolean takerWins, int stakeMultiplier) {
        boolean bonusForTaker;
        if (type.isForWinnerTeam())
            bonusForTaker = takerWins;
        else
            bonusForTaker = taker;

        int result = type.getBonusScore();
        if (!bonusForTaker)
            result *= -1;

        if (type.isMultiplierApplied())
            result *= stakeMultiplier;

        return result;
    }

}
