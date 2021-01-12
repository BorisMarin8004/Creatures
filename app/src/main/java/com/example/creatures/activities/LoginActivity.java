package com.example.creatures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.creatures.db.PlayersDAO;
import com.example.creatures.other.Player;
import com.example.creatures.R;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends BaseActivity {

    private final static String ADMIN_USERNAME = "admin";
    private final static String ADMIN_PASSWORD = "admin";

    private EditText usernameField;
    private EditText passwordField;

    private String username;
    private String password;

    private Button loginButton;
    private Button createAccButton;

    private Player activePlayer;
    private PlayersDAO playersDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);

        loginButton = findViewById(R.id.loginButton);
        createAccButton = findViewById(R.id.createAccButton);

        playersDAO = getDAO();

        checkForUser();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disableActivePlayer();
                activePlayer = null;

                getValuesFromDisplay();

                if (validatePlayer()){

                    setActivePlayerFromDisplay();
                    invalidateOptionsMenu();

                    Toast.makeText(getApplicationContext(),"Welcome: " + username, Toast.LENGTH_SHORT).show();
                    resetFields();
                    if (isAdmin()){
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                    }else{
                        setActivePlayerFromDatabase();
                        setActivePlayer();
                        if (activePlayer.getCreatures().size() < 3){
                            if (activePlayer.getCp() != 0){
                                Intent intent = new Intent(getApplicationContext(), CreateCreatureActivity.class);
                                startActivity(intent);
                            } else {
                                if (activePlayer.getCreatures().size() > 0){
                                    Toast.makeText(getApplicationContext(), "You do not have creation points! Kill your creatures!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "You lost the game! Create new account and try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else if (activePlayer.getCp() == 0){
                            ArrayList<Player> listOfAllPlayers = new ArrayList<>(playersDAO.getPlayers());
                            if (listOfAllPlayers.size() > 1) {
                                Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "We need more players to fight", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Intent intent = new Intent(getApplicationContext(), ManageCreaturesActivity.class);
                            startActivity(intent);
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Wrong username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(activePlayer);
                disableActivePlayer();
                getValuesFromDisplay();
                if (!validateUsername()){
                    setActivePlayerFromDisplay();
                    if (isAdmin()){
                        Toast.makeText(getApplicationContext(),"Cannot create player with username: " + activePlayer.getUsername(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(),"New player: " + activePlayer.getUsername(), Toast.LENGTH_SHORT).show();
                    System.out.println(activePlayer);
                    playersDAO.insert(activePlayer);
                } else {
                    Toast.makeText(getApplicationContext(),"Player with username: " + username + " already exists.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    private void setActivePlayerFromDatabase() {
        activePlayer = playersDAO.getPlayerByUsername(username);
    }

    protected boolean isAdmin() {
        if (username == null || password == null){
            return false;
        }
        return username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
    }

    private void getValuesFromDisplay(){
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
    }

    private boolean validatePlayer(){
        if (validateUsername()) {
            Player player = playersDAO.getPlayerByUsername(username);
            return player.getPassword().equals(password);
        } else {
            return isAdmin();
        }
    }

    private void setActivePlayerFromDisplay(){
        if (activePlayer == null){
            activePlayer = new Player();
        }
        activePlayer.setUsername(username);
        activePlayer.setPassword(password);
    }

    private boolean validateUsername(){
        return playersDAO.getPlayerByUsername(username) != null;
    }

    private void checkForUser() {

        if(playersDAO.getActivePlayer() != null){
            activePlayer = playersDAO.getActivePlayer();
            invalidateOptionsMenu();
            return;
        }

        List<Player> users = playersDAO.getPlayers();
        if(users.size() <= 0){
            Toast.makeText(this, "Please create an account", Toast.LENGTH_SHORT).show();
        }

    }

    private void setActivePlayer(){
        activePlayer.setActive(true);
        playersDAO.update(activePlayer);
    }










}