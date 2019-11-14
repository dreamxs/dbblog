package cn.dblearn.blog.mapper.sys;

import cn.dblearn.blog.entity.sys.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bobbi
 * @since 2018-10-08
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户所有权限
     * @param userId
     * @return
     */
    List<String> queryAllPerms(Integer userId);

    /**
     * 查询用户的menuId
     * @param userId
     * @return
     */
    List<Integer> queryAllMenuId(Integer userId);

    /**
     * 功能描述: <br>
     * 〈返回最大的当前用户值〉
     *
     * @param null
     * @return:
     * @since: 1.0.0
     * @Author:xsping
     * @Date: 2019/11/14 22:02
     */

    int queryMaxUserId();
}
