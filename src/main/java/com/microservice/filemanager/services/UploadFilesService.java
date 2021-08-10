// Project import
package com.microservice.filemanager.services;
import com.microservice.filemanager.repositories.UploadFiles;
// Spring boot native imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// Java imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/uploadfiles")
public class UploadFilesService {

    @Autowired
    private UploadFiles uploadFiles;

    @PostMapping(value="/upload/{value}")
    public ResponseEntity<String> uploadFiles(@RequestParam("file") List<MultipartFile> files, @PathVariable String value ){
        try {
            uploadFiles.save( files , value);
            return ResponseEntity.status(HttpStatus.OK).body("Los archivos fueron cargados correctamente al servidor");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El/los archivo/s ya existe/n");
        }
    }

    @RequestMapping(value="/files/{value}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName, @PathVariable String value ) throws Exception {
        byte[] contents = uploadFiles.load(fileName, value);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/getall")
    public ResponseEntity<List<File>> getAllFiles() throws Exception {
        List<File> files = uploadFiles.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(UploadFilesService.class, "getFile", path.getFileName().toString()).build().toString();
            return new File(filename, url).getAbsoluteFile();
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @PostMapping(value = "/file", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile file, RedirectAttributes attributes) throws IOException {
        try{
            if (file == null || file.isEmpty()) {
                attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Algo ocurrió mal 2");
            }
            StringBuilder builder = new StringBuilder();
            builder.append(System.getProperty("user.home"));
            builder.append(File.separator);
            builder.append("uploaded-files");
            builder.append(File.separator);
            builder.append(file.getOriginalFilename());

            byte[] fileBytes = file.getBytes();
            Path path = Paths.get(builder.toString());
            Files.write(path, fileBytes);

            attributes.addFlashAttribute("message", "Archivo cargado correctamente");
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        }catch ( Exception e ) {
            System.err.println( e ); // capturar error server-side
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Algo ocurrió mal 3");
        }
    }
}
