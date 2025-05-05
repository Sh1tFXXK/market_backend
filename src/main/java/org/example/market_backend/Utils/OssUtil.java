package org.example.market_backend.Utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
public class OssUtil {
    /**
     * 向阿里云的OSS存储中存储文件，file也可以用InputStream 代替
     * @parameter client
     * OSS客户端
     * @parameter fileName
     * 上传文件名
     * @parameter bucketName
     * 存储空间名
     * @parameter diskName
     * 存储空间中的文件夹名
     * @return String 唯一MD5编码
     */
    public static  boolean uploadInputStreamObject2Oss(OSS client, InputStream is, String fileName, String bucketName, String diskName){

        try {
            log.info("开始上传文件到阿里云OSS");
            //创建上传Ogject的Metadata
//            ObjectMetadate metadata = new ObjectMetadate();
//            metadata.setContentLength(is.available());
//            metadata.setCacheControl("no-cache");
//            metadata.setHeader("Pragma", "no-cache");
//            metadata.setContentEncoding("utf-8");
//            metadata.setContentType(getContentType(fileName));
            log.info("uploading"+ bucketName+diskName+fileName);
            //上传文件
            PutObjectResult add=client.putObject(bucketName, diskName+"/"+fileName, is);
            log.info("上传文件到阿里云OSS成功");
            log.info("上传文件 result:"+ JSONObject.toJSONString(add));
        }catch (Exception e){
            log.error("上传文件到阿里云OSS失败");
            log.error("上传文件到阿里云OSS失败"+e.getMessage());
            return false;
        }finally {
            if(is!=null){
                log.info("关闭文件流");
                try {
                    is.close();
                } catch (Exception e) {
                    log.error("关闭文件流失败",e);
                }
                client.shutdown();
            }
        }
        return true;
    }
    public static OSS getOssClient() {
            return new OSSClientBuilder().build("your-endpoint", "your-access-key-id", "your-access-key-secret");
        }
    public static String getBucketName() {
            return "your-bucket-name";
        }
    public static String getOssUrl() {
            return "https://your-bucket-name.your-region.oss.aliyun.com";
        }
    private static class ObjectMetadate {
        private long contentLength;
        private String cacheControl;
        private String pragma;
        private String contentEncoding;
    }
    /**
     * 向阿里云的OSS存储中存储文件，批量上传
     * @parameter client
     * OSS客户端
     * @param files
     * 上传文件名
     * @parameter bucketName
     * 存储空间名
     * @parameter diskName
     * 存储空间中的文件夹名
     * @return String 唯一MD5编码
     */
    public static  StringBuilder batchUploadInputStreamObject2Oss(OSS client, MultipartFile[] files,  String bucketName, String diskName) {
        //TODO
        return null;}

}