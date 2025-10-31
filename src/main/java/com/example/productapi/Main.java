package com.example.productapi;

import static spark.Spark.*;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();

        // 🔹 Serve static files (CSS, JS, images)
        staticFiles.location("/public");

        // 🏠 Home route (renders index.mustache)
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Ramon's Collectibles");
            return new ModelAndView(model, "index.mustache");
        }, new MustacheTemplateEngine());

        // 📦 Products page (renders myproducts.mustache)
        get("/products", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                model.put("products", ProductService.getAllProducts());
            } catch (SQLException e) {
                model.put("error", "Error fetching products: " + e.getMessage());
            }
            return new ModelAndView(model, "myproducts.mustache");
        }, new MustacheTemplateEngine());

        // 🧩 API configuration (only for /api/* routes)
        before("/api/*", (req, res) -> res.type("application/json"));

        // 🟢 GET - Get all products
        get("/api/products", (req, res) -> {
            try {
                return gson.toJson(ProductService.getAllProducts());
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error getting products: " + e.getMessage()));
            }
        });

        // 🟢 GET - Get product by ID
        get("/api/products/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Product product = ProductService.getProductById(id);
                if (product == null) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Product not found"));
                }
                return gson.toJson(product);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid ID format"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Database error: " + e.getMessage()));
            }
        });

        // 🟢 POST - Add new product
        post("/api/products", (req, res) -> {
            Product product = gson.fromJson(req.body(), Product.class);

            if (product.getNombre() == null || product.getNombre().trim().isEmpty()
                    || product.getCosto() <= 0 || product.getCantidad() < 0) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid or incomplete product data"));
            }

            try {
                ProductService.addProduct(product);
                res.status(201);
                return gson.toJson(product);
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error adding product: " + e.getMessage()));
            }
        });

        // 🟠 PUT - Update product by ID
        put("/api/products/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Product existing = ProductService.getProductById(id);
                if (existing == null) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Product not found"));
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
                return gson.toJson(Map.of("error", "Invalid ID format"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error updating product: " + e.getMessage()));
            }
        });

        // 🔴 DELETE - Delete product by ID
        delete("/api/products/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                boolean deleted = ProductService.deleteProduct(id);
                if (!deleted) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Product not found"));
                }
                return gson.toJson(Map.of("message", "Product successfully deleted"));
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid ID format"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error deleting product: " + e.getMessage()));
            }
        });

        System.out.println("🚀 Server running at http://localhost:4567");
    }
}
