package cn.yanque.models.upload.service;

import cn.yanque.models.upload.pojo.vo.req.UploadPresignReq;
import cn.yanque.models.upload.pojo.vo.res.DownloadPresignRes;
import cn.yanque.models.upload.pojo.vo.res.UploadPresignRes;

public interface UploadService {

    UploadPresignRes createUploadPresign(UploadPresignReq req);

    DownloadPresignRes createDownloadPresign(String objectKey);
}
