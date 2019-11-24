package cn.dblearn.blog.entity.sys;

import cn.dblearn.blog.common.validator.group.AddGroup;
import cn.dblearn.blog.common.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author xsping
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TagCategory对象", description="标签分类关系表")
public class SysTagCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "分类标识不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "分类标识")
    private Integer categoryId;

    @NotNull(message = "标签标识不能为空" , groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签标识" )
    private Integer tagId;

    @ApiModelProperty(value = "插入时间" )
    private LocalDateTime operatedate;


}
