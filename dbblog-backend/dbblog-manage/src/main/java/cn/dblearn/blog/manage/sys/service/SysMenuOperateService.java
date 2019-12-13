package cn.dblearn.blog.manage.sys.service;


import cn.dblearn.blog.entity.sys.SysMenuOperate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 功能描述: <br>
 * 获取菜单操作类型
 * @since: 1.0.0
 * @Author: xsping
 * @email: 727162355@qq.com
 * @Date: 2019/12/9 22:23
 */

public interface SysMenuOperateService extends IService<SysMenuOperate> {
    /**
     * 获取指定菜单的所有操作
     * @param menuid
     * @return
     */
    List<SysMenuOperate> listMenuOperate(Integer menuid);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
   /* List<SysMenuOperate> queryListParentId(Integer parentId, List<Integer> menuIdList);*/

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    /*List<SysMenuOperate> queryListParentId(Integer parentId);*/

    /**
     * 获取不包含按钮的菜单列表
     */
    /*List<SysMenuOperate> queryNotButtonList();*/

    /**
     * 获取用户菜单列表
     */
   /* List<SysMenuOperate> getUserMenuList(Integer userId);*/

    /**
     * 删除
     */
    /*void delete(Integer menuId);*/

    /**
     * 最大列id
     */
    int queryMaxId();
}
