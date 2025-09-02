package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品实体类
 * </p>
 * <p>
 * 用于管理企业产品信息，包括产品标题、内容、图片等，支持首页展示配置
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID，主键，自增
     */
    private Integer id;

    /**
     * 产品标题
     * 产品的名称，用于显示和识别产品
     */
    private String title;

    /**
     * 产品主图URL
     * 产品的主要展示图片，存储相对路径，访问时会自动拼接本地文件访问前缀
     */
    private String productMainUrl;

    /**
     * 产品HTML内容
     * 产品的详细内容，支持HTML格式，用于展示产品的完整信息
     */
    private String htmlContent;

    /**
     * 创建时间
     * 产品记录的创建时间，默认为当前时间
     */
    private Date createdAt;

    /**
     * 产品简短描述
     * 产品的简要说明，用于快速了解产品功能和特点
     */
    private String shortDesc;

    /**
     * 是否在首页展示
     * 控制产品是否在首页进行展示，TRUE表示展示，FALSE表示不展示
     */
    private Boolean showIndex;

}
