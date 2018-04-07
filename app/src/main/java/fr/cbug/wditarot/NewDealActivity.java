package fr.cbug.wditarot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import fr.cbug.wditarot.model.Bid;
import fr.cbug.wditarot.model.Bonus;
import fr.cbug.wditarot.model.CardColor;
import fr.cbug.wditarot.model.PlayedDeal;
import fr.cbug.wditarot.model.Player;

public class NewDealActivity extends AppCompatActivity {
    private static final String TAG = "newDealActivity";
    public static final String DEAL_KEY = "deal";
    private List<Player> players;
    private PlayedDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);

        this.players = (List<Player>) getIntent().getSerializableExtra(ShowScoresActivity.PLAYERS_KEY);

        deal = new PlayedDeal();
        deal.setPlayers(players);

        generateTakerButtons();
        generateBidButtons();
        manageCallSection();
        generateBonusesSection();
        generateOudlersCount();
        manageScoresSection();

        Button validate = findViewById(R.id.deal_validate_btn);
        validate.setOnClickListener(this::validate);
    }

    private void validate(View view) {
        String results = "";
        if (deal.completeData()) {
            deal.computeRoundScore();
            for (Map.Entry<Player, Integer> score : deal.getScores().entrySet())
                results += score.getKey() + " : " + score.getValue() + "\n";

            Toast.makeText(this, results, Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            intent.putExtra(DEAL_KEY, deal);
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(this, R.string.deal_incomplete_data, Toast.LENGTH_SHORT).show();
    }

    private void generatePartnerButtons() {
        RadioGroup takerButtonsLayout = findViewById(R.id.deal_call_partner_layout);

        for (Player player : players) {
            Button playerButton = createButton(true, player.getName(), (view, selected) -> selectPartner(view, selected, player));
            addToGroup(takerButtonsLayout, playerButton);
        }
    }

    private void generateCalledColorButtons() {
        RadioGroup colorButtonsLayout = findViewById(R.id.deal_call_color_layout);

        for (CardColor color : CardColor.values()) {
            Button button = createButton(true, color.getLabelRes(), (view, selected) -> selectCalledColor(view, selected, color));
            addToGroup(colorButtonsLayout, button);
        }
    }

    private void generateTakerButtons() {
        RadioGroup takerButtonsLayout = findViewById(R.id.deal_taker_layout);

        for (Player player : players) {
            Button playerButton = createButton(true, player.getName(), (view, selected) -> selectTaker(view, selected, player));
            addToGroup(takerButtonsLayout, playerButton);
        }
    }

    private void generateBidButtons() {
        RadioGroup bidButtonsLayout = findViewById(R.id.deal_bids_layout);

        for (Bid bid : Bid.values()) {
            Button button = createButton(true, bid.getLabel(), (view, selected) -> selectBid(view, selected, bid));
            addToGroup(bidButtonsLayout, button);
        }
    }

    private void manageCallSection() {
        ViewGroup callLayout = findViewById(R.id.deal_call_layout);

        if (players.size() == 5) {
            callLayout.setVisibility(LinearLayout.VISIBLE);
            generateCalledColorButtons();
            generatePartnerButtons();
        } else {
            callLayout.setVisibility(LinearLayout.GONE);
        }
    }

    private void generateBonusesSection() {
        generateLackPlayersButtons();
        generateAttackDefButtons(R.id.deal_handful_layout, Bonus.BonusType.HANDFUL);
        generateAttackDefButtons(R.id.deal_double_handful_layout, Bonus.BonusType.DOUBLE_HANDFUL);
        generateAttackDefButtons(R.id.deal_triple_handful_layout, Bonus.BonusType.TRIPLE_HANDFUL);
        generateAttackDefButtons(R.id.deal_one_at_end_layout, Bonus.BonusType.ONE_AT_END);
    }

    private void generateLackPlayersButtons() {
        ViewGroup lackButtonsLayout = findViewById(R.id.deal_lack_layout);

        for (Player player : players) {
            Button button = createButton(false, player.getName(), (view, selected) -> addPlayerBonus(view, selected, Bonus.BonusType.LACK, player));
            addToGroup(lackButtonsLayout, button);
        }
    }

    private void generateAttackDefButtons(int layoutId, Bonus.BonusType type) {
        ViewGroup layout = findViewById(layoutId);
        Button attack = createButton(false, R.string.attack, (view, selected) -> addTeamBonus(view, selected, type, true));
        Button defense = createButton(false, R.string.defense, (view, selected) -> addTeamBonus(view, selected, type, false));
        addToGroup(layout, attack);
        addToGroup(layout, defense);
    }

    private void generateOudlersCount() {
        ViewGroup layout = findViewById(R.id.deal_nb_oudlers_layout);
        for (int i = 0; i <= 3; i++) {
            final int oudlerCount = i;
            Button btn = createButton(true, String.valueOf(oudlerCount),
                    (view, selected) -> selectOudlersCount(view, selected, oudlerCount));
            addToGroup(layout, btn);
        }
    }

    private void manageScoresSection() {
        SeekBar defenseSeekBar = findViewById(R.id.deal_defense_score_bar);
        TextView defenseText = findViewById(R.id.deal_defense_score_text);

        SeekBar attackSeekBar = findViewById(R.id.deal_attack_score_bar);
        TextView attackText = findViewById(R.id.deal_attack_score_text);

        defenseSeekBar.setOnSeekBarChangeListener(new ScoreBarListener(false, defenseText, attackSeekBar, attackText));
        attackSeekBar.setOnSeekBarChangeListener(new ScoreBarListener(true, attackText, defenseSeekBar, defenseText));
    }

    private class ScoreBarListener implements SeekBar.OnSeekBarChangeListener {
        private boolean updateDeal;
        private TextView associatedText;
        private SeekBar otherBar;
        private TextView otherText;

        ScoreBarListener(boolean updateDeal, TextView associatedText, SeekBar otherBar, TextView otherText) {
            this.updateDeal = updateDeal;
            this.associatedText = associatedText;
            this.otherBar = otherBar;
            this.otherText = otherText;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
            if (updateDeal)
                deal.setTakerCardPoints(value);
            if (fromUser) {
                associatedText.setText(String.valueOf(value));
                int otherValue = 91 - value;
                otherBar.setProgress(otherValue);
                otherText.setText(String.valueOf(otherValue));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private void selectBid(View view, boolean selected, Bid bid) {
        if (selected) {
            deal.setBid(bid);
            Log.d(TAG, "Bid is "+bid);
        }
    }

    private void selectTaker(View view, boolean selected, Player player) {
        if (selected) {
            deal.setTaker(player);
            Log.d(TAG, "Taker is "+player.getName());
        }
    }

    private void selectCalledColor(CompoundButton view, boolean selected, CardColor color) {
        if (selected) {
            deal.setCalled(color);
            Log.d(TAG, "Called color is "+color);
        }
    }

    private void selectPartner(CompoundButton view, boolean selected, Player player) {
        if (selected) {
            deal.setPartner(player);
            Log.d(TAG, "Called player is "+player.getName());
        }
    }

    private void addPlayerBonus(CompoundButton view, boolean selected, Bonus.BonusType type, Player player) {
        Bonus playerBonus = null;
        for (Bonus bonus : deal.getBonuses())
            if (type.equals(bonus.getType()) && player.equals(bonus.getPlayer()))
                playerBonus = bonus;

        if (selected) {
            if (playerBonus == null)
                deal.getBonuses().add(new Bonus(type, player));
        } else if (playerBonus != null) {
            deal.getBonuses().remove(playerBonus);
        }
    }


    private void addTeamBonus(CompoundButton view, boolean selected, Bonus.BonusType type, boolean attack) {
        Bonus teamBonus = null;
        for (Bonus bonus : deal.getBonuses())
            if (type.equals(bonus.getType()) && attack == bonus.isTaker())
                teamBonus = bonus;

        if (selected) {
            if (teamBonus == null)
                deal.getBonuses().add(new Bonus(type, attack));
        } else if (teamBonus != null) {
            deal.getBonuses().remove(teamBonus);
        }
    }

    private void selectOudlersCount(CompoundButton view, boolean selected, int oudlerCount) {
        if (selected) {
            deal.setTakerOudlersCount(oudlerCount);
            Log.d(TAG, "Oudler count = "+oudlerCount);
        }
    }

    private Button createButton(boolean radio, String text, CompoundButton.OnCheckedChangeListener listener) {
        Button button = createButton(radio, listener);
        button.setText(text);
        return button;
    }
    private Button createButton(boolean radio, int textResId, CompoundButton.OnCheckedChangeListener listener) {
        Button button = createButton(radio, listener);
        button.setText(textResId);
        return button;
    }
    private Button createButton(boolean radio, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton button = radio ? new RadioButton(this) : new CheckBox(this);
        button.setOnCheckedChangeListener(listener);
        button.setBackgroundResource(R.drawable.my_radio_button_background);
        button.setButtonDrawable(null);
        return button;
    }

    private void addToGroup(ViewGroup group, View button) {
        ViewGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1);
        group.addView(button, layoutParams);
    }
}
