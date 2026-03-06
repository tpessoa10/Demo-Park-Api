package com.mballen.demo_park_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes_tem_vagas")
@EntityListeners(AuditingEntityListener.class)
public class ClienteVaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_recibo", nullable = false, unique = true, length = 15)
    private String recibo;

    @Column(name = "placa", nullable = false, length = 8)
    private String placa;
    @Column(name = "marca", nullable = false, length = 45)
    private String marca;
    @Column(name = "modelo", nullable = false, length = 45)
    private String modelo;
    @Column(name = "cor", nullable = false, length = 45)
    private String cor;
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime data_entrada;
    @Column(name = "data_saida", nullable = true)
    private LocalDateTime data_saida;
    @Column(name = "valor", nullable = true, columnDefinition = "decimal(7,2)")
    private BigDecimal valor;
    @Column(name = "desconto", nullable = true, columnDefinition = "decimal(7,2)")
    private BigDecimal desconto;
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    @JoinColumn(name = "id_cliente", nullable = false)
    private Vaga vaga;

    @CreatedDate
    private LocalDateTime dataCriacao;
    @Column(name = "data_modificacao")
    @LastModifiedDate
    private LocalDateTime dataModificacao;
    @Column(name = "criado_por")
    @CreatedBy
    private String criadoPor;
    @Column(name = "modificado_por")
    @LastModifiedBy
    private String modificadoPor;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClienteVaga that = (ClienteVaga) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
