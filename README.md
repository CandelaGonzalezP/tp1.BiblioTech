# Trabajo Práctico 1: Sistema de Gestión de Biblioteca "BiblioTech"
## Curso: Programación en Java - 4to Año Ingeniería en Informática

---

### Datos del Alumno

| Campo | Respuesta |
| :--- | :--- |
| **Nombre** | Candela |
| **Apellido** |Gonzalez Privitera |
| **Legajo** | 63007 |

> **Fecha de entrega:** 28 de Abril de 2026

---

### 1. Introducción y Contexto
La biblioteca de la Universidad ha decidido modernizar su sistema de gestión manual. Como ingenieros en formación, se le ha encomendado el diseño y desarrollo del núcleo (core) del sistema **BiblioTech**.

Este proyecto no solo busca evaluar sus conocimientos técnicos en Java, sino también su capacidad para diseñar sistemas robustos, mantenibles y profesionalmente estructurados bajo estándares de la industria actual.

---

### 2. Objetivos de Aprendizaje
Al finalizar este trabajo práctico, el alumno será capaz de:
- Aplicar los principios **SOLID** en un diseño de software real.
- Utilizar características modernas de **Java** (Records, Optional).
- Implementar arquitecturas desacopladas mediante **Interfaces** e **Inyección de Dependencias**.
- Gestionar errores de forma profesional mediante una jerarquía de **Excepciones Personalizadas**.

---

### 3. El Desafío: Requerimientos Funcionales
El sistema debe gestionar los siguientes procesos clave:

#### A. Gestión de Recursos (Libros y más)
- Registro de libros con: ISBN, título, autor, año y categoría.
- El sistema debe soportar diferentes tipos de ejemplares (Libros Físicos y E-books).
- Búsqueda avanzada por múltiples criterios (título, autor o categoría).

#### B. Gestión de Usuarios (Socios)
- Registro de socios con validación de datos (DNI único, email con formato correcto).
- Categorización de socios: *Estudiante* (máximo 3 libros) y *Docente* (máximo 5 libros).

#### C. Ciclo de Préstamo
- Registro de préstamos verificando disponibilidad y límites del socio.
- Gestión de devoluciones con cálculo automático de días de retraso.
- Registro histórico de transacciones.

---

### 4. Requisitos Técnicos Obligatorios

#### Modern Java
- **Records**: Utilizar `record` para entidades que actúen como DTOs o datos inmutables.
- **Interfaces / Herencia**: Definir la jerarquía de tipos de socios o libros utilizando polimorfismo.
- **Optional**: Evitar el uso de `null` en los retornos de búsqueda.

#### Arquitectura y Diseño
- **Capas**: Clara separación entre `model`, `repository`, `service` y el punto de entrada `Main (CLI)`.
- **Inyección de Dependencias**: Los servicios deben recibir sus repositorios por constructor (usando interfaces).
- **SOLID**: Se evaluará estrictamente la aplicación de cada principio.

#### Manejo de Errores
- Uso de `try-catch` y lanzamiento de excepciones específicas (ej: `LibroNoDisponibleException`).

---

### 5. Estructura Sugerida del Proyecto
```
src/
├── main/java/com/bibliotech/
│   ├── model/          # Records, Clases, Enums
│   ├── repository/     # Interfaces y sus implementaciones (in-memory)
│   ├── service/        # Lógica de negocio (Interfaces + Impl)
│   ├── exception/      # Jerarquía de excepciones de negocio
│   └── Main.java       # Orquestador del sistema (CLI)
```

---

### 6. Metodología de Trabajo y Entrega (Flujo Obligatorio)

Para fomentar hábitos profesionales de desarrollo, es **obligatorio** seguir el flujo de trabajo de GitHub para cada requerimiento:

1.  **Issues**: Cada funcionalidad o corrección (ej: "Alta de Libros", "Lógica de Préstamo") debe estar registrada como un Issue en GitHub antes de comenzar a programar.
2.  **Ramas**: No se debe trabajar directamente sobre la rama `main`. Cada Issue debe resolverse en una rama específica (`feature/nombre-funcionalidad`).
3.  **Commit Messages**: Seguir la convención de *Conventional Commits* (ej: `feat: add loan validation logic`).
4.  **Pull Requests**: Para integrar el código de una rama a `main`, se debe abrir un PR vinculándolo al Issue correspondiente. El alumno debe realizar la "auto-revisión" del código en la interfaz de GitHub antes de fusionarlo.

#### Entregables
- Repositorio **individual** de **GitHub** con el historial completo de Issues y Pull Requests.
- Documentación breve de las decisiones de diseño tomadas.

---

### 7. Rúbrica de Evaluación

| Criterio | Nivel Experto (10) | Nivel Inicial (4-6) |
| :--- | :--- | :--- |
| **Principios SOLID** | Aplicación impecable, interfaces bien segregadas. | Clases con múltiples responsabilidades. |
| **Java Moderno** | Uso fluido de Records y Optional. | Uso de Java estilo "legacy" (clases mutables, nulls). |
| **Arquitectura** | Desacoplamiento total mediante interfaces. | Dependencias fuertes entre clases concretas. |
| **Manejo de Errores** | Jerarquía de excepciones clara y controlada. | Uso de `RuntimeException` genéricas o `printStacktrace`. |

---

### 8. Bonus (Opcional)
- Implementar un sistema de **Sanciones**: Si un socio devuelve tarde, se le bloquea por N días.
- **Persistencia**: Cargar/Guardar los datos en archivos JSON o CSV.

---

### 9. Guía de Inicio (Skeletons Sugeridos)

Para ayudar en el arranque, se sugieren los siguientes patrones de diseño:

#### A. Modelo con Records e Interfaces
```java
// src/com/bibliotech/model/Recurso.java
public interface Recurso {
    String isbn();
    String titulo();
}

// src/com/bibliotech/model/Libro.java
public record Libro(String isbn, String titulo, String autor, int paginas) implements Recurso {}
```

#### B. Repositorios Genéricos (SOLID - Interface Segregation)
```java
// src/com/bibliotech/repository/Repository.java
public interface Repository<T, ID> {
    void guardar(T entidad);
    Optional<T> buscarPorId(ID id);
    List<T> buscarTodos();
}
```

#### C. Lógica de Negocio (SOLID - Dependency Inversion)
```java
// src/com/bibliotech/service/PrestamoService.java
public class PrestamoService {
    private final Repository<Libro, String> libroRepo;
    private final Repository<Socio, Integer> socioRepo;

    // Inyección por constructor
    public PrestamoService(Repository<Libro, String> libroRepo, Repository<Socio, Integer> socioRepo) {
        this.libroRepo = libroRepo;
        this.socioRepo = socioRepo;
    }

    public void realizarPrestamo(String isbn, int socioId) throws BibliotecaException {
        // Implementar validaciones y lógica
    }
}
```
