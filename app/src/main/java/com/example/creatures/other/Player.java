package com.example.creatures.other;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.creatures.db.PlayersDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = PlayersDatabase.PLAYERS_TABLE)
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private static final Integer CREATURE_LIMIT = 3;
//    private static final List<String> MANAGE_OPTIONS = Collections.unmodifiableList(
//            new ArrayList<String>() {{
//                add("Feed");
//                add("Pet");
//                add("Train");
//            }});


    private String username;
    private String password;
    private List<Creature> creatures;
    private Integer cp;
    private boolean readyToFight;
    private boolean active;


    public Player(){
        this("Not logged in", "NoPassword");
    }

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.creatures = new ArrayList<>();
        this.cp = 20;
        this.readyToFight = false;
        this.active = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void setCreatures(List<Creature> creatures) {
        this.creatures = creatures;
    }

    public Integer getCp() {
        return cp;
    }

    public void setCp(Integer cp) {
        this.cp = cp;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setReadyToFight(boolean readyToFight){
        this.readyToFight = readyToFight;
    }

    public boolean isReadyToFight() {
        return readyToFight;
    }

    public Integer getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean addCreature(Creature creature){
        if (creatures.size() < CREATURE_LIMIT){
            if (!creatures.contains(creature)){
                creatures.add((Creature) creature.clone());
                return true;
            }else{
                System.out.println("Error:: Creature is a part of this player roster");
                return false;
            }
        }else{
            System.out.println("Error:: CREATURE LIMIT of " + CREATURE_LIMIT + " has been reached cannot add creature.");
            return false;
        }
    }

    public void removeDeadCreatures(){
        Creature creatureToRemove = null;
        for (Creature creature : creatures){
            if (creature.getHp() < 1){
                creatureToRemove = creature;
            }
        }
        if (creatureToRemove != null){
            creatures.remove(creatureToRemove);
            if (creatures.size() < 3){
                readyToFight = false;
            }
        }
    }

//    public boolean createCreature(String name, Integer attack, Integer attackType, Integer hp, Integer defense, Integer agility){
//        if (creatures.size() <= CREATURE_LIMIT){
//            if (attackType == 0 || attackType == 1 || attackType == 2){
//                if (cp - attack > 0){
//                    cp -= attack;
//                    if (cp - hp > 0){
//                        cp -= hp;
//                        if (cp - defense > 0){
//                            cp -= defense;
//                            if (cp - agility > 0){
//                                cp -= agility;
//                                return addCreature(new Creature(name, Double.valueOf(attack), attackType, Double.valueOf(hp), Double.valueOf(defense), Double.valueOf(agility)));
//                            }else{
//                                System.out.println("Error:: Insufficient creation points (agility)");
//                                return false;
//                            }
//                        }else{
//                            System.out.println("Error:: Insufficient creation points (defense)");
//                            return false;
//                        }
//                    }else{
//                        System.out.println("Error:: Insufficient creation points (hp)");
//                        return false;
//                    }
//                }else{
//                    System.out.println("Error:: Insufficient creation points (attack)");
//                    return false;
//                }
//            }else{
//                System.out.println("Error:: Wrong attack type.");
//                return false;
//            }
//        }else{
//            System.out.println("Error:: CREATURE LIMIT of " + CREATURE_LIMIT + " has been reached cannot create creature.");
//            return false;
//        }
//    }

//    public boolean manageCreatures(Integer creatureIndex, String manageOption){
//        if (MANAGE_OPTIONS.contains(manageOption)){
//            switch (manageOption) {
//                case "Feed":
//                    creatures.get(creatureIndex).feed();
//                    return true;
//                case "Pet":
//                    creatures.get(creatureIndex).pet();
//                    return true;
//                case "Train":
//                    creatures.get(creatureIndex).train();
//                    return true;
//            }
//            System.out.println("Error:: MANAGE_OPTIONS has wrong options");
//        }else{
//            System.out.println("Error:: No such manage option.");
//        }
//        return false;
//    }

//    public boolean fight(HashMap<Creature, Creature> attackPair){
//        if (attackPair.size() == 3){
//            for (Creature attacker: attackPair.keySet()) {
//                attacker.attack(attackPair.get(attacker));
//            }
//            return true;
//        }else{
//            System.out.println("Error:: wrong size of attackPair has to be 3 pairs.");
//            return false;
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getUsername().equals(player.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @NonNull
    @Override
    public String toString() {
        return username + "; " + password + ":\n" +
                "Creatures: " + creatures +
                "CP: " + cp + "\n";
    }
}
