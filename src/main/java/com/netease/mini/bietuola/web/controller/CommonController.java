package com.netease.mini.bietuola.web.controller;

import com.netease.mini.bietuola.config.nos.ContentType;
import com.netease.mini.bietuola.config.nos.NosService;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/5/6
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {
    private static final Logger LOG = LoggerFactory.getLogger(CommonController.class);

    private final NosService nosService;

    public CommonController(NosService nosService) {
        this.nosService = nosService;
    }

    /**
     * 上传文件
     * @param object
     * @return
     */
    @PostMapping("/object/upload")
    public JsonResponse uploadObj(MultipartFile object) {
        if (object == null || object.isEmpty()) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("上传文件为空");
        }
        String originFilename = object.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(originFilename, ".");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String objectName = StringUtils.isBlank(suffix) ? uuid : uuid + "." + suffix;
        InputStream in;
        try {
            in = object.getInputStream();
            nosService.uploadStream(in, in.available(), objectName, suffix);
        } catch (Exception e) {
            LOG.error("upload object fail, objectFilename:{}", originFilename, e);
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("上传失败");
        }
        Map<String, String> data = new HashMap<>();
        data.put("url", nosService.getNosUrlPrefix() + objectName);
        return JsonResponse.success(data);
    }

}
