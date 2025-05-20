# Catálogo de Requisitos Funcionales del Sistema

## 1. Gestión de Usuarios

**RF-01**  
**Descripción breve:** El sistema debe permitir la creación de usuarios con roles específicos.  
**Descripción detallada:** Los usuarios deben poder ser creados con roles como `ROLE_USER`, `ROLE_PROFESOR` o `ROLE_ADMIN`. Estos roles determinarán los permisos y accesos dentro del sistema.

**RF-02**  
**Descripción breve:** El sistema debe permitir la edición de los datos de los usuarios existentes.  
**Descripción detallada:** Los administradores deben poder modificar los datos de los usuarios, como nombre, apellidos, correo, contraseña, rol y departamento asignado.

**RF-03**  
**Descripción breve:** El sistema debe permitir la eliminación de usuarios.  
**Descripción detallada:** Al eliminar un usuario, sus préstamos deben ser reasignados automáticamente a un usuario predeterminado para evitar inconsistencias en el sistema.

**RF-04**  
**Descripción breve:** El sistema debe permitir la recuperación de contraseñas.  
**Descripción detallada:** Los usuarios deben poder solicitar una nueva contraseña mediante su correo electrónico. El sistema generará una contraseña temporal y la enviará al correo registrado.

---

## 2. Gestión de Departamentos

**RF-05**  
**Descripción breve:** El sistema debe permitir la creación de nuevos departamentos.  
**Descripción detallada:** Los administradores deben poder agregar departamentos con un nombre único para organizar los recursos y usuarios.

**RF-06**  
**Descripción breve:** El sistema debe permitir la edición de los datos de los departamentos existentes.  
**Descripción detallada:** Los administradores deben poder modificar el nombre de los departamentos existentes.

**RF-07**  
**Descripción breve:** El sistema debe permitir la eliminación de departamentos.  
**Descripción detallada:** Al eliminar un departamento, los usuarios asociados deben ser reasignados automáticamente al departamento predeterminado "Sin departamento".

---

## 3. Gestión de Materiales

**RF-08**  
**Descripción breve:** El sistema debe permitir la creación de materiales.  
**Descripción detallada:** Los materiales deben incluir atributos como nombre, número de serie, marca, descripción, estado y departamento al que pertenecen.

**RF-09**  
**Descripción breve:** El sistema debe permitir la edición de los datos de los materiales existentes.  
**Descripción detallada:** Los administradores deben poder modificar los atributos de los materiales, como nombre, marca, estado y descripción.

**RF-10**  
**Descripción breve:** El sistema debe permitir la eliminación de materiales.  
**Descripción detallada:** Los materiales que ya no sean necesarios deben poder ser eliminados del sistema.

**RF-11**  
**Descripción breve:** El sistema debe permitir la búsqueda y agrupación de materiales.  
**Descripción detallada:** Los materiales deben poder ser buscados y agrupados por atributos como nombre y marca para facilitar su gestión.

---

## 4. Gestión de Libros

**RF-12**  
**Descripción breve:** El sistema debe permitir la creación de libros.  
**Descripción detallada:** Los libros deben incluir atributos como ISBN, título, autor, editorial, estado y foto opcional.

**RF-13**  
**Descripción breve:** El sistema debe permitir la edición de los datos de los libros existentes.  
**Descripción detallada:** Los administradores deben poder modificar los atributos de los libros, como título, autor, editorial y estado.

**RF-14**  
**Descripción breve:** El sistema debe permitir la eliminación de libros.  
**Descripción detallada:** Al eliminar un libro, los préstamos asociados deben ser reasignados automáticamente a un libro predeterminado llamado "missingLibro".

**RF-15**  
**Descripción breve:** El sistema debe permitir la búsqueda y agrupación de libros.  
**Descripción detallada:** Los libros deben poder ser buscados y agrupados por atributos como título, autor y editorial para facilitar su gestión.

---

## 5. Gestión de Préstamos

**RF-16**  
**Descripción breve:** El sistema debe permitir la creación de préstamos.  
**Descripción detallada:** Los préstamos deben asociar un libro a un usuario receptor, registrando la fecha de préstamo y la fecha límite de devolución.

**RF-17**  
**Descripción breve:** El sistema debe permitir la edición de los datos de los préstamos existentes.  
**Descripción detallada:** Los administradores deben poder modificar las fechas de préstamo, plazo y devolución, así como el estado del préstamo.

**RF-18**  
**Descripción breve:** El sistema debe permitir la devolución de libros.  
**Descripción detallada:** Los libros devueltos deben actualizar su estado a "Libre" y registrar la fecha de devolución en el préstamo correspondiente.

**RF-19**  
**Descripción breve:** El sistema debe permitir el envío de recordatorios para préstamos pendientes.  
**Descripción detallada:** Los usuarios con préstamos pendientes deben recibir recordatorios por correo electrónico antes de la fecha límite de devolución.

---

## 6. Registro de Operaciones

**RF-20**  
**Descripción breve:** El sistema debe registrar todas las operaciones realizadas.  
**Descripción detallada:** Cada operación, como creación, edición o eliminación de entidades, debe ser registrada con detalles como fecha, usuario que realizó la operación y descripción.

**RF-21**  
**Descripción breve:** El sistema debe permitir la visualización del historial de operaciones.  
**Descripción detallada:** Los usuarios con rol `ROLE_ADMIN` deben poder acceder a un historial detallado de todas las operaciones realizadas en el sistema.

---

## 7. Seguridad y Autenticación

**RF-22**  
**Descripción breve:** El sistema debe permitir el inicio de sesión con credenciales de usuario.  
**Descripción detallada:** Los usuarios deben autenticarse mediante su correo electrónico y contraseña para acceder al sistema.

**RF-23**  
**Descripción breve:** El sistema debe restringir el acceso según el rol del usuario.  
**Descripción detallada:** Las funcionalidades del sistema deben estar restringidas según el rol del usuario (`ROLE_USER`, `ROLE_PROFESOR`, `ROLE_ADMIN`).

**RF-24**  
**Descripción breve:** El sistema debe encriptar las contraseñas de los usuarios.  
**Descripción detallada:** Todas las contraseñas deben ser almacenadas en la base de datos de forma encriptada para garantizar la seguridad.

---

## 8. Interfaz de Usuario

**RF-25**  
**Descripción breve:** El sistema debe proporcionar una interfaz armoniosa y fácil de usar.  
**Descripción detallada:** La interfaz debe ser intuitiva, con un diseño moderno y compatible con dispositivos móviles y navegadores modernos.

**RF-26**  
**Descripción breve:** El sistema debe permitir la visualización de datos en diferentes formatos.  
**Descripción detallada:** Los usuarios deben poder alternar entre vistas en formato de tabla y tarjetas para visualizar los datos según su preferencia.

---

## 9. Configuración Inicial

**RF-27**  
**Descripción breve:** El sistema debe crear un departamento predeterminado llamado "Sin departamento".  
**Descripción detallada:** Este departamento debe ser creado automáticamente al iniciar el sistema y utilizado para reasignar usuarios cuando se eliminen otros departamentos.

**RF-28**  
**Descripción breve:** El sistema debe crear un usuario administrador predeterminado.  
**Descripción detallada:** Si no existe un usuario con rol `ROLE_ADMIN`, el sistema debe crear uno automáticamente con credenciales predeterminadas.

**RF-29**  
**Descripción breve:** El sistema debe crear un libro predeterminado llamado "missingLibro".  
**Descripción detallada:** Este libro debe ser creado automáticamente al iniciar el sistema y utilizado para reasignar préstamos cuando se eliminen otros libros.
