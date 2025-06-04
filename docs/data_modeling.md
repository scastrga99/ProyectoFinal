# Modelado de Datos

## 1. Introducción
Este documento describe el modelo de datos de la aplicación, detallando las entidades principales, sus atributos y relaciones, así como un diagrama de clases que ilustra la estructura general del sistema.

---

## 2. Entidades y Relaciones

### 2.1 Usuario
**Descripción:**  
Entidad que representa a cada usuario registrado en el sistema.

**Atributos:**
- idUsuario: Identificador único.
- nombre: Nombre del usuario.
- apellidos: Apellidos del usuario.
- correo: Correo electrónico.
- password: Contraseña cifrada.
- fechaAlta: Fecha de registro.
- fechaBaja: Fecha de baja (opcional).
- rol: Rol asignado (`ROLE_USER`, `ROLE_PROFESOR`, `ROLE_ADMIN`).
- departamento: Referencia al departamento al que pertenece.

**Relaciones:**
- Muchos usuarios pertenecen a un departamento.

---

### 2.2 Departamento
**Descripción:**  
Define los departamentos a los que pueden pertenecer usuarios y materiales.

**Atributos:**
- idDepartamento: Identificador único.
- nombre: Nombre del departamento.

**Relaciones:**
- Un departamento tiene muchos usuarios.
- Un departamento tiene muchos materiales.

---

### 2.3 Material
**Descripción:**  
Entidad para la gestión de materiales.

**Atributos:**
- idMaterial: Identificador único.
- nombre: Nombre del material.
- numSerie: Número de serie único.
- marca: Marca (opcional).
- descripcion: Descripción (opcional).
- estado: Estado actual (ej. "Nuevo", "Usado").
- fechaAlta: Fecha de alta.
- fechaBaja: Fecha de baja (opcional).
- imagen: Imagen asociada (opcional).
- departamento: Referencia al departamento.

**Relaciones:**
- Muchos materiales pertenecen a un departamento.

---

### 2.4 Libro
**Descripción:**  
Entidad para la gestión de libros.

**Atributos:**
- idLibro: Identificador único.
- isbn: Código ISBN.
- titulo: Título del libro.
- autor: Autor.
- editorial: Editorial.
- estado: Estado (ej. "Libre", "Prestado").
- foto: Imagen asociada (opcional).

**Relaciones:**
- Un libro puede estar asociado a varios préstamos.

---

### 2.5 Prestamo
**Descripción:**  
Registra los préstamos de libros.

**Atributos:**
- idPrestamo: Identificador único.
- usuarioRealiza: Usuario que realiza el préstamo.
- usuarioRecibe: Usuario que recibe el préstamo.
- libro: Libro prestado.
- fechaPrestamo: Fecha del préstamo.
- fechaPlazo: Fecha límite de devolución.
- fechaDevolucion: Fecha de devolución (opcional).
- devuelto: Indicador de devolución.

**Relaciones:**
- Un préstamo está asociado a un usuario que realiza, uno que recibe y un libro.

---

### 2.6 Registro
**Descripción:**  
Historial de operaciones realizadas en el sistema.

**Atributos:**
- idRegistro: Identificador único.
- entidad: Nombre de la entidad afectada.
- entidadId: Identificador de la entidad.
- operacion: Tipo de operación ("CREAR", "ACTUALIZAR", "ELIMINAR").
- detalles: Información adicional (opcional).
- fecha: Fecha de la operación.

## 4. Conclusión
El modelo de datos propuesto cubre las necesidades de gestión de usuarios, departamentos, materiales, libros, préstamos y registros, asegurando integridad y trazabilidad en el sistema.
