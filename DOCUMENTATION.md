# Decisiones de Diseño - BiblioTech

## 1. Tecnologías y Herramientas
- **Java 17** con Maven como gestor de dependencias
- **IntelliJ IDEA** como IDE
- **Git + GitHub** para control de versiones siguiendo el flujo de trabajo con Issues, ramas y Pull Requests

---

## 2. Arquitectura en Capas

Se implementó una arquitectura de 4 capas con separación clara de responsabilidades:

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| Modelo | `model` | Entidades del dominio (Libro, Ebook, Socio, Prestamo) |
| Repositorio | `repository` | Acceso y almacenamiento de datos en memoria |
| Servicio | `service` | Lógica de negocio y validaciones |
| Entrada | `Main.java` | Interfaz de línea de comandos (CLI) |
| Persistencia | `persistence` | Lectura y escritura de datos en archivos CSV |
| Excepciones | `exception` | Jerarquía de errores del dominio |

---

## 3. Decisiones sobre el Modelo de Dominio

### Records para entidades inmutables
Se utilizaron **Java Records** para `Libro`, `Ebook` y `Prestamo` ya que son entidades que no cambian su estado una vez creadas. Esto garantiza inmutabilidad y reduce el código boilerplate.

```java
public record Libro(String isbn, String titulo, String autor, int anio, CategoriaLibro categoria, boolean disponible) implements Recurso {}
```

### Interface Recurso
Se definió la interface `Recurso` para unificar `Libro` y `Ebook` bajo un mismo tipo, permitiendo polimorfismo en el sistema de préstamos. Esto facilita extender el sistema con nuevos tipos de recursos en el futuro.

### Clase abstracta Socio
`Socio` se implementó como clase abstracta con dos subclases concretas:
- `Estudiante`: límite de 3 préstamos simultáneos
- `Docente`: límite de 5 préstamos simultáneos

Cada subclase sobreescribe `getLimiteLibros()` y `getTipo()`, aplicando el principio **Open/Closed** de SOLID.

### Enums
Se utilizaron enums para `CategoriaLibro` (CIENCIA, HISTORIA, LITERATURA, TECNOLOGIA, ARTE, OTRO) y `TipoSocio` (ESTUDIANTE, DOCENTE) para evitar el uso de Strings arbitrarios y garantizar valores válidos.

---

## 4. Decisiones sobre Repositorios

### Interface genérica Repository<T, ID>
Se definió una interface genérica base con operaciones comunes:
```java
public interface Repository<T, ID> {
    void guardar(T entidad);
    Optional<T> buscarPorId(ID id);
    List<T> buscarTodos();
}
```

Cada repositorio específico extiende esta interface agregando métodos propios:
- `LibroRepository`: búsqueda por título, autor y categoría
- `SocioRepository`: verificación de DNI y email duplicados
- `PrestamoRepository`: búsqueda por socio

### Implementaciones in-memory
Las implementaciones usan `HashMap` como almacenamiento, lo que permite búsqueda O(1) por ID. Se eligió esta estructura por simplicidad y eficiencia para el alcance del proyecto.

---

## 5. Decisiones sobre Servicios

### Inyección de dependencias por constructor
Los servicios reciben sus repositorios por constructor usando interfaces, no implementaciones concretas. Esto aplica el principio **Dependency Inversion** de SOLID y facilita el testing futuro.

```java
public PrestamoServiceImpl(LibroRepository libroRepo, SocioRepository socioRepo, PrestamoRepositoryImpl prestamoRepo) { ... }
```

### Validaciones en SocioServiceImpl
- DNI único: se verifica antes de registrar un socio
- Formato de email: se valida con expresión regular
- Se lanzan excepciones específicas (`DniDuplicadoException`, `EmailInvalidoException`) en lugar de excepciones genéricas

### Lógica de préstamo
Antes de registrar un préstamo se verifican:
1. Que el libro exista
2. Que el libro esté disponible
3. Que el socio exista
4. Que el socio no haya alcanzado su límite de préstamos
5. Que el socio no esté sancionado

---

## 6. Manejo de Errores

Se definió una jerarquía de excepciones bajo `BibliotecaException`:

├── LibroNoDisponibleException
├── LibroNoEncontradoException
├── SocioNoEncontradoException
├── SocioSinCupoException
├── DniDuplicadoException
└── EmailInvalidoException

Esto permite capturar errores específicos del dominio y mostrar mensajes claros al usuario en el CLI, evitando el uso de `RuntimeException` genéricas o `printStackTrace`.

---

## 7. Uso de Optional

Se evitó el uso de `null` en todos los retornos de búsqueda utilizando `Optional<T>`:

```java
Optional<Libro> buscarPorId(String isbn);
```

En los servicios se usa `orElseThrow()` para lanzar excepciones específicas cuando no se encuentra un recurso.

---

## 8. Interfaz CLI

Se implementó un menú interactivo en `Main.java` con las siguientes opciones:
1. Registrar libro
2. Registrar socio
3. Realizar préstamo
4. Devolver libro
5. Listar libros
6. Listar socios
7. Listar préstamos
8. Guardar datos
0. Salir

La inyección de dependencias se realiza en el `main`, instanciando repositorios y pasándolos a los servicios por constructor.

---

## 9. Bonus: Sistema de Sanciones

Al devolver un libro con retraso, el sistema calcula automáticamente los días de retraso y bloquea al socio por esa cantidad de días. La sanción se almacena como `fechaFinSancion` en el objeto `Socio`. Al intentar realizar un préstamo, se verifica si el socio está sancionado antes de permitirlo.

---

## 10. Bonus: Persistencia CSV

Se implementó un sistema de persistencia en archivos CSV con dos clases:
- `CsvHelper`: utilidad para leer y escribir archivos CSV
- `PersistenciaService`: coordina la serialización/deserialización de libros y socios

Los archivos se guardan en la carpeta `data/` que se crea automáticamente en runtime. Esta carpeta está excluida del repositorio mediante `.gitignore` ya que contiene datos de ejecución, no código fuente.

Al iniciar el sistema se cargan automáticamente los datos guardados, y el usuario puede guardarlos manualmente con la opción 8 del menú.