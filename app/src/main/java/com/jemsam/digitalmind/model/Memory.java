package com.jemsam.digitalmind.model;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by jeremy.toussaint on 12/10/16.
 */

public class Memory extends SugarRecord {

    private Long userId;
    private String title;
    private String description;
    private Date date;
    private Boolean isFavorite = false;
    private String imagePath;
    private float rating = 2.5f;
    private Double latitude;
    private Double longitude;

    public Memory() {
    }

    public Memory(Date date, User user) {
        this.userId = user.getId();
        this.date = date;
    }

    public Memory(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.isFavorite = false;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public void setCoordinates(LatLng coordinates) {
        this.latitude = coordinates.latitude;
        this.longitude = coordinates.longitude;
    }

    public LatLng getCoordinates() {
        if (latitude == null || longitude == null){
            return null;
        }
        return new LatLng(latitude, longitude);
    }

    public static List<Memory> sortByDate(Boolean isDesc, User user){

        Memory.executeQuery("VACUUM");
        List<Memory> sortedMemories;

        if(isDesc)
        {
            sortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE user_id = " + user.getId() + " ORDER BY Date DESC");
        }
        else
        {
            sortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE user_id = " + user.getId() + " ORDER BY Date");
        }
        return sortedMemories;
    }

    public static List<Memory> sortByTitle(Boolean isDesc, User user){

        Memory.executeQuery("VACUUM");
        List<Memory> sortedMemories;

        if(isDesc)
        {
            sortedMemories = Memory.findWithQuery(Memory.class,  "SELECT * FROM MEMORY WHERE user_id = " + user.getId() + " ORDER BY Title DESC");
        }
        else
        {
            sortedMemories = Memory.findWithQuery(Memory.class,  "SELECT * FROM MEMORY WHERE user_id = " + user.getId() + " ORDER BY Title");
        }

        return sortedMemories;
    }

    public static List<Memory> searchMemory(String keyWord, User user){

        Memory.executeQuery("VACUUM");
        List<Memory> lSortedMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE Title LIKE '%" + keyWord + "%' AND user_id = " + user.getId());

        return lSortedMemories;
    }

    public static List<Memory> getAllMemories(User user){
        /*return Memory.listAll(Memory.class);*/
        return  Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE user_id = " + user.getId());
    }

    public static List<Memory> getAllFavorites(User user)
    {
        Memory.executeQuery("VACUUM");
        List<Memory> lFavoriteMemories = Memory.findWithQuery(Memory.class, "SELECT * FROM MEMORY WHERE is_favorite = '1' AND user_id = " + user.getId());

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
            memory.setCoordinates(memoryToUpdate.getCoordinates());
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
