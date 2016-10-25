package com.jemsam.digitalmind.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by jeremy.toussaint on 21/10/16.
 */

public class TagMemory extends SugarRecord {

    private Long tagId;
    private Long memoryId;

    public TagMemory() {
    }

    public TagMemory(Long tagId, Long memoryId) {
        this.memoryId = memoryId;
        this.tagId = tagId;
    }

    public Long getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(Long memoryId) {
        this.memoryId = memoryId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public static List<TagMemory> getAllTagMemories(){
        return TagMemory.listAll(TagMemory.class);
    }
}
