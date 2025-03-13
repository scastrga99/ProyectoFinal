Descripción de la Aplicación Web
La aplicación web es un sistema de gestión de inventarios para un instituto, desarrollado utilizando Spring Boot y Thymeleaf. La aplicación permite a los usuarios gestionar materiales y libros asociados a diferentes departamentos del instituto. A continuación, se describen las funcionalidades principales de la aplicación:

Funcionalidades Principales
Gestión de Materiales del Departamento

Visualización de Materiales: Los usuarios pueden ver una lista de materiales agrupados por nombre y marca en cada departamento. La lista muestra el nombre, la marca y la cantidad disponible de cada material.
Agregar Nuevo Material: Los usuarios pueden agregar un nuevo material a través de un formulario en un modal. El formulario incluye campos para el nombre, número de serie, marca, descripción, estado, fecha de alta, fecha de baja y departamento.
Agregar Múltiples Materiales: Los usuarios pueden agregar múltiples materiales a la vez proporcionando una lista de números de serie separados por comas. El formulario también incluye campos para el nombre, marca, estado y departamento.
Ajustar Materiales: Los usuarios pueden ajustar la cantidad de materiales existentes (aumentar o reducir) a través de un formulario en un modal. El formulario incluye campos para el tipo de ajuste (aumentar o reducir) y la cantidad.
Gestión de Libros Asociados

Visualización de Libros Asociados: Los usuarios pueden ver una lista de libros asociados a diferentes departamentos. La lista muestra el nombre, número de serie, marca, descripción, estado, fecha de alta, fecha de baja y departamento de cada libro.
Agregar Libro por ISBN: Los usuarios pueden agregar un nuevo libro proporcionando el ISBN a través de un formulario en un modal.
Agregar Múltiples Libros por ISBN: Los usuarios pueden agregar múltiples libros a la vez proporcionando una lista de ISBNs separados por comas.
Navegación y Estilo

Navegación: La aplicación incluye un menú de navegación que permite a los usuarios acceder a diferentes secciones de la aplicación, como la gestión de materiales y la gestión de libros.
Estilo: La aplicación utiliza Bootstrap para el diseño y la disposición de los elementos, proporcionando una interfaz de usuario moderna y responsiva.
