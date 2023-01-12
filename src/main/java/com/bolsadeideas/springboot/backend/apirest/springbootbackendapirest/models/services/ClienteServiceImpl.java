package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.services;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.dao.IClienteDao;
import com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClienteServiceImpl implements IClienteService {

    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);

    }

}
