package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/9/10 8:58
 * @version 1.0
 */
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    MediaFilesMapper mediaFilesMapper;
    @Autowired
    MinioClient minioClient;

    @Autowired
    MediaFileService currentProxy;

    // 普通文件桶
    @Value("${minio.bucket.files}")
    private String bucket_files;
    // 视频文件桶
    @Value("${minio.bucket.videofiles}")
    private String bucket_video_files;

    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();

        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName) {

        String md5Hex = DigestUtils.md5Hex(bytes);
        // 处理存储名称
        if (StringUtils.isEmpty(folder))
            folder = getFileFolder(true, true, true);
        else
            folder = folder + "/";
        if (StringUtils.isEmpty(objectName)) {
            // 如果名称为空，使用文件的md5作为名称
            String filename = uploadFileParamsDto.getFilename();
            objectName = md5Hex + filename.substring(filename.lastIndexOf("."));
        }
        objectName = folder + objectName;

        try {
            addMediaFilesToMinIO(bytes, bucket_files, objectName);
            MediaFiles mediaFiles = currentProxy.addMediaFilesToMinDB(companyId, uploadFileParamsDto, objectName, md5Hex, bucket_files);
            // 返回数据
            UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
            BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
            return uploadFileResultDto;
        } catch (Exception e) {
            log.debug("{}上传失败：{}",uploadFileParamsDto.getFilename() , e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
//        return null;
    }

    @Transactional
    public MediaFiles addMediaFilesToMinDB(Long companyId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileId, String bucket) {
        // 保存到数据库
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
        if (mediaFiles == null) {
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            mediaFiles.setId(fileId);
            mediaFiles.setFileId(fileId);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setBucket(bucket);
            mediaFiles.setFilePath(objectName);
            mediaFiles.setUrl("/" + bucket_files + "/" + objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setAuditStatus("002003");
            mediaFilesMapper.insert(mediaFiles);
        }
        return mediaFiles;
    }

    private void addMediaFilesToMinIO(byte[] bytes, String bucket, String objectName) {

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (objectName.indexOf(".") >= 0) {
            String extension = objectName.substring(objectName.lastIndexOf("."));
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            if (extensionMatch != null)
                contentType = extensionMatch.getMimeType();
        }

        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            // 上传到minio
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            log.debug("上传文件到文件系统出错：{}", e.getMessage());
            XueChengPlusException.cast(e.getMessage());
        }

    }

    // 根据日期拼接目录
    private String getFileFolder(boolean year, boolean month, boolean day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前日期字符串
        String dateString = sdf.format(new Date());
        //取出年、月、日
        String[] dateStringArray = dateString.split("-");
        StringBuffer folderString = new StringBuffer();
        if(year){
            folderString.append(dateStringArray[0]);
            folderString.append("/");
        }
        if(month){
            folderString.append(dateStringArray[1]);
            folderString.append("/");
        }
        if(day){
            folderString.append(dateStringArray[2]);
            folderString.append("/");
        }
        return folderString.toString();
    }

    /***
     * @description 查询文件是否存在
     * @param fileMd5
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean>
     * @author Hao Ge
     * @date 2023/2/2 19:15*/
    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        // 查询文件信息
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            // 获取基本信息
            String bucket = mediaFiles.getBucket();
            String filePath = mediaFiles.getFilePath();
            InputStream stream = null;
            try {
                stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(filePath)
                                .build());
                if (stream != null)
                    // 文件已经存在
                    return RestResponse.success(true);
            } catch (Exception e) {
                log.debug("查询文件异常：{}", e.getMessage());
            }
        }
        return RestResponse.success(false);
    }

    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        // 得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        // 查询
        InputStream fileInputStream = null;
        try {
            fileInputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket_video_files)
                            .object(chunkFilePath)
                            .build());
            if (fileInputStream != null)
                // 分块已经存在
                return RestResponse.success(true);
        } catch (Exception e) {
            log.debug("查询文件块异常：{}", e.getMessage());
        }
        return RestResponse.success(false);
    }

    /***
     * @description 上传分块
     * @param fileMd5
     * @param chunk
     * @param bytes
     * @return com.xuecheng.base.model.RestResponse
     * @author Hao Ge
     * @date 2023/2/2 19:28*/
    @Override
    public RestResponse uploadChunk(String fileMd5, int chunk, byte[] bytes) {
        // 获取文件分块的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        String chunkFilePath = chunkFileFolderPath + chunk;

        try {
            addMediaFilesToMinIO(bytes, bucket_video_files, chunkFilePath);
            return RestResponse.success(true);
        } catch (Exception e) {
            log.debug("上传分块文件:{},失败:{}",chunkFilePath,e.getMessage());
        }
        return RestResponse.validfail(false, "上传分块失败");
    }

    @Override
    public RestResponse mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        // 下载分块
        File[] chunkFiles = checkChunkStatus(fileMd5, chunkTotal);
        // 获得名称
        String filename = uploadFileParamsDto.getFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        File tempMergeFile = null;
        try {
            try {
                //创建一个临时文件作为合并文件
                tempMergeFile = File.createTempFile("'merge'", extension);
            } catch (IOException e) {
                XueChengPlusException.cast("创建临时合并文件出错");
            }
            // 创建合并文件的流对象
            try (RandomAccessFile raf_write = new RandomAccessFile(tempMergeFile, "rw")) {
                byte[] b = new byte[1024]; // 一个分块1024个字节，1M
                for (File file : chunkFiles) {
                    try (RandomAccessFile raf_read = new RandomAccessFile(file, "r")) {
                        int len = -1;
                        while ((len = raf_read.read(b)) != -1) {
                            // 向合并文件写数据
                            raf_write.write(b, 0, len);
                        }
                    }
                }
            } catch (IOException e) {
                XueChengPlusException.cast("合并文件过程出错");
            }

            // 校验合并后的文件是否正确
            try {
                FileInputStream mergeFileStream = new FileInputStream(tempMergeFile);
                String mergeMd5Hex = DigestUtils.md5Hex(mergeFileStream);
                if (!fileMd5.equals(mergeMd5Hex)) {
                    log.debug("合并文件校验不通过,文件路径:{},原始文件md5:{}", tempMergeFile.getAbsolutePath(), fileMd5);
                    XueChengPlusException.cast("合并文件校验不通过");
                }
            } catch (IOException e) {
                log.debug("合并文件校验出错,文件路径:{},原始文件md5:{}", tempMergeFile.getAbsolutePath(), fileMd5);
                XueChengPlusException.cast("合并文件校验出错");
            }

            // 拿到合并文件在minio的存储路径并上传
            String mergeFilePath = getFilePathByMd5(fileMd5, extension);
            addMediaFilesToMinIO(tempMergeFile.getAbsolutePath(), bucket_video_files, mergeFilePath);

            // 文件信息入库
            uploadFileParamsDto.setFileSize(tempMergeFile.length());//合并文件的大小
            addMediaFilesToMinDB(companyId, uploadFileParamsDto, fileMd5, bucket_video_files, mergeFilePath);

            return RestResponse.success(true);
        } finally {
            //删除临时分块文件
            if(chunkFiles != null){
                for (File chunkFile : chunkFiles) {
                    if(chunkFile.exists()){
                        chunkFile.delete();
                    }
                }
            }
            //删除合并的临时文件
            if(tempMergeFile != null){
                tempMergeFile.delete();
            }
        }
    }

    // 大文件上传
    private void addMediaFilesToMinIO(String filePath, String bucket, String objectName){
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(filePath)
                    .build();
            //上传
            minioClient.uploadObject(uploadObjectArgs);
            log.debug("文件上传成功:{}",filePath);
        } catch (Exception e) {
            XueChengPlusException.cast(e.getMessage());
        }
    }

    private String getFilePathByMd5(String fileMd5,String fileExt){
        return fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }

    private File[] checkChunkStatus(String fileMd5, int chunkTotal) {
        // 获取分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File[] files = new File[chunkTotal];
        // 检查分块是否上传完毕
        for (int i = 0; i < chunkTotal; i++) {
            String chunkFilePath = chunkFileFolderPath + i;
            File chunkFile = null;
            try {
                chunkFile = File.createTempFile("chunk", null);
            } catch (IOException e) {
                e.printStackTrace();
                XueChengPlusException.cast("创建临时分块文件出错: " + e.getMessage());
            }
            // 下载分块文件
            downloadFileFromMinIO(chunkFile, bucket_video_files, chunkFilePath);
            files[i] = chunkFile;
        }
        return files;
    }

    private File downloadFileFromMinIO(File file, String bucket, String objectName) {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(objectName).build();
        try(
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            FileOutputStream outputStream =new FileOutputStream(file);
        ) {
            IOUtils.copy(inputStream,outputStream);
            return file;
        }catch (Exception e){
            e.printStackTrace();
            XueChengPlusException.cast("查询分块文件出错");
        }
        return null;
    }

    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "";
    }
}
