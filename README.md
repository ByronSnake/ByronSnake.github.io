# Proyecto de Control Backend para Envíos 24/7 - Starken

Este proyecto Java Spring es una aplicación backend diseñada para controlar la lógica de negocios de un sistema de envíos 24/7 de Starken. La aplicación se ejecuta en una Raspberry Pi 4 con Ubuntu y es fundamental para el funcionamiento y gestión eficiente del sistema de envíos automatizado.

## Funcionalidades Principales

### Control de lógica de negocios:
- El backend gestiona la lógica de negocios del sistema, procesando solicitudes entrantes del frontend y generando respuestas adecuadas.
- Interactúa con servicios web externos para validar medidas, etiquetas de envío, etc.

### Interacción con componentes físicos:
- Interactúa con partes físicas de la máquina, como puertas, sensores, cintas transportadoras, cámaras, rodillos, etc.
- Implementa una API para el control de elementos físicos de la máquina, permitiendo operaciones como abrir/cerrar puertas, activar/detener cintas transportadoras, etc.

### Notificaciones de incidencias:
- La aplicación envía notificaciones o alertas en tiempo real sobre incidencias físicas detectadas por los sensores de la máquina.
- Ejemplos de notificaciones incluyen alertas cuando una puerta no se cierra correctamente o cuando el cliente no confirma la operación.

## Tecnologías Utilizadas

- Java Spring: Utilizado para desarrollar la lógica de negocio del backend.
- Servicios web externos: Integración con servicios web externos para validar medidas, etiquetas, etc.
- Raspberry Pi 4 con Ubuntu: Plataforma de hardware para ejecutar la aplicación backend.
- API de Control: Implementación de una API para controlar elementos físicos de la máquina.
- Notificaciones en tiempo real: Implementadas para alertar sobre incidencias físicas.

## Contacto

Para más información o preguntas, contáctame a través de:
- Email: byro.lopez@live.cl
- LinkedIn: www.linkedin.com/in/byronlopezs

