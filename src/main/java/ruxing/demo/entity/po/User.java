package ruxing.demo.entity.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ruxing on 08/12/2016.
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1043088843047983374L;

    /**
     * 主键
     */
    @JSONField(name = "user_id")
    private Long userId;

    /**
     * 名字
     */
    private String name;

    /**
     * 年龄
     */
    private Long age;

    /**
     * 城市
     */
    private String city;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JSONField(name = "update_time")
    private Date UpdateTime;

    /**
     * 删除标记(0 未删除 1 删除)
     */
    private Boolean deleted;

}
