package com.mballen.demo_park_api.web.controller;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.jwt.JwtUserDetails;
import com.mballen.demo_park_api.repository.ClienteRepository;
import com.mballen.demo_park_api.service.ClienteService;
import com.mballen.demo_park_api.service.UsuarioService;
import com.mballen.demo_park_api.web.controller.dto.ClienteCreateDto;
import com.mballen.demo_park_api.web.controller.dto.ClienteResponseDto;
import com.mballen.demo_park_api.web.controller.dto.UsuarioResponseDto;
import com.mballen.demo_park_api.web.controller.dto.mapper.ClienteMapper;
import com.mballen.demo_park_api.web.controller.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Contém todas as operações relativas ao recursos do cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Vincular um usuário a um novo Cliente", description = "Recurso para criar um novo cliente vinculado á um usuário existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "CPF já cadastrado no sistema!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não permite role ADMIN!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails){
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDto(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> getById (@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

}
