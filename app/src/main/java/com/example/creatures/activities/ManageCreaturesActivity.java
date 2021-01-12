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


public class ManageCreaturesActivity extends BaseActivity {

    private TextView mpDisplay;
    private ListView creaturesDisplay;

    private Button buttonReset;
    private Button buttonFight;
    private Button buttonApply;

    private Creature selectedCreature;

    private Player activePlayer;
    private PlayersDAO playersDAO;

    private int playerCp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_creatures);

        mpDisplay = findViewById(R.id.mpDisplay);
        creaturesDisplay = findViewById(R.id.creaturesList);

        buttonReset = findViewById(R.id.buttonReset);
        buttonFight = findViewById(R.id.buttonFight);
        buttonApply = findViewById(R.id.buttonApply);

        playersDAO = getDAO();
        activePlayer = playersDAO.getActivePlayer();

        playerCp = activePlayer.getCp();

        refreshDisplay();

        creaturesDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArrayList<Creature> creaturesList = new ArrayList<>(activePlayer.getCreatures());
                selectedCreature = creaturesList.get(position);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activePlayer = playersDAO.getActivePlayer();
                playerCp = activePlayer.getCp();
                selectedCreature = null;
                refreshDisplay();
            }
        });

        buttonFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCp > -1) {
                    applyCpChanges();
                    ArrayList<Player> listOfAllPlayersToFight = new ArrayList<>(playersDAO.getPlayersToFight());
                    if (listOfAllPlayersToFight.size() > 1) {
                        Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "We need more players to fight or players need more creatures", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    notEnoughMp();
                }
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyCpChanges();
            }
        });
    }

    public void buttonCreatureHandleClick(View v){
        if (selectedCreature != null){
            if (playerCp > 0) {
                switch(v.getId()){
                    case R.id.buttonPet:
                        selectedCreature.pet();
                        break;
                    case R.id.buttonFeed:
                        if (selectedCreature.getMaxHp().equals(selectedCreature.getHp())){
                            Toast.makeText(getApplicationContext(), "Feeding fed creature!", Toast.LENGTH_SHORT).show();
                        }
                        selectedCreature.feed();
                        break;
                    case R.id.buttonTrain:
                        selectedCreature.train();
                        break;
                }
                playerCp -= 1;
                refreshDisplay();
            } else {
                notEnoughMp();
            }
        }else{
            creatureNotSelected();
        }
    }

    private void applyCpChanges(){
        activePlayer.setCp(playerCp);
        playersDAO.update(activePlayer);
    }

    private void notEnoughMp() {
        Toast.makeText(getApplicationContext(), "You do not have enough MP", Toast.LENGTH_SHORT).show();
    }

    private void creatureNotSelected(){
        Toast.makeText(getApplicationContext(), "Please select a creature", Toast.LENGTH_SHORT).show();
    }

    private void refreshDisplay() {

        String points = "Cp: " + playerCp;
        mpDisplay.setText(points);

        ArrayList<String> creaturesStringList = new ArrayList<>();
        for (Creature creature : activePlayer.getCreatures()) {
            creaturesStringList.add(creature.toString());
        }
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), creaturesStringList, false, true, null);
        creaturesDisplay.setAdapter(adapter);
    }

    @Override
    protected boolean isAdmin() {
        return false;
    }
}