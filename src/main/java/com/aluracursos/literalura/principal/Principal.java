package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import jakarta.transaction.Transactional;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.*;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos convertir = new ConvierteDatos();
    private static String API_BASE = "https://gutendex.com/books/?search=";
    //var json = consumoApi.obtenerDatos("https://gutendex.com/books/?ids=1513");
    //var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=Romeo%20and%20Juliet");
    private List<Libro> datosLibro = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Optional<Libro> libroBuscado;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    
                    |***************************************************|
                    |*****       BIENVENIDO A BIBLIOTECA GUTENDEX       ******|                        
                    |***************************************************|
                    
                    1 - Agregar Libro por Nombre
                    2 - Mostrar libros buscados
                    3 - Buscar libro por Nombre
                    4 - Buscar todos los Autores de libros buscados
                    5 - Buscar Autores por año
                    6 - Buscar Libros por Idioma
                    7 - Top 10 Libros más Descargados
                    8 - Buscar Autor por Nombre
                   
                    
               
                    0 - Salir
                    
                    |***************************************************|
                    |*****            INGRESE UNA OPCIÓN          ******|
                    |***************************************************|
                    """;

            try {
                System.out.println(menu);
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {

                System.out.println("|****************************************|");
                System.out.println("|  Por favor, ingrese un número válido.  |");
                System.out.println("|****************************************|\n");
                sc.nextLine();
                continue;
            }



            switch (opcion){
                case 1:
                    buscarLibroEnLaWeb();
                    break;
                case 2:
                    librosBuscados();
                    break;
                case 3:
                    buscarLibroPorNombre();
                    break;
                case 4:
                    BuscarAutores();
                    break;
                case 5:
                    buscarAutoresPorAnio();
                    break;
                case 6:
                    buscarLibrosPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
                case 0:
                    opcion = 0;
                    System.out.println("|********************************|");
                    System.out.println("|    Finalizó la aplicación.  |");
                    System.out.println("|********************************|\n");
                    break;
                default:
                    System.out.println("|*********************|");
                    System.out.println("|  Opción Incorrecta. |");
                    System.out.println("|*********************|\n");
                    System.out.println("Intente con una nueva Opción");
                    muestraElMenu();
                    break;
            }
        }
    }

    private DatosLibro getDatosLibro(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = sc.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "%20"));
        //System.out.println("JSON INICIAL: " + json);
        System.out.println(json);
       DatosLibro datos = convertir.convertirDatosJsonAJava(json, DatosLibro.class);

       return datos;
    }


    private void buscarLibroEnLaWeb() {
        DatosLibro datos = getDatosLibro();

        Libro libro = new Libro(datos);
        libroRepository.save(libro);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    @Transactional
    private void librosBuscados(){
        //datosLibro.forEach(System.out::println);
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros encontrados en la base de datos:");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarLibroPorNombre() {
        System.out.println("Ingrese el título del libro que quiere buscar: ");
        var titulo = sc.nextLine();
        libroBuscado = Optional.ofNullable(libroRepository.findByTituloContainsIgnoreCase(titulo));
        if (libroBuscado.isPresent()) {
            System.out.println("El libro buscado fue: " + libroBuscado.get());
        } else {
            System.out.println("El libro con el tItulo '" + titulo + "' no se encontró.");
        }
    }

    private  void BuscarAutores(){
        //LISTAR AUTORES DE LIBROS BUSCADOS
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos. \n");
        } else {
            System.out.println("Libros encontrados en la base de datos: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                // add() retorna true si el nombre no estaba presente y se añade correctamente
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }

    private void  buscarLibrosPorIdioma(){
        System.out.println("Ingrese Idioma en el que quiere buscar: \n");
        System.out.println("|***********************************|");
        System.out.println("|  Opción - es : Libros en español. |");
        System.out.println("|  Opción - en : Libros en ingles.  |");
        System.out.println("|***********************************|\n");

        var idioma = sc.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros segun idioma encontrados en la base de datos:");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }

    }

    private void buscarAutoresPorAnio() {
//        //BUSCAR AUTORES POR ANIO

        System.out.println("Indica el año para consultar que autores estan vivos: \n");
        var anioBuscado = sc.nextInt();
        sc.nextLine();

        List<Autor> autoresVivos = autorRepository.findByFechaDeNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores que estuvieran vivos en el año " + anioBuscado + ".");
        } else {
            System.out.println("Los autores que estaban vivos en el año " + anioBuscado + " son:");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor : autoresVivos) {
                if (autor.getFechaDeNacimiento() != null && autor.getFechaFallecimiento() != null) {
                    if (autor.getFechaDeNacimiento() <= anioBuscado && autor.getFechaFallecimiento() >= anioBuscado) {
                        if (autoresUnicos.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }

    private void top10LibrosMasDescargados(){
        List<Libro> top10Libros = libroRepository.findTop10ByTituloByCantidadDescargas();
        if (!top10Libros.isEmpty()){
            int index = 1;
            for (Libro libro: top10Libros){
                System.out.printf("Libro %d: %s Autor: %s Descargas: %d\n",
                        index, libro.getTitulo(), libro.getAutor().getNombre(), libro.getCantidadDescargas());
                index++;
            }
            //top10Libros.forEach(l-> System.out.printf("Libro: %s Autor: %s Descargas: %s\n",
            //       l.getTitulo(), l.getAutores().getNombre(), l.getCantidadDescargas()));
        }
    }


    private void buscarAutorPorNombre() {
        System.out.println("Ingrese nombre del escritor que quiere buscar: ");
        var escritor = sc.nextLine();
        Optional<Autor> escritorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(escritor);
        if (escritorBuscado != null) {
            System.out.println("\nEl escritor buscado fue: " + escritorBuscado.get().getNombre());

        } else {
            System.out.println("\nEl escritor con el titulo '" + escritor + "' no se encontró.");
        }
    }
}
