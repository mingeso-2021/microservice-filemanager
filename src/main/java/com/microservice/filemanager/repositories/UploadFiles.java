package com.microservice.filemanager.repositories;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface UploadFiles {
    public void save( MultipartFile file, String value ) throws Exception;
    public byte[] load( String name, String value ) throws Exception;
    public void save( List<MultipartFile> file, String value ) throws Exception;
    public Stream<Path> loadAll() throws Exception;
}

