package com.mballen.demo_park_api.service;

import com.mballen.demo_park_api.entity.Vaga;
import com.mballen.demo_park_api.exception.CodigoUniqueViolationException;
import com.mballen.demo_park_api.exception.EntityNotFoundException;
import com.mballen.demo_park_api.repository.VagaRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VagaService {
    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar (Vaga vaga){
        try{
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex){
            throw new CodigoUniqueViolationException(String.format("Vaga %s ja existe", vaga.getCodigo()));
        }
    }

    @Transactional
    public Vaga buscarPorCodigo (String codigo){
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga com codigo %s não encontrada", codigo))
        );
    }
}
