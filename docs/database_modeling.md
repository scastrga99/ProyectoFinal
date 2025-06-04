# Modelado de Base de Datos

## 1. Introducción
Este documento describe la estructura de la base de datos relacional utilizada por la aplicación, detallando las tablas, sus campos principales, claves primarias y foráneas, así como las relaciones entre ellas.

---

## 2. Tablas Principales

### 2.1 usuarios
- **id_usuario** (PK)
- nombre
- apellidos
- correo
- password
- fecha_alta
- fecha_baja (opcional)
- rol
- id_departamento (FK)

### 2.2 departamentos
- **id_departamento** (PK)
- nombre

### 2.3 materiales
- **id_material** (PK)
- nombre
- num_serie (único)
- marca (opcional)
- descripcion (opcional)
- estado
- fecha_alta
- fecha_baja (opcional)
- imagen (opcional)
- id_departamento (FK)

### 2.4 libros
- **id_libro** (PK)
- isbn
- titulo
- autor
- editorial
- estado
- foto (opcional)

### 2.5 prestamos
- **id_prestamo** (PK)
- id_usuario_realiza (FK)
- id_usuario_recibe (FK)
- id_libro (FK)
- fecha_prestamo
- fecha_plazo
- fecha_devolucion (opcional)
- devuelto

### 2.6 registros
- **id_registro** (PK)
- entidad
- entidad_id
- operacion
- detalles (opcional)
- fecha

---

## 3. Relaciones

- Un departamento puede tener muchos usuarios y materiales.
- Un usuario pertenece a un departamento.
- Un material pertenece a un departamento.
- Un libro puede estar asociado a varios préstamos.
- Un préstamo está relacionado con dos usuarios (quien realiza y quien recibe) y un libro.
- Los registros pueden estar asociados a cualquier entidad del sistema.

## 5. Conclusión
La estructura propuesta permite gestionar de forma eficiente la información y las relaciones entre los distintos elementos del sistema, garantizando la integridad referencial y facilitando futuras ampliaciones.
