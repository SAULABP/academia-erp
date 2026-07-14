package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.CategoriaEgreso;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class EgresoPorCategoriaDTO {
    private CategoriaEgreso categoria;
    private BigDecimal total;

    public EgresoPorCategoriaDTO(CategoriaEgreso categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }
}