package cn.dblearn.blog.manage.sys.service.impl;

import cn.dblearn.blog.common.util.PageUtils;
import cn.dblearn.blog.common.util.Query;
import cn.dblearn.blog.entity.sys.SysTagCategory;
import cn.dblearn.blog.manage.sys.service.SysTagCategoryService;
import cn.dblearn.blog.mapper.sys.SysTagCategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsping
 * @since 2019-11-20
 */
@Service
@Slf4j
public class SysTagCategoryServiceImpl extends ServiceImpl<SysTagCategoryMapper, SysTagCategory> implements SysTagCategoryService {

    /**
     * 分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysTagCategory> page=baseMapper.selectPage(new Query<SysTagCategory>(params).getPage(),
                new QueryWrapper<SysTagCategory>().lambda());
        return new PageUtils(page);
    }

}
