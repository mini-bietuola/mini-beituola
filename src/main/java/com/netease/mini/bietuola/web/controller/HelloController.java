package com.netease.mini.bietuola.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.mini.bietuola.config.redis.RedisService;
import com.netease.mini.bietuola.constant.Role;
import com.netease.mini.bietuola.entity.Hello;
import com.netease.mini.bietuola.service.HelloService;
import com.netease.mini.bietuola.web.controller.query.HelloQuery;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
@RestController
@RequestMapping("/api/hello")
public class HelloController {

    private final static Logger LOG = LoggerFactory.getLogger(Hello.class);

    private final HelloService helloService;
    private final RedisService redisService;

    public HelloController(HelloService helloService, RedisService redisService) {
        this.helloService = helloService;
        this.redisService = redisService;
    }

    @GetMapping("/list")
    public JsonResponse listHello(HelloQuery helloQuery) {
        System.out.println(helloQuery);
        // 分页演示，如果要分页，按照以下三个步骤（注意：1、2两步骤要不都执行，要不都不执行）
        // 1、设置分页参数
        PageHelper.startPage(helloQuery.getPageNumber(), helloQuery.getPageSize());
        // 2、执行mybatis查询
        List<Hello> lists;
        if (StringUtils.isNotBlank(helloQuery.getName())) {
            lists = helloService.getHelloByName(helloQuery.getName());
        } else {
            lists = helloService.listHello();
        }
        // 3、包装分页结果
        PageInfo<Hello> result = new PageInfo<>(lists);
        return JsonResponse.success(result);
        // 如果出错，使用以下代码
//        return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数错误");
    }

    @PostMapping("/create")
    public JsonResponse create(String name) {
        Hello h = new Hello();
        h.setName(name);
        h.setAge(22);
        h.setCreateTime(System.currentTimeMillis());
        h.setDay(20180202);
        h.setPrice(34.4444);
        h.setRole(Role.USER);
        if (helloService.save(h)) {
            LOG.info("创建hello");
            return JsonResponse.success();
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("保存失败");
    }

    @PutMapping("/update")
    public JsonResponse update(String name, long id) {
        if (helloService.update(name, id)) {
            return JsonResponse.success();
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("更新失败");
    }

    @GetMapping("/redis")
    public void redisOps() {
        Hello hello = new Hello();
        hello.setName("hellll");
        redisService.set("hello1", hello); // 1
        redisService.getStringRedisTemplate().opsForValue().set("stringKey", "stringValue"); // 2

        System.out.println((Hello) redisService.get("hello1")); // 1
        System.out.println(redisService.getStringRedisTemplate().opsForValue().get("stringKey"));  //2

    }

}
