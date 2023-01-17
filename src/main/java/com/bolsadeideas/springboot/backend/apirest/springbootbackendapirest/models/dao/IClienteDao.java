package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

    
}
