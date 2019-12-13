package cn.dblearn.blog.mapper.sys;

import cn.dblearn.blog.entity.sys.SysMenu;
import cn.dblearn.blog.entity.sys.SysMenuOperate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 *
 *  * 添加了@Mapper注解之后这个接口在编译时会生成相应的实现类,
 *  与之对应的mybatis sqlxml文件实现sql编写
 *  可以用@Select @Update @Delete  @Insert等注解实现简单sql
 *  *
 *  * 需要注意的是：这个接口中不可以定义同名的方法，因为会生成相同的id
 *  * 也就是说这个接口是不支持重载的
 *
 */
@Mapper
public interface SysMenuOperateMapper extends BaseMapper<SysMenuOperate> {

    int queryMaxId();

    List<SysMenuOperate> listMenuOperate(Integer menuid);
}
