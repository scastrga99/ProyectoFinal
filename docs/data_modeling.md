# Documentación del Modelado de Datos

## 1. Introducción
El modelado de datos de la aplicación está diseñado para gestionar entidades relacionadas con usuarios, departamentos, materiales, libros, préstamos y registros de operaciones. Este documento describe las entidades principales, sus atributos y relaciones, junto con un diagrama de clases que ilustra su interacción.

---

## 2. Entidades y Relaciones

### 2.1 Usuario
**Descripción:**  
Representa a los usuarios del sistema.

**Atributos principales:**
- Identificador único del usuario.
- Nombre y apellidos.
- Correo electrónico.
- Contraseña encriptada.
- Fecha de alta y fecha de baja (opcional).
- Rol del usuario (`ROLE_USER`, `ROLE_PROFESOR`, `ROLE_ADMIN`).
- Relación con un departamento.

**Relaciones:**
- Muchos a Uno con la entidad `Departamento`.

---

### 2.2 Departamento
**Descripción:**  
Representa los departamentos a los que pertenecen los usuarios y materiales.

**Atributos principales:**
- Identificador único del departamento.
- Nombre del departamento.

**Relaciones:**
- Uno a Muchos con la entidad `Usuario`.
- Uno a Muchos con la entidad `Material`.

---

### 2.3 Material
**Descripción:**  
Representa los materiales gestionados por el sistema.

**Atributos principales:**
- Identificador único del material.
- Nombre y número de serie único.
- Marca y descripción (opcional).
- Estado del material (e.g., "Nuevo", "Usado").
- Fecha de alta y fecha de baja (opcional).
- Imagen asociada al material (opcional).
- Relación con un departamento.

**Relaciones:**
- Muchos a Uno con la entidad `Departamento`.

---

### 2.4 Libro
**Descripción:**  
Representa los libros gestionados por el sistema.

**Atributos principales:**
- Identificador único del libro.
- ISBN, título, autor y editorial.
- Estado del libro (e.g., "Libre", "Prestado").
- Imagen asociada al libro (opcional).

**Relaciones:**
- Uno a Muchos con la entidad `Prestamo`.

---

### 2.5 Prestamo
**Descripción:**  
Representa los préstamos realizados en el sistema.

**Atributos principales:**
- Identificador único del préstamo.
- Relación con el usuario que realiza el préstamo.
- Relación con el usuario que recibe el préstamo.
- Relación con el libro prestado.
- Fecha de préstamo, fecha límite para la devolución y fecha de devolución (opcional).
- Indicador de si el libro fue devuelto.

**Relaciones:**
- Muchos a Uno con la entidad `Usuario` (usuario que realiza y usuario que recibe).
- Muchos a Uno con la entidad `Libro`.

---

### 2.6 Registro
**Descripción:**  
Representa el historial de operaciones realizadas en el sistema.

**Atributos principales:**
- Identificador único del registro.
- Nombre de la entidad afectada (e.g., "Usuario", "Libro").
- Identificador de la entidad afectada.
- Tipo de operación realizada (e.g., "CREAR", "ACTUALIZAR", "ELIMINAR").
- Detalles adicionales sobre la operación (opcional).
- Fecha en que se realizó la operación.

---

## 3. Diagrama de Clases
El siguiente diagrama de clases muestra las entidades del sistema y sus relaciones:

```plaintext
+-------------------+       +-------------------+       +-------------------+
|     Usuario       |       |   Departamento    |       |      Material     |
+-------------------+       +-------------------+       +-------------------+
| - idUsuario       |       | - idDepartamento  |       | - idMaterial      |
| - nombre          |       | - nombre          |       | - nombre          |
| - apellidos       |       +-------------------+       | - numSerie        |
| - correo          |              ^                   | - marca           |
| - password        |              |                   | - descripcion     |
| - fechaAlta       |              |                   | - estado          |
| - fechaBaja       |              |                   | - fechaAlta       |
| - rol             |              |                   | - fechaBaja       |
| - departamento    |              |                   | - departamento    |
+-------------------+              |                   +-------------------+
        ^                          |
        |                          |
        |                          |
+-------------------+       +-------------------+       +-------------------+
|     Prestamo      |       |      Libro        |       |     Registro      |
+-------------------+       +-------------------+       +-------------------+
| - idPrestamo      |       | - idLibro         |       | - idRegistro      |
| - usuarioRealiza  |       | - isbn            |       | - entidad         |
| - usuarioRecibe   |       | - titulo          |       | - entidadId       |
| - libro           |       | - autor           |       | - operacion       |
| - fechaPrestamo   |       | - editorial       |       | - detalles        |
| - fechaPlazo      |       | - estado          |       | - fecha           |
| - fechaDevolucion |       | - foto            |       +-------------------+
| - devuelto        |       +-------------------+
+-------------------+
```

---

## 4. Conclusión
El modelado de datos de la aplicación refleja las entidades principales del sistema y sus relaciones. Este diseño garantiza la integridad de los datos y facilita la implementación de las funcionalidades requeridas en el sistema. Además, el diagrama de clases proporciona una visión clara de cómo interactúan las entidades entre sí.
