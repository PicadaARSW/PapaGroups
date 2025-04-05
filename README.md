# PapaGroups Microservice - WhereWeApp

Este microservicio forma parte de la arquitectura de la aplicación **WhereWeApp**, una solución móvil desarrollada en React Native con Expo que permite a los usuarios compartir su ubicación en tiempo real con grupos de amigos. El servicio de **PapaGroups** es responsable de la gestión y operación de los grupos.

---

## 🧩 Tipo de Microservicio

- **Dominio**: Gestión de grupos
- **Base de datos**: MongoDB
- **Framework**: Spring Boot
- **Arquitectura**: RESTful + DTO + Swagger
- **Integración**: Azure AD (EntraID) vía tokens para identificar al usuario
- **Transporte**: HTTP REST
- **Otros servicios relacionados**: Servicio de usuarios (`UserService`), servicio de ubicaciones (`LocationService`)

---

## 📦 Endpoints

> Todos los endpoints están bajo el prefijo `/api/v1/groups`.

### POST `/`
Crea un nuevo grupo. El cuerpo debe contener:
```json
{
  "admin": "user-id",
  "nameGroup": "Nombre del grupo"
}
```

### GET `/`
Obtiene todos los grupos existentes.

### GET `/{id}`
Obtiene los detalles de un grupo específico por ID.

### GET `/user/{userId}`
Obtiene todos los grupos a los que pertenece un usuario.

### POST `/join/{code}/{userId}`
Permite a un usuario unirse a un grupo mediante un código único.

---

## 📄 Modelo de datos (`Group`)

```json
{
  "id": "507f1f77bcf86cd799439011",
  "admin": "admin123",
  "nameGroup": "Study Group",
  "members": ["user1", "user2"],
  "code": "ABC123"
}
```

---

## ✅ Cobertura de Pruebas - JaCoCo

![alt text](image.png)

### Generar reporte:
```bash
mvn clean verify
```

Reporte HTML generado en:  
`/target/site/jacoco/index.html`

![image](https://github.com/user-attachments/assets/47654009-12bd-4440-ba49-5ce3d90f492b)

---

## 📊 Análisis de Calidad - SonarCloud

![image](https://github.com/user-attachments/assets/5baf9363-0d50-431b-b8ee-06838735b65d)


Incluye:
- Cobertura de JaCoCo
- Reglas de calidad
- Análisis estático (bugs, code smells, duplicación)

---

## 📌 Consideraciones

- Los métodos del servicio utilizan conversiones explícitas entre entidades (`Group`) y DTOs (`GroupDTO`) para desacoplar el modelo de base de datos del API.
- Se utiliza `UUID` para la generación del código de grupo.
- El servicio valida si el usuario ya pertenece al grupo antes de permitir su unión.
- Todas las operaciones están documentadas con anotaciones de Swagger/OpenAPI.

---

## 👨‍💻 Equipo de Desarrollo

- **Team Picada ARSW 2025**
- 📧 picadaarsw2025@outlook.com

---
