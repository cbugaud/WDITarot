package fr.cbug.wditarot.model;

import com.google.common.collect.Lists;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Celine_Bugaud on 03/04/2018.
 */
public class PlayedDealTest {
    private static final Player SIMON = new Player("Simon");
    private static final Player THILL = new Player("Thill");
    private static final Player JULIEN = new Player("Julien");
    private static final Player ERWAN = new Player("Erwan");
    private static final Player AUDE = new Player("Aude");

    @Test
    public void computeRoundScore5Players() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN, AUDE, ERWAN));
        round.setBid(Bid.GUARD);
        round.setTaker(JULIEN);
        round.setPartner(AUDE);
        round.setTakerCardPoints(42);
        round.setTakerOudlersCount(2);

        round.computeRoundScore();

        assertEquals(104, round.getScores().get(JULIEN).intValue());
        assertEquals(52, round.getScores().get(AUDE).intValue());
        assertEquals(-52, round.getScores().get(SIMON).intValue());
        assertEquals(-52, round.getScores().get(THILL).intValue());
        assertEquals(-52, round.getScores().get(ERWAN).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void computeRoundScore5PlayersTakerLoose() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN, AUDE, ERWAN));
        round.setBid(Bid.TAKE);
        round.setTaker(JULIEN);
        round.setPartner(AUDE);
        round.setTakerCardPoints(42);
        round.setTakerOudlersCount(0);

        round.computeRoundScore();

        assertEquals(-78, round.getScores().get(JULIEN).intValue());
        assertEquals(-39, round.getScores().get(AUDE).intValue());
        assertEquals(39, round.getScores().get(SIMON).intValue());
        assertEquals(39, round.getScores().get(THILL).intValue());
        assertEquals(39, round.getScores().get(ERWAN).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void computeRoundScore5PlayersSimonette() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN, AUDE, ERWAN));
        round.setBid(Bid.GUARD_WITHOUT);
        round.setTaker(SIMON);
        round.setPartner(SIMON);
        round.setTakerCardPoints(42);
        round.setTakerOudlersCount(1);

        round.computeRoundScore();

        assertEquals(-544, round.getScores().get(SIMON).intValue());
        assertEquals(136, round.getScores().get(AUDE).intValue());
        assertEquals(136, round.getScores().get(JULIEN).intValue());
        assertEquals(136, round.getScores().get(THILL).intValue());
        assertEquals(136, round.getScores().get(ERWAN).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void computeRoundScore4Players() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN, AUDE));
        round.setBid(Bid.GUARD);
        round.setTaker(JULIEN);
        round.setTakerCardPoints(42);
        round.setTakerOudlersCount(3);

        round.computeRoundScore();

        assertEquals(186, round.getScores().get(JULIEN).intValue());
        assertEquals(-62, round.getScores().get(AUDE).intValue());
        assertEquals(-62, round.getScores().get(SIMON).intValue());
        assertEquals(-62, round.getScores().get(THILL).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void computeRoundScore3PlayersThillounette() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN));
        round.setBid(Bid.GUARD_AGAINST);
        round.setTaker(THILL);
        round.setTakerCardPoints(50);
        round.setTakerOudlersCount(1);

        round.computeRoundScore();

        assertEquals(-312, round.getScores().get(THILL).intValue());
        assertEquals(156, round.getScores().get(JULIEN).intValue());
        assertEquals(156, round.getScores().get(SIMON).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void computeRoundScore5PlayersOneAtEnd() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN, AUDE, ERWAN));
        round.setBid(Bid.GUARD);
        round.setTaker(JULIEN);
        round.setPartner(AUDE);
        round.setTakerCardPoints(42);
        round.setTakerOudlersCount(2);
        round.getBonuses().add(new Bonus(Bonus.BonusType.ONE_AT_END, true));

        round.computeRoundScore();

        assertEquals(144, round.getScores().get(JULIEN).intValue());
        assertEquals(72, round.getScores().get(AUDE).intValue());
        assertEquals(-72, round.getScores().get(SIMON).intValue());
        assertEquals(-72, round.getScores().get(THILL).intValue());
        assertEquals(-72, round.getScores().get(ERWAN).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void computeRoundScore3PlayersThillounetteHandfulOneAtEnd() throws Exception {
        PlayedDeal round = new PlayedDeal();
        round.setPlayers(Lists.newArrayList(SIMON, THILL, JULIEN));
        round.setBid(Bid.GUARD_AGAINST);
        round.setTaker(THILL);
        round.setTakerCardPoints(50);
        round.setTakerOudlersCount(1);
        round.getBonuses().add(new Bonus(Bonus.BonusType.HANDFUL, true));
        round.getBonuses().add(new Bonus(Bonus.BonusType.ONE_AT_END, true));

        round.computeRoundScore();

        assertEquals(-232, round.getScores().get(THILL).intValue());
        assertEquals(116, round.getScores().get(JULIEN).intValue());
        assertEquals(116, round.getScores().get(SIMON).intValue());

        assertEquals(0, round.getScores().values().stream().mapToInt(Integer::intValue).sum());
    }
}