package com.aluracursos.literalura.controller;

import com.aluracursos.literalura.dto.LibroDTO;
import com.aluracursos.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping()
    public List<LibroDTO>obtenerTodosLosLibros(){
        return libroService.obtenerTodosLosLibros();
    }

    @GetMapping("/top10")
    public List<LibroDTO> obtenerTop10() {
        return libroService.obtenerTop10();
    }

    @GetMapping("/{id}")
    public LibroDTO obtenerPorId(@PathVariable Long id) {
        return libroService.obtenerPorId(id);

    }
}
