package com.qhg.sgp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "columnist")
public class Columnist {
    @JSONField(name = "columnist_id")
    @Id
    private Integer columnistId;
    @JSONField(name = "columnist_img")
    private String columnistImg;
    @JSONField(name = "columnist_name")
    private String columnistName;
    private String description;
    @JSONField(name = "library_count")
    private Integer libraryCount;
}
