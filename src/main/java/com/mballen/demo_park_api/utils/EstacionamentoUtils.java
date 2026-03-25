package com.mballen.demo_park_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;

    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {

        if (entrada == null || saida == null || saida.isBefore(entrada)) {
            throw new IllegalArgumentException("Datas inválidas");
        }

        long minutes = entrada.until(saida, ChronoUnit.MINUTES);

        BigDecimal total;

        if (minutes <= 15) {

            total = BigDecimal.valueOf(PRIMEIROS_15_MINUTES);

        } else if (minutes <= 60) {

            total = BigDecimal.valueOf(PRIMEIROS_60_MINUTES);

        } else {

            long minutosExcedentes = minutes - 60;
            long faixas = (long) Math.ceil(minutosExcedentes / 15.0);

            total = BigDecimal.valueOf(PRIMEIROS_60_MINUTES)
                    .add(BigDecimal.valueOf(faixas)
                            .multiply(BigDecimal.valueOf(ADICIONAL_15_MINUTES)));
        }

        return total.setScale(2, RoundingMode.HALF_EVEN);
    }

    private static final BigDecimal DESCONTO_PERCENTUAL = new BigDecimal("0.30");

    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {

        if (numeroDeVezes > 0 && numeroDeVezes % 10 == 0) {
            // aplica 30% de desconto
            return custo.multiply(DESCONTO_PERCENTUAL)
                    .setScale(2, RoundingMode.HALF_EVEN);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static String gerarRecibo(){
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0, 19);
        return recibo.replace("-","")
                .replace(":","")
                .replace(" T","-");
    }
}
