package cn.dblearn.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 类别表
 * </p>
 *
 * @author bobbi
 * @since 2018-12-17
 */
@Data
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "排序")
    @TableField(value = "`rank`")
    private Integer rank;

    @ApiModelProperty(value = "父主键")
    @NotNull(message = "父主键不能为空")
    private Integer parentId;

    @ApiModelProperty(value = "level级别" )
    @NotNull(message = "level级别不能为空" )
    private String level;

    @TableField(exist = false)
    private String parentName;


}
