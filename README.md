# Ciudades API

API REST desarrollada con Spring Boot que permite gestionar ciudades e integrarlas con datos meteorológicos en tiempo real a través de OpenWeatherMap.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL (Supabase)
- OpenWeatherMap API
- Lombok
- Maven

## Cómo ejecutar el proyecto

### Requisitos previos

- Java 21 instalado
- IntelliJ IDEA o VS Code
- Cuenta en Supabase
- API Key de OpenWeatherMap

### Pasos

1. Clona el repositorio:

```bash
git clone https://github.com/TU_USUARIO/ciudades-api.git
```

2. Configura el archivo `src/main/resources/application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:postgresql://TU_HOST:6543/postgres
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
openweather.api.key=TU_API_KEY
```

3. Ejecuta el proyecto desde IntelliJ con el botón o con:

```bash
mvn spring-boot:run
```

4. La API estará disponible en `http://localhost:8081`

## Endpoints

### Ciudades (CRUD)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/ciudades` | Obtener todas las ciudades |
| GET | `/api/ciudades/{id}` | Obtener ciudad por ID |
| POST | `/api/ciudades` | Crear nueva ciudad |
| PUT | `/api/ciudades/{id}` | Actualizar ciudad |
| DELETE | `/api/ciudades/{id}` | Eliminar ciudad |

### Tiempo (OpenWeatherMap)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/ciudades/{id}/tiempo` | Ciudad de la BD + tiempo actual |
| GET | `/api/ciudades/tiempo?ciudad=X` | Tiempo de cualquier ciudad |

## Ejemplo de uso

### Crear una ciudad

```json
{
    "nombre": "Sevilla",
    "pais": "España",
    "poblacion": 688711,
    "descripcion": "Capital de Andalucía"
}
```

### Respuesta de ciudad con tiempo

```json
{
    "ciudad": {
        "id": 1,
        "nombre": "Sevilla",
        "pais": "España",
        "poblacion": 688711,
        "descripcion": "Capital de Andalucía"
    },
    "tiempo": {
        "ciudad": "Sevilla",
        "pais": "ES",
        "descripcion": "cielo claro",
        "temperatura": 22.2,
        "sensacionTermica": 22.33,
        "humedad": 71,
        "viento": 2.24
    }
}
```

## Autor

Álvaro Ramírez — DAM 2026