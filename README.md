
# TransGate – Android Management App

## 📌 Descripción

TransGate es una aplicación Android diseñada para gestionar operaciones en tiempo real dentro del ecosistema Transdoor. Permite a usuarios administrativos consultar y actualizar datos directamente desde dispositivos móviles, eliminando la dependencia de sistemas de escritorio.

## 🎯 Problema que resuelve

En entornos operativos, el acceso a información suele depender de sistemas centralizados, lo que limita la rapidez de respuesta.

Esta aplicación aborda ese problema proporcionando:

* Acceso remoto a datos críticos
* Actualización inmediata de registros
* Reducción de procesos manuales

## 🧠 Mi aportación

Desarrollo completo de la capa cliente Android, incluyendo:

* Implementación de consumo de API REST
* Gestión de autenticación de usuarios
* Procesamiento de datos JSON desde backend SQL
* Diseño de la estructura de navegación y flujo de la app
* Manejo de estados y sincronización con servidor

## ⚙️ Stack Tecnológico

* Lenguaje: Java (Android SDK)
* Arquitectura: Cliente–Servidor (API REST)
* Formato de datos: JSON
* Gestión de dependencias: Gradle

## 🔄 Flujo de funcionamiento

1. El usuario inicia sesión mediante credenciales seguras
2. La app realiza petición a la API REST
3. El backend procesa la solicitud y devuelve datos en formato JSON
4. La app renderiza la información en la interfaz
5. Las modificaciones se envían al servidor en tiempo real

## 🧩 Decisiones técnicas

* Uso de API REST para desacoplar frontend y backend
* Manejo de datos en JSON por su compatibilidad y ligereza
* Arquitectura modular para facilitar escalabilidad
* Validación básica de sesiones para control de acceso

## 📁 Estructura del proyecto

/app/src/main
→ Código fuente Java y layouts XML

/gradle
→ Configuración del sistema de build



Este proyecto está orientado a la capa cliente y depende de un backend externo (Transdoor API).


