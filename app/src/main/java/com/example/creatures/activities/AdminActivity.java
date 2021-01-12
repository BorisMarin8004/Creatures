package com.example.creatures.activities;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.creatures.other.Creature;
import com.example.creatures.other.CustomAdapter;
import com.example.creatures.other.Player;
import com.example.creatures.R;
import com.example.creatures.db.PlayersDAO;
import com.example.creatures.db.PlayersDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private ListView databaseView;

    private EditText valueToSearch;

    private Button buttonSearch;
    private Button deleteAllButton;
    private Button backButton;

    private PlayersDAO playersDAO;
    private List<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        
        databaseView = findViewById(R.id.databaseView);

        valueToSearch = findViewById(R.id.valueToSearch);

        buttonSearch = findViewById(R.id.searchElement);
        deleteAllButton = findViewById(R.id.deleteAll);
        backButton = findViewById(R.id.back);

        playersDAO = Room.databaseBuilder(this, PlayersDatabase.class, PlayersDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getPlayersDAO();

        refreshDisplay(null);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = valueToSearch.getText().toString();
                ArrayList<Player> foundPlayers = new ArrayList<>();
                players = playersDAO.getPlayers();
                for (Player player : players) {
                    if (player.getUsername().contains(username)){
                        foundPlayers.add(player);
                    }
                }
                if (foundPlayers.size() > 0){
                    refreshDisplay(foundPlayers);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no player with username: " + username, Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                players = playersDAO.getPlayers();
                for (Player player : players) {
                        playersDAO.delete(player);
                }
                refreshDisplay(null);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void refreshDisplay(ArrayList<Player> listToDisplay){
        if (listToDisplay == null){
            players = playersDAO.getPlayers();
        } else {
            players = listToDisplay;
        }

        ArrayList<String> playersStringList = new ArrayList<>();
        for (Player player : players) {
            playersStringList.add(player.toString());
        }
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), playersStringList, true, false, null);
        databaseView.setAdapter(adapter);
    }
}
