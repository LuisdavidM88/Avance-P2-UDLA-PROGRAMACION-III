# 🛻💚 Gestión de Pedidos & Flota de Camiones – UDLA (Programación III)

## 👥 Integrantes
- **Luis Morales**
- **Emily Mullo**
- **Lucas Karlsson**
- **José Tituaña**

---

## 🚀 Descripción del Proyecto

Este proyecto es una aplicación desarrollada en **Java + Swing** que permite gestionar:

- 🟦 **Pedidos de recolección** hechos por clientes  
- 🟩 **Flota de camiones** encargados de atender los pedidos  

Incluye validación de datos, algoritmos de ordenamiento, búsqueda binaria, recursividad, estructuras dinámicas y una interfaz gráfica intuitiva organizada por módulos.

---

## ⚙️ Funcionalidades Principales

### 🔵 Módulo de Pedidos
- Registrar nuevos pedidos  
- Editar información existente  
- Mostrar listado general  
- Calcular peso total por tipo de material (recursivo)  
- Mostrar siguiente pedido en cola (FIFO)  
- Validación completa de todos los campos  

### 🟢 Módulo de Camiones
- Registrar camiones  
- Editar información del camión  
- Ordenar por:
  - **ID** (Bubble Sort)  
  - **Capacidad** (Insertion Sort)  
  - **Año** (Insertion Sort descendente)  
- Enviar camión a ruta (cola de salida)  
- Despachar el siguiente camión  

---

## 📦 Estructura del Proyecto
src/
├── Camion.java
├── PedidoRecoleccion.java
├── GestorCamiones.java
├── GestorPedidos.java
└── Ventana/
└── Ventana.java
