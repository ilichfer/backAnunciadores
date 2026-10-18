# Anunciadores de Cristo — Backend API

API REST para la gestión eclesiástica digital de la iglesia **Anunciadores de Cristo (ADC)**. Proporciona servicios de autenticación, programación de servicios, gestión de ministerios, TCD (Tiempo Con Dios), notificaciones, dashboard, mensajes de contacto e imágenes mensuales.

## Stack

| Tecnología | Versión |
|---|---|
| Java | 11 |
| Spring Boot | 2.7.18 |
| Spring Security + JWT (JJWT) | 5.7 / 0.11.5 |
| Spring Data JPA / Hibernate | 2.7 |
| MySQL | 8 |
| Maven | Wrapper (`mvnw`) |
| Cloudflare R2 (AWS SDK S3 v2) | 2.20.26 |
| OpenFeign | 2021.0.9 |
| MapStruct | 1.5.5.Final |

## Estructura

```
src/main/java/com/anunciadores/
├── controller/        → Endpoints REST, uno por módulo:
│                        Auth, Persona, Ministerio, Programacion, Tcd, Notificacion,
│                        Contacto, ImagenMensual, Dashboard, Curso, Configuracion
│                        + GlobalExceptionHandler (errores sin detalles internos)
├── security/          → SecurityConfig (reglas por rol), filtro JWT, CurrentUser, RateLimiter
├── service/           → Lógica de negocio (+ interfaces/)
├── repository/        → Acceso a datos JPA
├── model/             → Entidades JPA
├── dto/               → Objetos de transferencia
├── mapper/            → MapStruct mappers
├── client/            → Clientes Feign (Telegram, Biblia) y R2
├── util/              → Fechas, validación de imágenes, limpieza del driver JDBC
└── scheduler/         → Tareas programadas (notificaciones)
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

## Configuración y secretos

`src/main/resources/application.properties` **no contiene secretos**. Las contraseñas, tokens y claves
se leen de un archivo aparte que nunca se sube a git:

| Dónde | Archivo |
|---|---|
| Local | `application-secrets.properties` en la raíz del proyecto |
| VPS | `/etc/anunciadores/application-secrets.properties` |

Copia `application-secrets.example.properties` y completa los valores:

| Propiedad | Descripción |
|---|---|
| `spring.datasource.*` | Conexión MySQL (anunciadb) |
| `cloudflare.r2.*` | Credenciales Cloudflare R2 |
| `security.jwt.secret` | Secreto JWT (mínimo 32 caracteres, p. ej. `openssl rand -base64 48`) |
| `propiedad.telegram.token` | Token bot Telegram |
| `token` | Token API de la Biblia |

En el VPS el archivo debe poder leerlo el usuario `tomcat`:

```bash
sudo mkdir -p /etc/anunciadores
sudo cp application-secrets.properties /etc/anunciadores/
sudo chown root:tomcat /etc/anunciadores/application-secrets.properties
sudo chmod 640 /etc/anunciadores/application-secrets.properties
```

## Principales endpoints

Todos bajo `/api`, sin prefijo de versión.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/auth/loginReact` | Inicio de sesión (cédula + contraseña, BCrypt) |
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

Se empaqueta como WAR para el Tomcat 9 del VPS (contexto `/anunciadores-0.0.1-SNAPSHOT`, Nginx
redirige `https://anunciaig.com/api/` a ese contexto):

```bash
./mvnw clean package            # corre los tests; agrega -DskipTests para omitirlos
scp target/anunciadores-0.0.1-SNAPSHOT.war root@VPS:/opt/tomcat/webapps/
# en el VPS:
chown tomcat:tomcat /opt/tomcat/webapps/anunciadores-0.0.1-SNAPSHOT.war
systemctl restart tomcat        # nunca arrancar Tomcat con startup.sh como root
```

## Base de datos

- En producción `ddl-auto=none`: Hibernate no modifica tablas. Si cambias una entidad, aplica el SQL a mano.
- En local `application-secrets.properties` puede usar `ddl-auto=update`.
- Base: `anunciadb` en MySQL localhost:3306. Scripts en `src/main/resources/querys/`.

## Seguridad

- Todas las rutas `/api/**` exigen token JWT (`Authorization: Bearer ...`), excepto:
  login, cambio de contraseña por cédula, registro público, formulario de contacto e imagen del mes.
- Las acciones de administración (roles, usuarios, ministerios, programación, cursos, reportes,
  mensajes de contacto, configuración) exigen rol `ADMIN`.
- Los datos personales (notificaciones, TCD, programación, notas, dashboard) solo los ve su dueño o un ADMIN.
- El registro público siempre crea rol `USUARIO`; solo un ADMIN puede elegir otro rol.
- Límite de intentos por IP en login, registro y contacto.
- Las imágenes se validan por su contenido real (JPG, PNG, WEBP, GIF).
- Contraseñas en BCrypt; las antiguas en MD5 se migran solas en el siguiente inicio de sesión.
- Las respuestas nunca incluyen la contraseña ni detalles internos de los errores.

## Funcionalidades pastorales (octubre 2026)

Migración SQL: `src/main/resources/querys/migraciones/2026-10-18_funcionalidades_pastorales.sql` (ejecutar una vez).

| Funcionalidad | Endpoints |
|---|---|
| Bitácora de cambios (ADMIN) | `GET /api/auditoria`, `GET /api/auditoria/acciones` |
| Confirmar turno / avisar que no puede | `PUT /api/servicios/{id}/confirmacion` `{estado: CONFIRMADO\|NO_PUEDE, motivo}` |
| Calendario y Google Calendar | `GET /api/calendario`, `GET /api/calendario/suscripcion`, `GET /api/calendario/ics/{token}.ics` (público con token) |
| Racha e insignias de TCD | `GET /api/tcd/racha/{idPersona}` |
| Notificaciones push | `GET /api/push/configuracion`, `POST/DELETE /api/push/suscripcion` |
| Panel pastoral y resumen semanal (ADMIN) | `GET /api/panel-pastor`, `GET /api/panel-pastor/resumenes`, `POST /api/panel-pastor/resumenes/generar` |
| WhatsApp (ADMIN) | `GET /api/whatsapp/estado`, `POST /api/whatsapp/conectar`, `PUT /api/whatsapp/preferencias`, `POST /api/whatsapp/prueba` |

Tareas automáticas (hora de Bogotá): cumpleaños 8:00, recordatorio de TCD 19:00, resumen semanal lunes 7:00.
Siempre crean la notificación en la app y el push; WhatsApp solo si el admin activó ese envío en Panel pastoral.

Configuración nueva en `application-secrets.properties`:

| Propiedad | Descripción |
|---|---|
| `push.vapid.public-key` / `push.vapid.private-key` | Claves VAPID (generar una vez; si cambian, los dispositivos deben volver a suscribirse) |
| `whatsapp.api-url` / `whatsapp.api-key` | Evolution API del VPS (`http://127.0.0.1:8085` y su `AUTHENTICATION_API_KEY`) |

