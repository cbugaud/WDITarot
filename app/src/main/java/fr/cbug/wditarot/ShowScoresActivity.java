package fr.cbug.wditarot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import fr.cbug.wditarot.model.Deal;
import fr.cbug.wditarot.model.Game;
import fr.cbug.wditarot.model.Player;

public class ShowScoresActivity extends AppCompatActivity {
    public static final String PLAYERS_KEY = "players";
    public static final int ACT_NEWDEAL = 1;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);

        game = new Game((List<Player>) getIntent().getSerializableExtra(PLAYERS_KEY));

        populateScoreGrid();

        FloatingActionButton fab = findViewById(R.id.new_deal_btn);
        fab.setOnClickListener(this::newDeal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateScoreGrid();
    }

    private void populateScoreGrid() {
        GridLayout grid = findViewById(R.id.score_grid);

        grid.setColumnCount(game.getPlayers().size());
        grid.removeAllViews();

        // Liste des joueurs
        for (Player player : game.getPlayers()) {
            TextView view = createTextCell();
            view.setText(player.getName());
            grid.addView(view);
        }

        // Scores totaux
        Map<Player, Integer> totalScores = game.totalScores();
        addScoreCells(grid, totalScores);

        // Scores de chaque partie
        for (Deal deal : game.getDeals()) {
            addScoreCells(grid, deal.getScores());
        }
    }

    private void addScoreCells(GridLayout grid, Map<Player, Integer> scores) {
        for (Player player : game.getPlayers()) {
            TextView view = createTextCell();
            Integer score = scores.get(player);

            if (score != null) {
                view.setText(String.valueOf(score));
            } else
                view.setText("-");
            grid.addView(view);
        }
    }

    @NonNull
    private TextView createTextCell() {
        TextView result = new TextView(this);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED, GridLayout.FILL,1),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1));
        result.setLayoutParams(layoutParams);
        return result;
    }

    private void newDeal(View view) {
        Intent intent = new Intent().setClass(view.getContext(), NewDealActivity.class);
        intent.putExtra(PLAYERS_KEY, (Serializable) game.getPlayers());
        startActivityForResult(intent, ACT_NEWDEAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACT_NEWDEAL && resultCode == RESULT_OK) {
            Deal newDeal = (Deal) data.getSerializableExtra(NewDealActivity.DEAL_KEY);
            game.addDeal(0, newDeal);
        }
    }
}
