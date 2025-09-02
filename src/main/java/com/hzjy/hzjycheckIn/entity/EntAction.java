package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 企业活动实体类
 * </p>
 * <p>
 * 用于管理企业内部的各种活动信息，包括活动标题、内容、图片等，支持首页展示配置
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
@TableName("ent_action")
public class EntAction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业活动ID，主键，自增
     */
    private Integer id;

    /**
     * 活动标题
     * 企业活动的名称，用于显示和识别活动
     */
    private String title;

    /**
     * 活动主图URL
     * 活动的主要展示图片，存储相对路径，访问时会自动拼接本地文件访问前缀
     */
    private String actionMainUrl;

    /**
     * 活动HTML内容
     * 活动的详细内容，支持HTML格式，用于展示活动的完整信息
     */
    private String htmlContent;

    /**
     * 创建时间
     * 活动记录的创建时间，默认为当前时间
     */
    private Date createdAt;

    /**
     * 活动简短描述
     * 活动的简要说明，用于快速了解活动内容
     */
    private String shortDesc;

    /**
     * 是否在首页展示
     * 控制活动是否在首页进行展示，TRUE表示展示，FALSE表示不展示
     */
    private Boolean showIndex;

}
