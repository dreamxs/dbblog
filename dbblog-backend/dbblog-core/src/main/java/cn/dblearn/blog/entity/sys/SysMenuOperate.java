package cn.dblearn.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单管理操作
 * </p>
 *
 * @author bobbi
 * @since 2018-10-19
 */
@Data
@ApiModel(value="SysMenuOperate对象", description="菜单管理按钮")
public class SysMenuOperate implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "menuid 菜单id")
    private Integer menuid;

    @ApiModelProperty(value = "权限操作名称")
    private String operatename;

    @ApiModelProperty(value = "权限类别")
    private String type;




}
