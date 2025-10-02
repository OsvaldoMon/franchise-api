# API de Franquicias - Spring Boot WebFlux

## Descripción

API REST reactiva para el manejo de franquicias, sucursales y productos desarrollada con Spring Boot WebFlux y arquitectura hexagonal. La aplicación permite gestionar una jerarquía de datos donde una franquicia contiene múltiples sucursales, y cada sucursal tiene un catálogo de productos con stock.

## Arquitectura

### Arquitectura Hexagonal (Clean Architecture)

El proyecto implementa arquitectura hexagonal siguiendo los principios de Clean Architecture:

```
src/main/java/com/nequi/franchise/
├── domain/                    # Capa de Dominio
│   ├── model/                # Entidades de dominio
│   │   ├── Franchise.java
│   │   ├── Branch.java
│   │   └── Product.java
│   └── port/                 # Puertos (interfaces)
│       ├── FranchiseRepository.java
│       └── FranchiseService.java
├── application/              # Capa de Aplicación
│   └── usecase/             # Casos de uso
│       └── FranchiseUseCase.java
└── infrastructure/          # Capa de Infraestructura
    ├── persistence/         # Persistencia
    │   ├── document/        # Documentos MongoDB
    │   ├── mapper/          # Mappers de persistencia
    │   └── repository/      # Implementación de repositorios
    └── web/                 # Capa web
        ├── controller/      # Controladores REST
        ├── dto/            # DTOs de transferencia
        └── mapper/         # Mappers web
```

### Tecnologías Utilizadas

- **Spring Boot 3.2.0** con WebFlux para programación reactiva
- **MongoDB** como base de datos NoSQL
- **Spring Data MongoDB Reactive** para acceso reactivo a datos
- **Jakarta Validation** para validación de datos
- **SLF4J + Logback** para logging
- **JUnit 5** para pruebas unitarias
- **JaCoCo** para cobertura de código
- **Docker** para containerización

## Características Principales

### Programación Reactiva
- Uso de `Mono` y `Flux` para operaciones asíncronas
- Operadores reactivos: `map`, `flatMap`, `switchIfEmpty`, `merge`, `zip`
- Manejo correcto de señales: `onNext`, `onError`, `onComplete`

### Funcionalidades Implementadas

#### Gestión de Franquicias
- ✅ Crear franquicia
- ✅ Obtener todas las franquicias
- ✅ Obtener franquicia por ID
- ✅ Actualizar nombre de franquicia
- ✅ Eliminar franquicia

#### Gestión de Sucursales
- ✅ Agregar sucursal a franquicia
- ✅ Actualizar nombre de sucursal

#### Gestión de Productos
- ✅ Agregar producto a sucursal
- ✅ Eliminar producto de sucursal
- ✅ Actualizar stock de producto
- ✅ Actualizar nombre de producto

#### Consultas Especiales
- ✅ Obtener productos con mayor stock por sucursal para una franquicia

## API Endpoints

### Franquicias

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/franchises` | Crear nueva franquicia |
| GET | `/api/v1/franchises` | Obtener todas las franquicias |
| GET | `/api/v1/franchises/{id}` | Obtener franquicia por ID |
| PUT | `/api/v1/franchises/{id}/name` | Actualizar nombre de franquicia |
| DELETE | `/api/v1/franchises/{id}` | Eliminar franquicia |

### Sucursales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/franchises/{franchiseId}/branches` | Agregar sucursal a franquicia |
| PUT | `/api/v1/franchises/{franchiseId}/branches/{branchId}/name` | Actualizar nombre de sucursal |

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/franchises/{franchiseId}/branches/{branchId}/products` | Agregar producto a sucursal |
| DELETE | `/api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}` | Eliminar producto de sucursal |
| PUT | `/api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` | Actualizar stock de producto |
| PUT | `/api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name` | Actualizar nombre de producto |

### Consultas Especiales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/franchises/{franchiseId}/products/max-stock` | Obtener productos con mayor stock por sucursal |

### Monitoreo

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/health` | Estado de salud de la aplicación |

## Modelo de Datos

### Franquicia (Franchise)
```json
{
  "id": "string",
  "name": "string",
  "branches": [
    {
      "id": "string",
      "name": "string",
      "products": [
        {
          "id": "string",
          "name": "string",
          "stock": "number"
        }
      ]
    }
  ]
}
```

### Producto con Sucursal (ProductWithBranch)
```json
{
  "product": {
    "id": "string",
    "name": "string",
    "stock": "number"
  },
  "branchName": "string"
}
```

## Instalación y Despliegue

### Prerrequisitos

- Java 17 o superior
- Maven 3.6 o superior
- Docker y Docker Compose (opcional)
- MongoDB (si no se usa Docker)

### Despliegue con Docker

1. **Construir y ejecutar con Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Verificar contenedores**
   ```bash
   docker-compose ps
   ```

3. **Ver logs**
   ```bash
   docker-compose logs -f franchise-api
   ```

## Pruebas

### Ejecutar Pruebas Unitarias

```bash
./mvnw test
```

### Ejecutar Pruebas con Cobertura

```bash
./mvnw clean test jacoco:report
```

La cobertura de código se genera en `target/site/jacoco/index.html`

### Ejecutar Pruebas de Integración

```bash
./mvnw verify
```

## Consideraciones de Diseño

### Arquitectura Hexagonal

**Ventajas implementadas:**
- **Separación clara de responsabilidades**: El dominio está aislado de la infraestructura
- **Testabilidad**: Fácil mockeo de dependencias externas
- **Flexibilidad**: Cambio de base de datos sin afectar lógica de negocio
- **Mantenibilidad**: Código organizado y fácil de entender

**Implementación:**
- **Puertos**: Interfaces que definen contratos entre capas
- **Adaptadores**: Implementaciones concretas de los puertos
- **Mappers**: Conversión entre entidades de dominio y DTOs/documentos

### Programación Reactiva

**Operadores utilizados:**
- `map`: Transformación de datos
- `flatMap`: Aplanamiento de streams anidados
- `switchIfEmpty`: Manejo de valores vacíos
- `merge`: Combinación de streams
- `zip`: Combinación de múltiples streams

**Beneficios:**
- **Escalabilidad**: Manejo eficiente de concurrencia
- **Recursos**: Uso optimizado de memoria y CPU
- **Responsividad**: No bloqueo de threads

### Persistencia con MongoDB

**Estructura de datos:**
- **Colecciones separadas**: `franchises`, `branches`, `products`
- **Referencias**: Uso de `franchise_id` para relacionar sucursales
- **Índices**: Optimización de consultas por ID

**Ventajas:**
- **Flexibilidad**: Esquema dinámico
- **Escalabilidad**: Distribución horizontal
- **Rendimiento**: Consultas eficientes

### Logging y Monitoreo

**Configuración:**
- **SLF4J + Logback**: Logging estructurado
- **Niveles configurables**: DEBUG, INFO, WARN, ERROR
- **Patrones personalizados**: Formato legible y estructurado

**Endpoints de monitoreo:**
- `/api/v1/health`: Estado de la aplicación
- `/actuator/health`: Health check detallado
- `/actuator/metrics`: Métricas de la aplicación

## Ejemplos de Uso

### Crear una Franquicia

```bash
curl -X POST http://localhost:8080/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{
    "name": "McDonald'\''s",
    "branches": []
  }'
```

### Agregar una Sucursal

```bash
curl -X POST http://localhost:8080/api/v1/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sucursal Centro",
    "products": []
  }'
```

### Agregar un Producto

```bash
curl -X POST http://localhost:8080/api/v1/franchises/{franchiseId}/branches/{branchId}/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Big Mac",
    "stock": 50
  }'
```

### Obtener Productos con Mayor Stock

```bash
curl -X GET http://localhost:8080/api/v1/franchises/{franchiseId}/products/max-stock
```

