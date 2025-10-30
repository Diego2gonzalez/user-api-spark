package com.example.productapi;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();

        // Rutas de la API
        get("/productos", (req, res) -> {
            res.type("application/json");
            try {
                return gson.toJson(ProductService.getAllProducts());
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error al obtener los productos"));
            }
        });

        post("/productos", (req, res) -> {
            res.type("application/json");
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

        System.out.println("🚀 Servidor corriendo en http://localhost:4567");
    }
}
