package fr.cbug.wditarot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.common.collect.Lists;

import java.util.List;

import fr.cbug.wditarot.model.Player;

public class NewDealActivity extends AppCompatActivity {
    private List<Player> players = Lists.newArrayList(new Player("Erwan"), new Player("Céline"), new Player("Julien"), new Player("Simon"), new Player("Aude"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);

//        generateTakerButtons();
//        generateBidButtons();
//        generateCalleeButtons();
//        generateBonusesButtons();
    }
}
