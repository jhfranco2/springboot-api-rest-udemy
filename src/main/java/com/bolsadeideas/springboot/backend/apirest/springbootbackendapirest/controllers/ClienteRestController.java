package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sound.midi.Patch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.services.IClienteService;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.services.IUploadFileService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200" })
public class ClienteRestController {

    private IClienteService clientesService;

    private IUploadFileService uploadService;

    //private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clientesService.findAll();
    }

    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return clientesService.findAll(pageable);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = null;
        try {
            cliente = clientesService.findById(id);
        } catch (DataAccessException e) {

            response.put("mensaje", "Error al realizar la consulata a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cliente == null) {
            response.put("mensaje",
                    "El cliente con el id ".concat(id.toString()).concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
        Cliente clienteNuevo = null;
        Map<String, Object> response = new HashMap<>();
        Boolean resultado = result.hasErrors();
        System.out.println(resultado);
        if (result.hasErrors()) {
            /*
             * List<String> errors = new ArrayList<>();
             * for (FieldError err : bindingResult.getFieldErrors()) {
             * errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
             * }
             */
            /* */ List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            clienteNuevo = clientesService.save(cliente);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con éxito!");
        response.put("cliente", clienteNuevo);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult bindingResult,
            @PathVariable Long id) {
        Cliente clienteActual = clientesService.findById(id);
        Cliente clienteActualizado = null;
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            /*
             * List<String> errors = new ArrayList<>();
             * for (FieldError err : bindingResult.getFieldErrors()) {
             * errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
             * }
             */
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (clienteActual == null) {
            response.put("mensaje",
                    "Error no se pudo editar, el cliente con el id ".concat(id.toString())
                            .concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());
            clienteActualizado = clientesService.save(clienteActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido actualizado con éxito!");
        response.put("cliente", clienteActualizado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = clientesService.findById(id);
        try {
            String nombreFotoAnterior = cliente.getFoto();
            uploadService.eliminar(nombreFotoAnterior);

            clientesService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente se ha eliminado con éxito");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        Cliente cliente = clientesService.findById(id);

        if (!archivo.isEmpty()) {
            String nombreArchivo = null;
            try {
                nombreArchivo = uploadService.copiar(archivo);
            } catch (IOException e) {
                response.put("mensaje", "Error al subir la imagen del cliente ");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

            }

            String nombreFotoAnterior = cliente.getFoto();

            uploadService.eliminar(nombreFotoAnterior);

            cliente.setFoto(nombreArchivo);

            clientesService.save(cliente);

            response.put("cliente", cliente);
            response.put("mensaje", "Ha subido correctamente la imagen " + nombreArchivo);

        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/img/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {

        Resource recurso = null;
        try {
            recurso = uploadService.cargar(nombreFoto);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
    }

}