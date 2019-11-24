package cn.dblearn.blog.manage.sys.service;


import cn.dblearn.blog.common.util.PageUtils;
import cn.dblearn.blog.entity.sys.SysTagCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsping
 * @since 2019-11-20
 */
public interface SysTagCategoryService extends IService<SysTagCategory> {

    /**
     * 分页查询
     * @param params
     * @return
     */
     PageUtils queryPage(Map<String, Object> params);
}
