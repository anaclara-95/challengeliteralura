package com.aluracursos.literalura.dto;

import com.aluracursos.literalura.model.Autor;

public record LibroDTO(Long id, Long libroId, String titulo, Autor autor, Genero genero, String idioma,
                       String imagen, Long cantidadDescargas) {
}
