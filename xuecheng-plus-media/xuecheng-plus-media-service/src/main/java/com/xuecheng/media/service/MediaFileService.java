package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

    /**
     * @description 媒资文件查询方法
     * @param pageParams 分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     * @author Mr.M
     * @date 2022/9/10 8:57
     */
    public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /***
     * @description 上传文件接口
     * @param companyId
     * @param uploadFileParamsDto
     * @param bytes
     * @param folder
     * @param objectName
     * @return com.xuecheng.media.model.dto.UploadFileResultDto
     * @author Hao Ge
     * @date 2023/2/2 13:29*/
    public UploadFileResultDto uploadFile (Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);

    /***
     * @description 存储file数据到数据库
     * @param companyId
     * @param uploadFileParamsDto
     * @param objectName
     * @param fileId
     * @param bucket
     * @return com.xuecheng.media.model.po.MediaFiles
     * @author Hao Ge
     * @date 2023/2/2 17:36*/
    public MediaFiles addMediaFilesToMinDB(Long companyId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileId, String bucket);

    /***
     * @description 检查文件是否存在
     * @param fileMd5
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean>
     * @author Hao Ge
     * @date 2023/2/2 19:08*/
    public RestResponse<Boolean> checkFile(String fileMd5);

    /***
     * @description 检查分块是否存在
     * @param fileMd5
     * @param chunkIndex
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean>
     * @author Hao Ge
     * @date 2023/2/2 19:09*/
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

    /***
     * @description 上传分块
     * @param fileMd5
     * @param chunk
     * @param bytes
     * @return com.xuecheng.base.model.RestResponse
     * @author Hao Ge
     * @date 2023/2/2 19:24*/
    public RestResponse uploadChunk(String fileMd5,int chunk,byte[] bytes);

    /***
     * @description 合并分块
     * @param companyId
     * @param fileMd5
     * @param chunkTotal
     * @param uploadFileParamsDto
     * @return com.xuecheng.base.model.RestResponse
     * @author Hao Ge
     * @date 2023/2/2 20:32*/
    public RestResponse mergechunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);

}
