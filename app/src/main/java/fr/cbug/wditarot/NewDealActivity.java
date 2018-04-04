package fr.cbug.wditarot;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.common.collect.Lists;

import java.util.List;

import fr.cbug.wditarot.model.Bid;
import fr.cbug.wditarot.model.PlayedDeal;
import fr.cbug.wditarot.model.Player;

public class NewDealActivity extends AppCompatActivity {
    private static final String TAG = "newDealActivity";
    private List<Player> players = Lists.newArrayList(new Player("Erwan"), new Player("Céline"), new Player("Julien"), new Player("Simon"), new Player("Aude"));
    private PlayedDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);

        deal = new PlayedDeal();
        generateTakerButtons();
        generateBidButtons();
//        generateCalleeButtons();
//        generateBonusesButtons();
    }

    private void generateTakerButtons() {
        Log.d(TAG, "Create taker buttons");
        RadioGroup takerButtonsLayout = findViewById(R.id.deal_taker_layout);

        for (Player player : players) {
            RadioButton playerButton = createRadioButton(player.getName(), (view, selected) -> selectTaker(view, selected, player));
            addRadioToGroup(takerButtonsLayout, playerButton);
        }
    }

    private void generateBidButtons() {
        Log.d(TAG, "Create bids buttons");
        RadioGroup bidButtonsLayout = findViewById(R.id.deal_bids_layout);

        for (Bid bid : Bid.values()) {
            RadioButton button = createRadioButton(bid.getLabel(), (view, selected) -> selectBid(view, selected, bid));
            addRadioToGroup(bidButtonsLayout, button);
        }
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

    private RadioButton createRadioButton(String text, CompoundButton.OnCheckedChangeListener listener) {
        RadioButton button = createRadioButton(listener);
        button.setText(text);
        return button;
    }
    private RadioButton createRadioButton(int textResId, CompoundButton.OnCheckedChangeListener listener) {
        RadioButton button = createRadioButton(listener);
        button.setText(textResId);
        return button;
    }
    private RadioButton createRadioButton(CompoundButton.OnCheckedChangeListener listener) {
        RadioButton button = new RadioButton(this);
        button.setOnCheckedChangeListener(listener);
        button.setBackgroundResource(R.drawable.my_radio_button_background);
        button.setButtonDrawable(null);
        return button;
    }

    private void addRadioToGroup(RadioGroup group, RadioButton button) {
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1);
        group.addView(button, layoutParams);
    }
}
