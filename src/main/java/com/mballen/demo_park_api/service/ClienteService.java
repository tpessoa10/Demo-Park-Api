package com.mballen.demo_park_api.service;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.exception.CpfUniqueViolationException;
import com.mballen.demo_park_api.exception.EntityNotFoundException;
import com.mballen.demo_park_api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try{
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex){
            throw new CpfUniqueViolationException(String.format("CPF %s não pode ser cadastrado, ja existe no sistema", cliente.getCpf()));
        }
    }

    @Transactional
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cliente não encontrado no sistema!")
                );
    }

}
