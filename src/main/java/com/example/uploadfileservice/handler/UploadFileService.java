package com.example.uploadfileservice.handler;

import java.nio.file.Path;
import java.util.List;

public interface UploadFileService {

    void uploadFile(Path path, String fileExtension, long fileId);
}
