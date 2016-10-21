package com.jemsam.digitalmind;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by jeremy.toussaint on 21/10/16.
 */

public class Tag extends SugarRecord {

    private String tagContent;

    public Tag() {
    }

    public Tag(String tagContent) {
        this.tagContent = tagContent;
    }

    public String getTagContent() {
        return tagContent;
    }

    public static List<Tag> getAllTags(){
        return Tag.listAll(Tag.class);
    }

    public static Tag getTag(String tagContent){
        List<Tag> tags = getAllTags();

        if (tags.size() > 0){
            for (Tag tag: tags){
                if (tag.getTagContent().equals(tagContent)){
                    return tag;
                }
            }
        }

        Tag tag = new Tag(tagContent);
        tag.save();

        return tag;
    }

    public static Tag getTag(Long tagId){
        List<Tag> tags = Tag.find(Tag.class, "id = ?", String.valueOf(tagId));

        if (tags.size() > 0){
            return tags.get(0);
        }

        return null;
    }

    public static Tag getTagByWord(String word){
        List<Tag> tags = Tag.find(Tag.class, "tag_content = ?", String.valueOf(word));

        if (tags.size() > 0){
            return tags.get(0);
        }
        return null;
    }

    public void linkMemory(Memory memory){
        List<TagMemory> jointure = TagMemory.getAllTagMemories();
        for (TagMemory tagMemory : jointure){
            if (tagMemory.getMemoryId() == memory.getId() && tagMemory.getTagId() == this.getId()){
                return;
            }
        }
        //Memory wasnt linked to tags previously
        TagMemory tagMemory = new TagMemory(this.getId(), memory.getId());
        tagMemory.save();

    }




}
