package com.jemsam.digitalmind.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by jeremy.toussaint on 21/10/16.
 */

public class Tag extends SugarRecord {
    private Long userId;
    private String tagContent;

    public Tag() {
    }

    public Tag(String tagContent, Long userId) {
        this.tagContent = tagContent;
        this.userId = userId;
    }

    public String getTagContent() {
        return tagContent;
    }

    public static List<Tag> getAllTags(User user){
        /*return Tag.listAll(Tag.class);*/
        return  Tag.findWithQuery(Tag.class, "SELECT * FROM TAG WHERE user_id = " + user.getId());
    }

    public static Tag getTag(String tagContent, User user){
        tagContent = tagContent.toLowerCase();

        List<Tag> tags = getAllTags(user);

        if (tags.size() > 0){
            for (Tag tag: tags){
                if (tag.getTagContent().equals(tagContent)){
                    return tag;
                }
            }
        }

        Tag tag = new Tag(tagContent, user.getId());
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

    public static Tag getTagByWord(String word, User user){
        word = word.toLowerCase();

        List<Tag> tags = Tag.findWithQuery(Tag.class, "SELECT * FROM Tag WHERE tag_content = '" + String.valueOf(word) + "' AND user_id = " + user.getId());

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
