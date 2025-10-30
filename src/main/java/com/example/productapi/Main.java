package com.example.productapi;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();

        // ✅ Configuración global
        before((req, res) -> res.type("application/json"));

        // 🟢 GET - Obtener todos los productos
        get("/productos", (req, res) -> {
            try {
                return gson.toJson(ProductService.getAllProducts());
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error al obtener los productos: " + e.getMessage()));
            }
        });

        // 🟢 GET - Obtener producto por ID
        get("/productos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Product product = ProductService.getProductById(id);
                if (product == null) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Producto no encontrado"));
                }
                return gson.toJson(product);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "ID inválido"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error al obtener el producto"));
            }
        });

        // 🟢 POST - Agregar un nuevo producto
        post("/productos", (req, res) -> {
            Product product = gson.fromJson(req.body(), Product.class);

            if (product.getNombre() == null || product.getNombre().trim().isEmpty()
                    || product.getCosto() <= 0 || product.getCantidad() < 0) {
                res.status(400);
                return gson.toJson(Map.of("error", "Datos inválidos o incompletos"));
            }

            try {
                ProductService.addProduct(product);
                res.status(201);
                return gson.toJson(product);
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error al agregar el producto"));
            }
        });

        // 🟠 PUT - Actualizar un producto por ID
        put("/productos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Product existing = ProductService.getProductById(id);
                if (existing == null) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Producto no encontrado"));
                }

                Product updated = gson.fromJson(req.body(), Product.class);
                existing.setNombre(updated.getNombre());
                existing.setDescripcion(updated.getDescripcion());
                existing.setCosto(updated.getCosto());
                existing.setCantidad(updated.getCantidad());

                ProductService.updateProduct(existing);
                return gson.toJson(existing);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "ID inválido"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error al actualizar el producto"));
            }
        });

        // 🔴 DELETE - Eliminar producto por ID
        delete("/productos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                boolean deleted = ProductService.deleteProduct(id);
                if (!deleted) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Producto no encontrado"));
                }
                return gson.toJson(Map.of("message", "Producto eliminado correctamente"));
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "ID inválido"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error al eliminar el producto"));
            }
        });

        System.out.println("🚀 Servidor corriendo en http://localhost:4567");
    }
}
