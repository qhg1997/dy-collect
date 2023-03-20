package com.qhg.bk.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名：renting
 * 包  名：models.db
 * 创建者：乔回国
 * 创建时间：2022/11/14 9:27
 * 描述：楼盘信息
 */
@Entity
@Data
@Table(name = "real_estate_new")
public class RealEstate {
    /*固定信息*/
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 商户
     */
    private Long business_id = 0L;

    /**
     * 店铺
     */
    private Long branch_id = 0L;
    /**
     * 城市
     */
    private Integer city_id = 0;
    /**
     * 维护人
     */
    private Long maintainer_id = 0L;


    /**
     * 楼盘名称
     */
    private String name;

    /**
     * 楼盘地址
     */
    private String address;

    /**
     * 手选 楼盘区域
     */
    private Integer region_id;
    /**
     * 地图获取的行政区
     */
    private Integer ad_region_id;
    /**
     * 楼盘定位信息
     */
    private String position;

    /*销售信息*/

    /**
     * 参考价格
     */
    private BigDecimal price;

    /**
     * 楼盘标签
     */
    private String label;
    /**
     * 楼盘联系人
     */
    private String contact_name;
    /**
     * 楼盘联系人电话
     */
    private String contact_phone;

    /*楼盘信息*/

    /**
     * 开发商
     */
    private String developer;
    /**
     * 用途 1 住宅 2 商用 3商住
     */
    private String _usage;
    /**
     * 产权  ??年
     */
    private String property;
    /**
     * 建筑类型  1塔楼 2板楼 3塔板结合
     */
    private String build_type;
    /**
     * 装修标准  1毛胚 2精装
     */
    private String adorn_standard = "-";
    /**
     * 开盘时间
     */
    private String open_time;
    /**
     * 占地面积 .
     */
    private String floor_space;
    /**
     * 建筑面积 .
     */
    private String build_space;
    /**
     * 楼栋
     */
    private String roof;
    /**
     * 规划户数
     */
    private String door;
    /**
     * 绿化率
     */
    private String green_rate;
    /**
     * 容积率
     */
    private String volume_rate;
    /**
     * 公摊率
     */
    private String share_rate;
    /**
     * 规划车位
     */
    private String parking_space;
    /**
     * 物业公司
     */
    private String manage_company;
    /**
     * 物业费用
     */
    private String manage_fee;
    /**
     * 供水 1民水 2商水 3民水/商水
     */
    private String water;
    /**
     * 供电 1民电 2商电 3民电/商电
     */
    private String electric;
    /**
     * 供暖 1自采暖 2集中供暖 3自采暖/集中供暖
     */
    private String heat;

    /**
     * 效果图
     */
    private String render_files;
    /**
     * 沙盘图
     */
    private String sand_files;
    /**
     * 区位图
     */
    private String location;
    /**
     * 实景图
     */
    private String real_files;
    /**
     * 小区配套
     */
    private String support_files;
    /**
     * 楼盘备注/特色（内部可见）
     */
    private String remarks;
    /**
     * 佣金周期
     */
    private String commission_cycle;
    private Integer status = 0;//0 待完善 1 已完善
    private Date create_time = new Date();
    private Date update_time = new Date();
}
