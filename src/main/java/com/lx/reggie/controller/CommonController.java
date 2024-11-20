package com.lx.reggie.controller;

import com.lx.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) { //参数名必须与前端（from data）保持一致
        log.info("aaa");
        //file是个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        try {
            //将文件转存至指定位置
            file.transferTo(new File("D:\\study\\java\\ruiji\\ruiji\\src\\main\\resources\\uploadImage\\Hello.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
