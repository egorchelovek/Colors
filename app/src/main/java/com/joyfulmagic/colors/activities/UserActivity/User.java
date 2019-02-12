package com.joyfulmagic.colors.activities.UserActivity;

import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

import java.util.ArrayList;

/**
 * User class for Guru class usage.
 */
public class User {

    public String name;
    public ArrayList<Skill> skills;

    public User() {
        skills = new ArrayList<Skill>();

        for (int i = 0; i < Settings.skills.length; i++) {
            skills.add(new Skill(Settings.skills[i]));
        }
    }

    public Skill getSkill(String name){
        for(int i = 0; i < skills.size(); i++){
            if(name.equals(skills.get(i).name)){
                return skills.get(i);
            }
        }
        return null;
    }

}

