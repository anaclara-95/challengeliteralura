package com.aluracursos.literalura.service;


import com.aluracursos.literalura.dto.LibroDTO;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository repository;

    public List<LibroDTO> obtenerTodosLosLibros(){
        return convierteDatos(repository.findAll());
    }

    public List<LibroDTO>obtenerTop10(){
        return convierteDatos(repository.findTop10ByTituloByCantidadDescargas());
    }

    public List<LibroDTO> convierteDatos (List<Libro> libro){
        return libro.stream().map(l-> new LibroDTO(l.getLibroId(),l.getId(),l.getTitulo(),l.getAutor(),
                        l.getGenero(),l.getIdioma(),l.getImagen(),l.getCantidadDescargas()))
                .collect(Collectors.toList());
    }

    public LibroDTO obtenerPorId(Long id) {
        Optional<Libro> libro = repository.findById(id);
        if(libro.isPresent()){
            Libro l = libro.get();
            return new LibroDTO(l.getLibroId(),l.getId(),l.getTitulo(),l.getAutor(),
                    l.getGenero(),l.getIdioma(),l.getImagen(),l.getCantidadDescargas());
        }
        return null;
    }

    public List<LibroDTO> obtenerLibrosPorIdioma(String idioma) {

        return convierteDatos(repository.findByIdioma(idioma));
    }



}
