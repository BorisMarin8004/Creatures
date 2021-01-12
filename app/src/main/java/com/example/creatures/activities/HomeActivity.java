package com.example.creatures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creatures.R;
import com.example.creatures.db.PlayersDAO;
import com.example.creatures.other.Creature;
import com.example.creatures.other.CustomAdapter;
import com.example.creatures.other.Player;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity {

    private TextView usernameDisplay;
    private TextView cpDisplay;
    private ListView creaturesDisplay;

    private Button buttonManage;
    private Button buttonCreateCreature;
    private Button buttonFight;

    private Player activePlayer;
    private PlayersDAO playersDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameDisplay = findViewById(R.id.username);
        cpDisplay = findViewById(R.id.cpDisplay);
        creaturesDisplay = findViewById(R.id.creaturesList);

        buttonCreateCreature = findViewById(R.id.buttonCreateCreature);
        buttonManage = findViewById(R.id.buttonManage);
        buttonFight = findViewById(R.id.buttonFight);

        playersDAO = getDAO();
        activePlayer = playersDAO.getActivePlayer();

        setTextViewElements();

        buttonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateActivePlayer();
                if (activePlayer.getCp() > 0){
                    if (activePlayer.getCreatures().size() != 0){
                        Intent intent = new Intent(getApplicationContext(), ManageCreaturesActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "You cannot manage what you do not have", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "You do not have enough creation points", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCreateCreature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateActivePlayer();
                if (activePlayer.getCreatures().size() != 3){
                    if (activePlayer.getCp() != 0){
                        Intent intent = new Intent(getApplicationContext(), CreateCreatureActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "You have no Cp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You already have enough creatures", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateActivePlayer();
                if (activePlayer.getCreatures().size() == 3){
                    ArrayList <Player> listOfPlayersToFight = new ArrayList<>(playersDAO.getPlayersToFight());
                    if (listOfPlayersToFight.size() > 1) {
                        Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "We need more players to fight or players need more creatures", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You need more creatures to fight", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setTextViewElements() {

        usernameDisplay.setText(activePlayer.getUsername());
        String points = "Cp: " + activePlayer.getCp();
        cpDisplay.setText(points);

        ArrayList<String> creaturesStringList = new ArrayList<>();
        for (Creature creature : activePlayer.getCreatures()) {
            creaturesStringList.add(creature.toString());
        }
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), creaturesStringList, true, false, cpDisplay);
        creaturesDisplay.setAdapter(adapter);
    }

    private void updateActivePlayer(){
        activePlayer = playersDAO.getActivePlayer();
    }

    @Override
    protected boolean isAdmin() {
        return false;
    }

}