# roony-specification-error-spring

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)

Puente (bridge) entre **`roony-specification-core`** y **`roony-error`** para Spring Boot.

Registra automáticamente un `@RestControllerAdvice` (auto-configuración) que captura las `FilterException` lanzadas al procesar filtros dinámicos y las devuelve como respuestas de error `400` en el formato estándar de `roony-error` (con `path` y `traceId`).

## Instalación

```xml
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-specification-error-spring</artifactId>
    <version>1.0.0</version>
</dependency>
```

O, si usas el BOM del ecosistema:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>1.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>io.github.roony11-1</groupId>
        <artifactId>roony-specification-core</artifactId>
        <!-- sin versión: la hereda del BOM -->
    </dependency>
    <dependency>
        <groupId>io.github.roony11-1</groupId>
        <artifactId>roony-specification-error-spring</artifactId>
        <!-- sin versión: la hereda del BOM -->
    </dependency>
</dependencies>
```

## Uso

Una petición con un filtro inválido, por ejemplo un valor mal tipado o un operador desconocido, dispara una `FilterException`:

```text
GET /productos?precio=gte|abc
```

Respuesta `400`:

```json
{
    "code": "ERR-0002",
    "message": "Entrada inválida",
    "timestamp": "2026-08-08T12:00:00Z",
    "path": "/productos"
}
```

> El código de error depende de la categoría registrada para `INVALID_INPUT` en `HttpStatusRegistry` (por defecto `400`).

## Comportamiento

- Convierte `FilterException` → `InvalidInputException` → código HTTP `400`.
- Rellena `path` y `traceId` igual que `roony-error-spring`.
- No interfiere con `roony-error-spring`; puedes usar ambos en el mismo proyecto (este advice solo gestiona `FilterException`).

## Dependencias

- `roony-specification-core` (filtros)
- `roony-error-core` + `roony-error-rest` (formato de error y `HttpStatusRegistry`)

---

MIT License · [Roony11-1](https://github.com/roony11-1)