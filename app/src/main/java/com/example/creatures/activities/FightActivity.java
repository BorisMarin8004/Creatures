package com.example.creatures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Random;

public class FightActivity extends BaseActivity {

    private TextView enemyName;
    private ListView enemyPlayerCreatureList;
    private ListView activePlayerCreatureList;

    private Button buttonToHome;

    private Player activePlayer;
    private Player enemyPlayer;
    private PlayersDAO playersDAO;

    private Creature selectedEnemyPlayerCreature;
    private Creature selectedActivePlayerCreature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        enemyName = findViewById(R.id.enemyName);
        enemyPlayerCreatureList = findViewById(R.id.enemyPlayerCreatureList);
        activePlayerCreatureList = findViewById(R.id.activePlayerCreatureList);

        buttonToHome = findViewById(R.id.buttonToHome);

        playersDAO = getDAO();
        activePlayer = playersDAO.getActivePlayer();
        setEnemyPlayer();

        enemyName.setText(enemyPlayer.getUsername());
        refreshDisplay();

        activePlayerCreatureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (selectedActivePlayerCreature == null){
                    ArrayList<Creature> creaturesList = new ArrayList<>(activePlayer.getCreatures());
                    selectedActivePlayerCreature = creaturesList.get(position);
                } else if (selectedEnemyPlayerCreature == null && selectedActivePlayerCreature.getAttackType().equals(2)){
                    ArrayList<Creature> creaturesList = new ArrayList<>(activePlayer.getCreatures());
                    selectedEnemyPlayerCreature = creaturesList.get(position);
                    Toast.makeText(getApplicationContext(), "Healing the ally!", Toast.LENGTH_SHORT).show();
                    selectedActivePlayerCreature.attack(selectedEnemyPlayerCreature);
                    updatePlayers();
                    refreshDisplay();
                    resetSelectedPlayerCreatures();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot attack allies!", Toast.LENGTH_SHORT).show();
                    resetSelectedPlayerCreatures();
                }
            }
        });

        enemyPlayerCreatureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (selectedActivePlayerCreature != null) {
                    if (selectedActivePlayerCreature.getAttackType().equals(2)) {
                        Toast.makeText(getApplicationContext(), "Healing the enemy!", Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<Creature> creaturesList = new ArrayList<>(enemyPlayer.getCreatures());
                    selectedEnemyPlayerCreature = creaturesList.get(position);
                    selectedActivePlayerCreature.attack(selectedEnemyPlayerCreature);
                    selectedEnemyPlayerCreature.attack(selectedActivePlayerCreature);
                    activePlayer.removeDeadCreatures();
                    enemyPlayer.removeDeadCreatures();
                    if (enemyPlayer.getCreatures().size() == 0){
                        Toast.makeText(getApplicationContext(), "Congrats, you won! Gain 10 CP.", Toast.LENGTH_SHORT).show();
                        enemyPlayer.setCp(enemyPlayer.getCp() + 5);
                        activePlayer.setCp(activePlayer.getCp() + 10);
                    } else if (activePlayer.getCreatures().size() == 0){
                        Toast.makeText(getApplicationContext(), "Oh you lost! Gain 5 CP.", Toast.LENGTH_SHORT).show();
                        activePlayer.setCp(activePlayer.getCp() + 5);
                        enemyPlayer.setCp(enemyPlayer.getCp() + 10);
                    }
                    updatePlayers();
                    refreshDisplay();
                    resetSelectedPlayerCreatures();
                }
            }
        });

        buttonToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enemyPlayer.getCreatures().size() == 0 || activePlayer.getCreatures().size() == 0){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot retreat now!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updatePlayers(){
        playersDAO.update(activePlayer);
        playersDAO.update(enemyPlayer);
    }

    private void resetSelectedPlayerCreatures() {
        selectedActivePlayerCreature = null;
        selectedEnemyPlayerCreature = null;
    }

    private void setEnemyPlayer() {
        ArrayList<Player> listOfAllPlayers = new ArrayList<>(playersDAO.getPlayersToFight());
        while (enemyPlayer == null || enemyPlayer.equals(activePlayer)) {
            Random rand = new Random();
            enemyPlayer = listOfAllPlayers.get(rand.nextInt(listOfAllPlayers.size()));
        }
    }

    private void refreshDisplay() {

        ArrayList<String> creaturesStringList = new ArrayList<>();
        for (Creature creature : activePlayer.getCreatures()) {
            creaturesStringList.add(creature.toString());
        }
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), creaturesStringList, false, true, null);
        activePlayerCreatureList.setAdapter(adapter);

        creaturesStringList = new ArrayList<>();
        for (Creature creature : enemyPlayer.getCreatures()) {
            creaturesStringList.add(creature.toString());
        }
        adapter = new CustomAdapter(getApplicationContext(), creaturesStringList, false, true, null);
        enemyPlayerCreatureList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Cannot retreat now!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean isAdmin() {
        return false;
    }
}