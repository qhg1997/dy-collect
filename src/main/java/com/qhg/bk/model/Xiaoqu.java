package com.qhg.bk.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bk_xiaoqu")
public class Xiaoqu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String region;
    private String xqid;
    private String info;
}
