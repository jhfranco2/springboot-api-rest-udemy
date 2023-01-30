package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Region;

public interface IClienteService {

    public List<Cliente> findAll();

    public Cliente findById(Long id);

    public Cliente save(Cliente cliente);

    public void delete(Long id);

    public Page<Cliente> findAll(Pageable pageable);

    public List<Region> findAllRegiones();
}
