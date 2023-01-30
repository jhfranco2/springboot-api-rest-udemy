package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Region;



public interface IUploadFileService {
    public Resource cargar(String nombreFoto) throws MalformedURLException;

    public String copiar(MultipartFile archivo) throws IOException;

    public boolean eliminar(String nombreFoto);

    public Path getPath(String nombreFoto);

}
