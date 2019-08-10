package fr.cbug.wditarot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.cbug.wditarot.model.Player;

public class ChoosePlayersActivity extends AppCompatActivity {
    private static final int DEFAULT_PLAYERS_COUNT = 3;

    private int playersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_players);

        playersCount = 0;
        generateDefaultEdits();

        Button addBtn = findViewById(R.id.players_add_player);
        addBtn.setOnClickListener(v -> addPlayerEdit());

        Button validateBtn = findViewById(R.id.players_validate);
        validateBtn.setOnClickListener(v -> validatePlayers());
    }

    private void validatePlayers() {
        List<Player> players = new ArrayList<>();

        ViewGroup group = findViewById(R.id.players_list_layout);
        for (int i = 0; i < group.getChildCount(); i++) {
            TextView text = (TextView)group.getChildAt(i);
            String playerName = text.getText().toString();
            if (playerName.isEmpty()) {
                Toast.makeText(this, R.string.players_empty_name, Toast.LENGTH_SHORT).show();
                return ;
            }
            Player player = new Player(playerName);
            if (players.contains(player)) {
                Toast.makeText(this, R.string.players_dupplicate_name, Toast.LENGTH_SHORT).show();
                return ;
            }
            players.add(player);
        }

        Intent intent = new Intent().setClass(this, ShowScoresActivity.class);
        intent.putExtra(ShowScoresActivity.PLAYERS_KEY, (Serializable) players);
        startActivity(intent);
    }

    private void generateDefaultEdits() {
        for (int i = 0; i < DEFAULT_PLAYERS_COUNT; i++) {
            addPlayerEdit();
        }
    }

    private void addPlayerEdit() {
        ViewGroup group = findViewById(R.id.players_list_layout);

        EditText edit = new EditText(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        edit.setLayoutParams(layoutParams);
        playersCount++;
        edit.setHint(getResources().getString(R.string.player) + " " + playersCount);

        group.addView(edit);
    }
}
