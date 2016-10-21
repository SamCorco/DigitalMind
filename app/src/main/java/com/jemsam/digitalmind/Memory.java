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
    private Boolean isFavorite = false;
    private String imagePath;
    private float rating = 2.5f;

    public Memory() {
    }

    public Memory(Date date) {
        this.date = date;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public static List<Memory> sortByDate(Boolean isDesc){

        Memory.executeQuery("VACUUM");
        List<Memory> sortedMemories;

        if(isDesc)
        {
            sortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Date DESC", null);
        }
        else
        {
            sortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Date", null);
        }
        return sortedMemories;
    }

    public static List<Memory> sortByTitle(Boolean isDesc){

        Memory.executeQuery("VACUUM");
        List<Memory> sortedMemories;

        if(isDesc)
        {
            sortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Title DESC", null);
        }
        else
        {
            sortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY ORDER BY Title", null);
        }

        return sortedMemories;
    }

    public static List<Memory> searchMemory(String keyWord){

        Memory.executeQuery("VACUUM");
        List<Memory> lSortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE Title LIKE '%" + keyWord + "%'" , null);

        return lSortedMemories;
    }

    public static List<Memory> getAllMemories(){
        return Memory.listAll(Memory.class);
    }

    public static List<Memory> getAllFavorites()
    {
        Memory.executeQuery("VACUUM");
        List<Memory> lFavoriteMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE is_favorite = '1'", null);

        return lFavoriteMemories;
    }

    public static void update(Memory memoryToUpdate){
        List<Memory> memories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE Date = ?", ""+memoryToUpdate.getDate().getTime());
        if (memories.size() > 0){
            Memory memory = memories.get(0);
            memory.setTitle(memoryToUpdate.getTitle());
            memory.setDescription(memoryToUpdate.getDescription());
            memory.setIsFavorite(memoryToUpdate.getIsFavorite());
            memory.setImagePath(memoryToUpdate.getImagePath());
            memory.setRating(memoryToUpdate.getRating());
            memory.save();
        }

    }


    public static Memory getMemory(Long memoryId) {
        List<Memory> memories = Memory.find(Memory.class, "id = ?", String.valueOf(memoryId));

        if (memories.size() > 0){
            return memories.get(0);
        }

        return null;
    }
}
