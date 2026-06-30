# Anunciadores de Cristo — Backend API

API REST para la gestión eclesiástica digital de la iglesia **Anunciadores de Cristo (ADC)**. Proporciona servicios de autenticación, programación de servicios, gestión de ministerios, TCD (Tiempo Con Dios), notificaciones, dashboard, mensajes de contacto e imágenes mensuales.

## Stack

| Tecnología | Versión |
|---|---|
| Java | 11 |
| Spring Boot | 2.7.4 |
| Spring Data JPA / Hibernate | 2.7.4 |
| MySQL | (configurado vía `application.properties`) |
| Maven | Wrapper (`mvnw`) |
| Cloudflare R2 (AWS SDK S3 v2) | 2.20.26 |
| OpenFeign | 2021.0.5 |
| MapStruct | 1.5.5.Final |
| JJWT | 0.9.1 |
| Swagger (Springfox) | 2.9.2 |

## Estructura

```
src/main/java/com/anunciadores/
├── controller/        → Endpoints REST (AuthController, PersonaReactController)
├── service/           → Lógica de negocio (24 servicios)
│   └── interfaces/    → Contratos de servicio
├── repository/        → Acceso a datos JPA (30+ repositorios)
├── model/             → Entidades JPA (30+ entidades)
├── dto/               → Objetos de transferencia (79 DTOs)
├── mapper/            → MapStruct mappers (7 mappers)
├── client/            → Clientes Feign (Telegram, Biblia, R2)
├── auth/              → DTOs de autenticación + configuración CORS
├── security/          → Configuración Spring Security
├── util/              → Utilidades (fechas, JWT, encriptación)
└── scheduler/         → Tareas programadas (limpieza TCD, notificaciones)
```

## Requisitos

- Java 11 (JDK)
- MySQL 8+
- Maven (o usar `mvnw`)

## Ejecutar

```bash
cd backAnunciadores
./mvnw spring-boot:run   # o mvnw.cmd en Windows
```

El servidor inicia en `http://localhost:5000`.

## Variables de entorno / Configuración

Editar `src/main/resources/application.properties`:

| Propiedad | Descripción |
|---|---|
| `server.port` | Puerto (5000) |
| `spring.datasource.*` | Conexión MySQL (anunciadb) |
| `cloudflare.r2.*` | Credenciales Cloudflare R2 |
| `security.jwt.secret` | Secreto JWT |
| `propiedad.telegram.token` | Token bot Telegram |

## Principales endpoints

Todos bajo `/api`, sin prefijo de versión.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/auth/loginReact` | Inicio de sesión (documento + password MD5) |
| | | |
| `GET` | `/api/users` | Listar usuarios |
| `GET` | `/api/user` | Usuario actual desde token |
| `GET` | `/api/buscar/{id}` | Persona por ID |
| `GET` | `/api/consutarDoc?doc=` | Persona por documento |
| | | |
| `POST` | `/api/saveService` | Guardar programación de servicios + notificaciones |
| `GET` | `/api/events` | Próximos servicios |
| `GET` | `/api/findprog?date=` | Programación por fecha |
| `GET` | `/api/schedule/persona/{id}` | Programación mensual de una persona |
| | | |
| `GET` | `/api/ministries` | Listar ministerios con posiciones |
| `POST` | `/api/ministeries/addperson` | Agregar persona a ministerio |
| `DELETE` | `/api/ministeries/{idMin}/personas/{idPer}` | Eliminar persona de ministerio |
| | | |
| `POST` | `/api/upload` | Subir imagen TCD (a R2) |
| `GET` | `/api/tdcbyPerson/{id}` | Reporte TCD de persona |
| | | |
| `GET` | `/api/notificaciones/{id}` | Notificaciones de persona |
| `GET` | `/api/notificaciones/{id}/no-leidas` | No leídas |
| `PUT` | `/api/notificaciones/{id}/leida` | Marcar como leída |
| | | |
| `GET` | `/api/imagen-mensual/{tipo}` | Obtener imagen mensual activa |
| `POST` | `/api/imagen-mensual/upload` | Subir imagen mensual (admin, a R2) |
| | | |
| `POST` | `/api/contacto` | Enviar mensaje de contacto (público) |
| `GET` | `/api/contacto` | Listar mensajes (admin) |
| `PUT` | `/api/contacto/{id}/leer` | Marcar mensaje leído (admin) |
| `GET` | `/api/contacto/no-leidos/count` | Contador no leídos (admin) |
| | | |
| `GET` | `/api/dashboard/stats/{id}` | Estadísticas del dashboard |
| | | |
| `GET` | `/api/findBirthday` | Cumpleaños de hoy |
| `GET` | `/api/findBirthDaysMOnth` | Cumpleaños del mes |

## Despliegue

Se empaqueta como WAR para Tomcat:

```bash
./mvnw clean package -DskipTests
```

El WAR se genera en `target/anunciadores-0.0.1-SNAPSHOT.war`. Renombrar a `ROOT.war` y copiar a `webapps/` de Tomcat.

## Base de datos

- Hibernate `ddl-auto=update` crea/actualiza tablas automáticamente.
- Base: `anunciadb` en MySQL localhost:3306.
- 35+ tablas: persona, servicio, ministerios, tdc, notificacion, contacto, imagen_mensual, rol, coordinador, etc.

## Seguridad

- Spring Security configurado con sesión STATELESS, CSRF desactivado, todos los endpoints permitidos.
- La autenticación real se hace manualmente vía JWT en los controladores.
- Clave JWT configurable en `application.properties`.
- Contraseñas hasheadas con MD5 (legado).
