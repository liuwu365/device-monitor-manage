package com.device.manage.web.controller.base;

import com.device.api.entity.Result;
import com.device.manage.core.constant.BasicConstant;
import com.device.manage.web.service.FileService;
import com.device.api.uitls.ErrorWriterUtil;
import com.yunpian.sdk.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;


/**
 * @author Administrator
 * @description 单文件上传控制器
 */
@Controller
public class FileUploadController {

    Logger logger = LoggerFactory.getLogger(FileUploadController.class);


    @Resource
    private FileService fileService;

    String format[] = {"image/bmp", "image/jpg", "image/jpeg", "image/png", "image/gif"};

    long maxSize = 2 * 1024;

    String _Square_ = "_Square_";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadHero(Model model, HttpServletRequest request, int gameTypeId, String eName) {
        String fileName = null;
        BufferedOutputStream stream = null;

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("Filedata");

            if (multipartFile == null) {
                Set<String> strings = ((DefaultMultipartHttpServletRequest) multipartRequest).getMultiFileMap().keySet();
                for (String fileId : strings) {
                    multipartFile = multipartRequest.getFile(fileId);
                    if (multipartFile != null) {
                        break;
                    }
                }
            }
            if (multipartFile == null) {
                return null;
            } else {

                long size = multipartFile.getSize();
                String contentType = multipartFile.getContentType();
                logger.info("图片大小:{}KB-{}字节", size / 1024, size);
                logger.info("contentType={}", contentType);
                if (multipartFile.isEmpty()) {
                    return "3";
                } else if (size / 1024 > maxSize) {
                    return "2";
                } else if (!Arrays.asList(format).contains(contentType.toLowerCase())) {
                    return "1";
                } else {
                    String ml = "/data/source/hero/";
                    InputStream inputStream = multipartFile.getInputStream();
                    int len = inputStream.available();
                    byte[] file_buff = new byte[len];
                    inputStream.read(file_buff);
                    File fileDir = new File(ml);
                    if (!fileDir.exists()) {
                        fileDir.mkdir();
                        logger.info("目录：{}，创建成功！", ml);
                    }
                    String img_suffix = contentType.split("/")[1];
                    fileName = getHeroName(eName, gameTypeId, img_suffix);
                    logger.info("fileName={}", fileName);
                    stream = new BufferedOutputStream(new FileOutputStream(ml + fileName));
                    stream.write(file_buff);

                }
            }
        } catch (IOException e) {
            logger.info("上传英雄图片异常：gameTypeId={}|eName={}|ex={}", gameTypeId, eName, ErrorWriterUtil.writeError(e).toString());

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.info("输出IO 异常");

                }
            }
        }
        return fileName;
    }

    public String getHeroName(String eName, int suffix_no, String img_format) {
        StringBuilder sb = new StringBuilder();
        sb.append(eName).append(_Square_).append(suffix_no - 1).append(".").append(img_format);
        return sb.toString();
    }


    @RequestMapping(value = "/upload1", method = RequestMethod.POST)
    public void handleFileUpload(Model model, HttpServletRequest request,
                                 HttpServletResponse response, @RequestParam("upload_org_code") MultipartFile file) {
        Result result = new Result();
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartFile file = multipartRequest.getFile("Filedata");
        if (!file.isEmpty()) {
            String name = String.valueOf(System.currentTimeMillis());
            try {
                byte[] bytes = file.getBytes();
                name += file.getOriginalFilename();
                String path = FileUploadController.class.getResource("/").getPath();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(path.concat(name))));
                stream.write(bytes);
                stream.close();
                result.setCode(200);
                result.setMsg(name);

                model.addAttribute("msg", path.concat(name));
//                return JsonUtil.gson.toJson(result);
//                return "You successfully uploaded " + name + " into " + name + "!\nurl=" + s;
            } catch (Exception e) {
                result.setCode(601);
                result.setMsg("You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            result.setCode(410);
            result.setMsg("You failed to upload  because the file was empty.");
        }
        logger.info(JsonUtil.toJson(result));
    }


    @RequestMapping(value = "uploads", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uploads(HttpServletRequest request,
                          HttpServletResponse response,
                          Model model, @RequestParam(value = "type", required = false) String type) throws Exception {
        String s = null;
        try {
            s = fileService.upLoad(request, type);
        } catch (Exception ex) {
            logger.error(ErrorWriterUtil.writeError(ex).toString());
            ex.printStackTrace();
            s = null;
        }

        return s;
    }

    @RequestMapping(value = "uploadsToJson", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result uploadsToJson(HttpServletRequest request, @RequestParam(value = "type", required = false) String type) {
        try {
            String img_url = fileService.upLoad(request, type);

            return Objects.isNull(img_url)?Result.failure(BasicConstant.UPLOAD_FAILED):Result.success(BasicConstant.UPLOAD_SUCCESS,img_url);
        } catch (Exception ex) {
            logger.error(ErrorWriterUtil.writeError(ex).toString());
            return Result.error();
        }
    }


}