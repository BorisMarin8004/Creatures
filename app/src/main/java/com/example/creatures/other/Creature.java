package com.example.creatures.other;



import com.example.creatures.activities.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Creature {

    private static Integer nextId = -1;
    private static final List<String> ATTACK_TYPES = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("Melee");
                add("Range");
                add("Heal");
            }});


    private String name;
    private Double attack;
    private Integer attackType;
    private Double hp;
    private Double maxHp;
    private Double defense;
    private Double agility;
    private Integer id;

    private static Integer getNextId() {
        nextId++;
        return nextId;
    }

    public Creature(){
        this("NoName", 0.0, -1, 0.0, 0.0, 0.0);
    }

    public Creature(String name, Double attack, Integer attackType, Double hp, Double defense, Double agility) {
        this.name = name;
        this.attack = attack;
        this.attackType = attackType;
        this.hp = hp;
        this.maxHp = hp;
        this.defense = defense;
        this.agility = agility;
        this.id = getNextId();
    }

    public Creature(String name, Double attack, Integer attackType, Double hp, Double maxHp, Double defense, Double agility, Integer id) {
        this.name = name;
        this.attack = attack;
        this.attackType = attackType;
        this.hp = hp;
        this.maxHp = maxHp;
        this.defense = defense;
        this.agility = agility;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAttack() {
        return attack;
    }

    public void setAttack(Double attack) {
        this.attack = attack;
    }

    public Integer getAttackType() {
        return attackType;
    }

    public void setAttackType(Integer attackType) {
        this.attackType = attackType;
    }

    public Double getHp() {
        return hp;
    }

    public void setHp(Double hp) {
        if (hp > maxHp){
            this.hp = maxHp;
        }else{
            this.hp = hp;
        }
    }

    public Double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Double maxHp) {
        this.maxHp = maxHp;
    }

    public Double getDefense() {
        return defense;
    }

    public void setDefense(Double defense) {
        this.defense = defense;
    }

    public Double getAgility() {
        return agility;
    }

    public void setAgility(Double agility) {
        this.agility = agility;
    }

    public Integer getId() {
        return id;
    }

    public void setId(){
        id = getNextId();
    }

    public static int getStaticId(){
        return nextId;
    }

    public void attack(Creature target){
        Double modifier;
        switch (ATTACK_TYPES.get(attackType)) {
            case "Melee":
                modifier = target.getDefense();
                break;
            case "Range":
                modifier = target.getAgility();
                break;
            case "Heal":
                modifier = -1.0;
                break;
            default:
                System.out.println("Error:: Creature " + name + " has no attack type.");
                return;
        }
        if (modifier == 0){
            modifier = 0.1;
        }
        target.setHp(target.getHp()-attack/modifier);
    }

    public void feed(){
        double newHp = hp+1;
        if (newHp > maxHp){
            newHp = maxHp;
        }
        hp = newHp;
    }

    public void pet(){
        attack *= 2;
    }

    public void train(){
        maxHp +=1;
        attack +=1;
        defense +=1;
        agility +=1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Creature creature = (Creature) o;
        return getId().equals(creature.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @NotNull
    @Override
    public String toString() {
        return name + ": " +
                "\n   HP: " + BaseActivity.round(hp, 1) + "/" + BaseActivity.round(maxHp, 1) +
                "\n   Attack: " + BaseActivity.round(attack, 1) +
                "\n   Attack type: " + ATTACK_TYPES.get(attackType) +
                "\n   Defense: " + BaseActivity.round(defense, 1) +
                "\n   Agility: " + BaseActivity.round(agility, 1);
    }

    @Override
    public Object clone() {
        try {
            return (Creature) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Creature(this.name, this.attack, this.attackType, this.hp, this.maxHp, this.defense, this.agility, this.id);
        }
    }

}
