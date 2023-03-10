package com.qhg.bk.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bk_xiaoqu_info")
public class XiaoquInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String info;
}
