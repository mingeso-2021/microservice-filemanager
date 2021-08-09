package com.microservice.filemanager.repositories;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class UploadFilesImp implements UploadFiles{

    private final Path rootFolder = Paths.get("../superadmin/uploaded-files");

    @Override
    public void save(MultipartFile file) throws Exception{
        Files.copy(file.getInputStream(), this.rootFolder.resolve(file.getOriginalFilename()));
    }

    @Override
    public Resource load(String name) throws Exception{
        Path file = rootFolder.resolve(name);
        return new UrlResource(file.toUri());
    }

    @Override
    public void save(List<MultipartFile> files) throws Exception{
        for (MultipartFile file : files) {
            this.save(file);
        }
    }

    @Override
    public Stream<Path> loadAll() throws Exception{
        return Files.walk(rootFolder, 1).filter(path -> !path.equals(rootFolder)).map(rootFolder::relativize);
    }
}
