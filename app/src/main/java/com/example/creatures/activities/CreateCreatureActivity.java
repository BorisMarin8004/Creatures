package com.example.creatures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creatures.R;
import com.example.creatures.db.PlayersDAO;
import com.example.creatures.other.Creature;
import com.example.creatures.other.Player;

import java.util.ArrayList;


public class CreateCreatureActivity extends BaseActivity {

    private TextView cpDisplay;

    private EditText creatureName;
    private EditText creatureAtk;
    private EditText creatureHp;
    private EditText creatureDef;
    private EditText creatureAgi;

    private Button buttonRange;
    private Button buttonMelee;
    private Button buttonHeal;

    private Button buttonCreateCreature;

    private Player activePlayer;
    private PlayersDAO playersDAO;

    private int playerCp;
    private int pointsSpentHp = 0;
    private int pointsSpentAtk = 0;
    private int pointsSpentDef = 0;
    private int pointsSpentAgi = 0;

    private Creature creature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_creature);

        cpDisplay = findViewById(R.id.cpDisplay);

        creatureName = findViewById(R.id.creatureName);
        creatureAtk = findViewById(R.id.creatureAtk);
        creatureHp = findViewById(R.id.creatureHp);
        creatureDef = findViewById(R.id.creatureDef);
        creatureAgi = findViewById(R.id.creatureAgi);

        buttonRange = findViewById(R.id.buttonRange);
        buttonMelee = findViewById(R.id.buttonMelee);
        buttonHeal = findViewById(R.id.buttonHeal);

        buttonCreateCreature = findViewById(R.id.buttonCreateCreature);

        creature = new Creature();

        playersDAO = getDAO();
        activePlayer = playersDAO.getActivePlayer();

        playerCp = activePlayer.getCp();
        displayCp();

        creatureHp.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    pointsSpentHp = Integer.parseInt(creatureHp.getText().toString());
                }catch (NumberFormatException e){
                    pointsSpentHp = 0;
                }
                displayCp();
            }
        });

        creatureAtk.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    pointsSpentAtk = Integer.parseInt(creatureAtk.getText().toString());
                }catch (NumberFormatException e){
                    pointsSpentAtk = 0;
                }
                displayCp();
            }
        });

        creatureDef.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    pointsSpentDef = Integer.parseInt(creatureDef.getText().toString());
                }catch (NumberFormatException e){
                    pointsSpentDef = 0;
                }
                displayCp();
            }
        });

        creatureAgi.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    pointsSpentAgi = Integer.parseInt(creatureAgi.getText().toString());
                }catch (NumberFormatException e){
                    pointsSpentAgi = 0;
                }
                displayCp();
            }
        });

        buttonMelee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creature.setAttackType(0);
            }
        });

        buttonRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creature.setAttackType(1);
            }
        });

        buttonHeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creature.setAttackType(2);
            }
        });

        buttonCreateCreature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasEnoughCp()){
                    Toast.makeText(getApplicationContext(), "Sorry but you do not have enough creation points", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (creature.getAttackType() == -1){
                    Toast.makeText(getApplicationContext(), "Please select the attack type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pointsSpentHp == 0){
                    Toast.makeText(getApplicationContext(), "Creature has to have more then 0 hp", Toast.LENGTH_SHORT).show();
                    return;
                }
                creature.setName(creatureName.getText().toString());
                creature.setAttack((double) pointsSpentAtk);
                creature.setMaxHp((double) pointsSpentHp);
                creature.setHp((double) pointsSpentHp);
                creature.setDefense((double) pointsSpentDef);
                creature.setAgility((double) pointsSpentAgi);
                creature.setId();
                System.out.println(creature);
                activePlayer.addCreature(creature);
                updateCp();
                playersDAO.update(activePlayer);
                creature = new Creature();
                resetFields();
                if (activePlayer.getCreatures().size() == 3){
                    activePlayer.setReadyToFight(true);
                    playersDAO.update(activePlayer);
                    if (activePlayer.getCp() > 0){
                        Intent intent = new Intent(getApplicationContext(), ManageCreaturesActivity.class);
                        finish();
                        startActivity(intent);
                    }else{
                        ArrayList<Player> listOfPlayersToFight = new ArrayList<>(playersDAO.getPlayersToFight());
                        if (listOfPlayersToFight.size() > 1) {
                            Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "We need more players to fight or players need more creatures", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Please create " + (3-activePlayer.getCreatures().size()) + " more creature(s)", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetFields() {
        creatureName.setText("");
        creatureAtk.setText("");
        creatureHp.setText("");
        creatureDef.setText("");
        creatureAgi.setText("");
    }

    private void updateCp() {
        activePlayer.setCp(activePlayer.getCp()-getTotalPointsSpent());
        playerCp = activePlayer.getCp();
    }

    private boolean hasEnoughCp() {
        return activePlayer.getCp() >= (pointsSpentAgi + pointsSpentDef + pointsSpentHp + pointsSpentAtk);
    }

    private void displayCp() {
        String text = "CP: " + (playerCp - getTotalPointsSpent());
        cpDisplay.setText(text);
    }

    private int getTotalPointsSpent(){
        return (pointsSpentAgi + pointsSpentDef + pointsSpentHp + pointsSpentAtk);
    }

    @Override
    protected boolean isAdmin() {
        return false;
    }

}
