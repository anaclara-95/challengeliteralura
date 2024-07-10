package com.aluracursos.literalura.service;

public interface IConvierteDatos {
    <T> T convertirDatosJsonAJava(String json , Class<T> clase);
}
