# Kokoni - Backend

## Introducción (General)
Bienvenido al backend de **Kokoni**, el núcleo y motor de datos detrás del gestor de lectura de mangas. Esta API RESTful está diseñada para integrar, procesar y almacenar información sobre bibliotecas de usuarios, progreso de lectura, metadatos de medios y configuraciones personalizadas.

El sistema se encarga de funcionar como un "*intermediario inteligente*", consultando APIs de terceros para obtener información detallada de los mangas y, a la vez, guardando de forma segura las listas y seguimientos propios de cada usuario registrado.

---

## Detalles Técnicos (Específico)
### Tecnologías Utilizadas
- **Java 17+ con Spring Boot:** Framework principal del backend, brindando robustez, inyección de dependencias y arquitectura escalable.
- **Spring Security & JWT:** Manejo de autenticación, autorización y protección de endpoints bajo tokens seguros.
- **Spring Data JPA (Hibernate):** Capa de persistencia para mapeo relacional de objetos hacia la base de datos subyacente.
- **Lombok:** Reducción del código boilerplate en entidades y DTOs (Getters, Setters, Constructores).

### Flujo de Datos e Integración Híbrida
El servicio destaca su capacidad para agrupar datos de múltiples fuentes (como la base de datos local y llamadas a APIs de mangas).
1. **Providers y Enrichers:** Funcionalidad encapsulada (`MangaProvider`, `MangaMetadataEnricher`) que busca un identificador externo (`externalId`), obtiene la metadata desde una fuente externa si no la tiene, y la sincroniza con la base de datos.
2. **Personalización del Usuario:** Existe un manejo de entidades únicas como `UserCustomMedia`, que permite sobrescribir los metadatos oficiales de un manga para la vista específica de un usuario (ej. cambio de título o progreso total).

### Arquitectura en Capas
- `controller/`: Expone las rutas RESTful (`MangaController`, `UserController`, `AuthController`).
- `service/`: Contiene la lógica de negocio completa (`MangaServiceImpl`, `UserService`).
- `repository/`: Interfaces puras de acceso a la base de datos (Spring Data).
- `entity/` y `dto/`: Modelado del dominio de negocio y encapsulación del traslado de datos interno y externo (respuestas a la web).

### Guía para Levantar Localmente
1. Clona el repositorio y navega a este directorio en la terminal.
2. Asegúrate de tener JDK y Maven instalados.
3. Configura tus variables de entorno para la base de datos en `application.properties`/`application.yml` (ej. URL, credenciales de PostgreSQL/MySQL).
4. Usa `./mvnw spring-boot:run` para iniciar el servidor Tomcat en el puerto configurado (usualmente 8080).