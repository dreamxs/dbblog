package cn.dblearn.blog.manage.sys.controller;

import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.base.AbstractController;
import cn.dblearn.blog.common.util.PageUtils;
import cn.dblearn.blog.common.validator.ValidatorUtils;
import cn.dblearn.blog.entity.sys.SysTagCategory;
import cn.dblearn.blog.manage.sys.service.SysTagCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsping
 * @since 2019-11-20
 */
@RestController
@Slf4j
@RequestMapping("/admin/sys/category")
public class SysTagCategoryController extends AbstractController {
    @Autowired
    private SysTagCategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:category:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:category:info")
    public Result info(@PathVariable("id") String id){
       SysTagCategory category = categoryService.getById(id);

        return Result.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:category:save")
    public Result save(@RequestBody SysTagCategory category){
        ValidatorUtils.validateEntity(category);
        categoryService.save(category);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:category:update")
    public Result update(@RequestBody SysTagCategory category){
        ValidatorUtils.validateEntity(category);
        categoryService.updateById(category);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:category:delete")
    public Result delete(@RequestBody String[] ids){
        categoryService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }
}
