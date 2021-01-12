package com.example.creatures.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.Room;

import com.example.creatures.R;
import com.example.creatures.db.PlayersDAO;
import com.example.creatures.db.PlayersDatabase;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    private Player activePlayer;
    private PlayersDAO playersDAO;

    private TextView elementToUpdate;
    private boolean deletableItems;
    private boolean clickableItems;

    public CustomAdapter(Context context, ArrayList<String> list, boolean deletableItems, boolean clickableItems, TextView elementToUpdate){
        this.context = context;
        this.list = list;
        playersDAO = Room.databaseBuilder(context, PlayersDatabase.class, PlayersDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getPlayersDAO();
        this.elementToUpdate = elementToUpdate;
        this.deletableItems = deletableItems;
        this.clickableItems = clickableItems;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String creatureInfo = list.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_element, parent, false);
        }
        TextView creatureInfoField = convertView.findViewById(R.id.elementText);
        creatureInfoField.setText(creatureInfo);

        if (deletableItems) {
            ImageView deleteSign = convertView.findViewById(R.id.deleteSign);
            deleteSign.setImageResource(R.drawable.plus_sign);
            deleteSign.setTag(position);
            if (elementToUpdate != null) {
//                set back color
                deleteSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();
                        activePlayer = playersDAO.getActivePlayer();

                        ArrayList<Creature> creaturesList = new ArrayList<>(activePlayer.getCreatures());
                        creaturesList.remove(position);
                        list.remove(position);
                        activePlayer.setCreatures(creaturesList);
                        activePlayer.setCp(activePlayer.getCp() + 5);
                        if (activePlayer.getCreatures().size() < 3) {
                            activePlayer.setReadyToFight(false);
                        }
                        playersDAO.update(activePlayer);
                        if (elementToUpdate != null) {
                            String points = "Cp: " + activePlayer.getCp();
                            elementToUpdate.setText(points);
                        }
                        notifyDataSetChanged();
                    }
                });
            } else {
                deleteSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();

                        ArrayList<Player> playersList = new ArrayList<>(playersDAO.getPlayers());
                        Player playerToRemove = playersList.get(position);
                        list.remove(position);
                        playersDAO.delete(playerToRemove);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        if (!clickableItems){
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
        }

        return convertView;

    }
}
