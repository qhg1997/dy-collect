package com.qhg.sgp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tag")
public class Tag {
    @JSONField(name = "tag_id")
    @Id
    private Integer tagId;
    @JSONField(name = "tag_title")
    private String tagTitle;
}
