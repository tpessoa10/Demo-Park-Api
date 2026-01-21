package com.mballen.demo_park_api.service;

import com.mballen.demo_park_api.entity.Usuario;
import com.mballen.demo_park_api.exception.EntityNotFoundException;
import com.mballen.demo_park_api.exception.PasswordInvalidException;
import com.mballen.demo_park_api.exception.UserNameUniqueVioletionException;
import com.mballen.demo_park_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) throws UserNameUniqueVioletionException {
        try{
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex){
            throw new UserNameUniqueVioletionException(String.format("Username {%s} ja existe", usuario.getUsername()));
        }
    }

    @Transactional
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado!"))
        );
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if (!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException("Nova senha não é igual à confirmação de senha");
        }

        Usuario usuario = buscarPorId(id);

        if (!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
            throw new PasswordInvalidException("Sua senha não confere");
        }

        usuario.setPassword(passwordEncoder.encode(novaSenha));
        return usuario;
    }

    @Transactional
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário com %s não encontrado!", username))
        );
    }

    @Transactional
    public Usuario.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
