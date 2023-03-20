package com.qhg.bk.model;

import com.alibaba.fastjson.JSONObject;
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

    public RealEstate toRe(Xiaoqu xiaoqu) {
        JSONObject jsonObject = new JSONObject() {{
            put("临潼", 1111);
            put("周至", 1615);
            put("新城区", 1106);
            put("未央", 1104);
            put("灞桥", 1107);
            put("碑林", 1103);
            put("莲湖", 1102);
            put("蓝田", 1614);
            put("西咸新区（西安）", 1635);
            put("鄠邑区", 1110);
            put("长安", 1105);
            put("阎良", 1108);
            put("雁塔", 1101);
            put("高陵", 1109);
        }};
        JSONObject object = JSONObject.parseObject(this.info);
        System.out.println(object);
        RealEstate realEstate = new RealEstate();
        realEstate.setName(xiaoqu.getName());
        realEstate.setAddress(object.getString("address"));
        realEstate.setRegion_id(jsonObject.getInteger(xiaoqu.getRegion()));
        realEstate.setAd_region_id(jsonObject.getInteger(xiaoqu.getRegion()));

        realEstate.setPosition(object.getString("pointLng") + "," + object.getString("pointLat"));
        object = object.getJSONObject("attr");
        realEstate.setDeveloper(object.getString("开发企业"));
        realEstate.set_usage(object.getString("房屋用途"));
        realEstate.setBuild_type(object.getString("建筑类型"));
        realEstate.setRoof(object.getString("楼栋数"));
        realEstate.setDoor(object.getString("总户数"));
        realEstate.setGreen_rate(object.getString("绿化率"));
        realEstate.setVolume_rate(object.getString("容积率"));
        realEstate.setParking_space(object.getString("固定车位"));
        realEstate.setManage_company(object.getString("物业公司"));
        realEstate.setManage_fee(object.getString("物业费"));
        realEstate.setWater(object.getString("用水类型"));
        realEstate.setElectric(object.getString("用电类型"));
        realEstate.setHeat(object.getString("供暖类型"));
        String real_files = "";
        realEstate.setReal_files(real_files);
        return realEstate;
    }
}
