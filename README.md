# Tienda Zapatos Zaretti

## Descripción del proyecto

Nuestro proyecto es un e-commerce enfocado en la venta de zapatos y zapatillas, con una aplicación móvil para clientes y un panel de administración.

## Integrantes

- Fernando Carter
- Julio Llauri
- Fernanda Orellana

## Funcionalidades implementadas

- **Autenticación:** Registro e inicio de sesión de usuarios.
- **Catálogo:** Visualización de productos con detalles (nombre, descripción, precio, imagen).
- **Carrito de compras:** Añadir y eliminar productos del carrito y ver el total.
- **Historial de pedidos:** Los usuarios pueden ver sus compras anteriores.
- **Panel de administración:** Permite crear, editar y eliminar productos del catálogo.
- **API Externa:** Se consume una API externa para mostrar una lista de posts a modo de "novedades" o "blog".

## Endpoints Utilizados

A continuación se detallan los endpoints que la aplicación consume.

### 1. Microservicio Propio (Spring Boot)

La aplicación está diseñada para conectarse a un microservicio propio que maneja toda la lógica de negocio.

- `GET /api/products`: Obtiene la lista completa de productos.
- `POST /api/products`: Crea un nuevo producto (requiere rol de administrador).
- `PUT /api/products/{id}`: Actualiza un producto existente (requiere rol de administrador).
- `DELETE /api/products/{id}`: Elimina un producto (requiere rol de administrador).
- `POST /api/auth/register`: Registra un nuevo usuario.
- `POST /api/auth/login`: Inicia sesión y devuelve un token de autenticación.
- `POST /api/orders`: Crea un nuevo pedido con los productos del carrito del usuario.

### 2. API Externa (JSONPlaceholder)

Se utiliza como un ejemplo de consumo de API de terceros para mostrar contenido adicional en la app.

- `GET https://jsonplaceholder.typicode.com/posts`: Obtiene una lista de posts de ejemplo.

## Pasos para ejecutar

1.  **Ejecutar el microservicio:**
    - Abrir el proyecto del microservicio en IntelliJ IDEA.
    - Ejecutar la clase principal `TiendazapatosApplication`.
    - Verificar que el servidor funciona visitando `http://localhost:8080/api/products` en un navegador.
2.  **Ejecutar la aplicación móvil:**
    - Abrir este proyecto en Android Studio.
    - Sincronizar las dependencias de Gradle.
    - **Importante:** Actualizar la IP del microservicio en el archivo `app/src/main/java/com/example/tiendazapatos/data/remote/RetrofitInstance.kt` para que coincida con la IP de la máquina donde se ejecuta el servidor.
    - Conectar un dispositivo Android o iniciar un emulador.
    - Ejecutar la aplicación (Botón "Run").
