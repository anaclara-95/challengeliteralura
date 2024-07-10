package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.print.attribute.standard.Media;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(@JsonAlias("id") Long libroId,
                         @JsonAlias("title") String titulo,
                         @JsonAlias("authors") List<Autor> autor, //@JsonAlias("authors") List<Author> authors,
                         @JsonAlias("subjects")  List<String> genero,
                         @JsonAlias("languages") List<String> idioma,
                         @JsonAlias("formats") String imagen,
                         @JsonAlias("download_count") Long cantidadDescargas) {
}
