/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Hello我WorldController
 * Author:   xsping
 * Date:     2019/11/1 19:42
 * Description: 万事第一步,hollow world spring boot!
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.dblearn.blog.auth.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈完成yml文件配置后 需要添加访问地址,否则会404〉
 *
 * @author xsping
 * @create 2019/11/1
 * @since 1.0.0
 */
@RestController
public class DataBaseController {

    @GetMapping("/druid/stat")
    public Object druidStat(){
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
