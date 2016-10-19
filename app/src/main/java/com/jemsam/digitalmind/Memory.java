package com.jemsam.digitalmind;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by jeremy.toussaint on 12/10/16.
 */

public class Memory extends SugarRecord {

    private String title;
    private String description;
    private Date date;
    private Boolean isFavorite;

    public Memory() {
    }

    public Memory(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.isFavorite = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void saveMemory(){
        this.save();
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public static List<Memory> sortByDate(Boolean pIsDesc){

        Memory.executeQuery("VACUUM");
        List<Memory> lSortedMemories;

        if(pIsDesc)
        {
            lSortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Date DESC", null);
        }
        else
        {
            lSortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Date", null);
        }
        return lSortedMemories;
    }

    public static List<Memory> sortByTitle(){

        Memory.executeQuery("VACUUM");
        List<Memory> lSortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Title", null);

        return lSortedMemories;
    }

    public static List<Memory> searchMemory(String keyWord){

        Memory.executeQuery("VACUUM");  //SELECT * FROM MEMORY WHERE Title LIKE "%'.?.'%\
        List<Memory> lSortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE Title LIKE '%" + keyWord + "%'" , null);

        return lSortedMemories;
    }

    public static List<Memory> getAllMemories(){
        return Memory.listAll(Memory.class);
    }


}
