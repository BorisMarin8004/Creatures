package com.example.creatures.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.creatures.R;
import com.example.creatures.db.PlayersDAO;
import com.example.creatures.db.PlayersDatabase;
import com.example.creatures.other.Player;

public abstract class BaseActivity extends AppCompatActivity {

    private Player activePlayer;
    private PlayersDAO playersDAO;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        activePlayer = playersDAO.getActivePlayer();
        MenuItem item = menu.findItem(R.id.account);
        if (activePlayer == null){
            Player tempPlayer = new Player();
            item.setTitle(tempPlayer.getUsername());
        } else {
            item.setTitle(activePlayer.getUsername());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            activePlayer = playersDAO.getActivePlayer();
            disableActivePlayer();
            if (!this.getClass().getSimpleName().equals("LoginActivity")){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(intent);
            }
            return true;
        } else if (item.getItemId() == R.id.account){
            if (activePlayer != null){
                if (isAdmin()){
                    Toast.makeText(getApplicationContext(),"Admin is homeless!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    if (!this.getClass().getSimpleName().equals("LoginActivity")){
                        finish();
                    }
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(),"You are not logged in", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract boolean isAdmin();


    protected PlayersDAO getDAO(){
        if (playersDAO == null){
            playersDAO = Room.databaseBuilder(this, PlayersDatabase.class, PlayersDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getPlayersDAO();
        }
        return playersDAO;
    }

    protected void disableActivePlayer(){
        if (activePlayer != null){
            activePlayer.setActive(false);
            playersDAO.update(activePlayer);
            activePlayer = null;
            invalidateOptionsMenu();
        }
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
