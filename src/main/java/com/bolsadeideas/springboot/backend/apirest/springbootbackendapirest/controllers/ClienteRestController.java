package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.dao.IClienteDao;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.services.IClienteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200" })
public class ClienteRestController {

    private IClienteService clientesService;

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clientesService.findAll();
    }

}
