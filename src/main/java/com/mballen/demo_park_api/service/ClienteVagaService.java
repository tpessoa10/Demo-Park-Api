package com.mballen.demo_park_api.service;

import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.exception.EntityNotFoundException;
import com.mballen.demo_park_api.repository.ClienteVagaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteVagaService {

    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional
    public ClienteVaga buscarPorRecibo(String recibo) {
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Recibo %s não encontrado no sistema ou checkout já realizado")
                )
        );
    }
}
