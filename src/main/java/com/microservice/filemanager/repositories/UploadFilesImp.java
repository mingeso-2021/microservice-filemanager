package com.microservice.filemanager.repositories;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
// Java imports
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class UploadFilesImp implements UploadFiles{

    private String pathFolder = "root/uploaded-files";
    private final File directory = new File( pathFolder );

    @Override
    public void save( MultipartFile file, String value ) throws Exception{
        // if root directory doesn't exist, then create
        if ( !directory.exists() ) directory.mkdir();
        String newPath = pathFolder + "/" + value;
        // same with the new directory route using postulant's name like value
        File directoryRoute = new File ( newPath );
        if ( !directoryRoute.exists() ) directoryRoute.mkdir();
        Path rootFolder = Paths.get( newPath );
        Files.copy(file.getInputStream(), rootFolder.resolve(file.getOriginalFilename()));
    }

    @Override
    public Resource load(String name, String value) throws Exception{
        Path rootFolder = Paths.get( pathFolder + value );
        Path file = rootFolder.resolve(name);
        return new UrlResource(file.toUri());
    }

    @Override
    public void save(List<MultipartFile> files, String value) throws Exception{
        for (MultipartFile file : files) {
            this.save( file, value );
        }
    }

    @Override
    public Stream<Path> loadAll() throws Exception{
        Path rootFolder = Paths.get( pathFolder );
        return Files.walk(rootFolder, 1).filter(path -> !path.equals(rootFolder)).map(rootFolder::relativize);
    }
}
