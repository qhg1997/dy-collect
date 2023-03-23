package com.qhg.sgp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "anchors")
public class Anchors {
    @Id
    @JSONField(name = "anchors_id")
    private Integer anchorsId;
    @JSONField(name = "anchors_img")
    private String anchorsImg;
    @JSONField(name = "anchors_name")
    private String anchorsName;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "library_count")
    private String libraryCount;
}
