package com.example.userapi;

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
        exception(Exception.class, (e, req, res) -> {
    e.printStackTrace(); // Show the stack trace in the console
});
        staticFiles.location("/public");
        Gson gson = new Gson();

        // ---------------- REST API (ya existentes) ----------------

        // 🟢 GET - Obtener todos los productos
        get("/productos", (req, res) -> {
            try {
                return gson.toJson(ProductService.getAllProducts());
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error retrieving products: " + e.getMessage()));
            }
        });

        // 🟢 GET - Obtains product by ID
        get("/productos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Product product = ProductService.getProductById(id);
                if (product == null) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Product Not Found"));
                }
                return gson.toJson(product);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid ID"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error retrieving product"));
            }
        });

        // 🟢 POST - Add new product
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

        // 🟠 PUT - Update product by ID
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

        // 🔴 DELETE - Delete product by ID
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

        System.out.println("🚀 Server running on http://localhost:4567");

        // ---------------- FRONTEND VIEWS ----------------
        get("/", (req, res) -> new ModelAndView(new HashMap<>(), "index.mustache"), new MustacheTemplateEngine());

        get("/hello", (req, res) -> "Hello world from Spark!");

        // ---------- My Products ----------
        get("/products", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("products", ProductService.getAllProducts());
            return new ModelAndView(model, "products.mustache");
        }, new MustacheTemplateEngine());

        // New product form
        get("/newproduct", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("action", "/savenewproduct");
            model.put("isEdit", false);
            return new ModelAndView(model, "form.mustache");
        }, new MustacheTemplateEngine());

        // Save new product
        post("/savenewproduct", (req, res) -> {
            String nombre = req.queryParams("nombre");
            String descripcion = req.queryParams("descripcion");
            double costo = Double.parseDouble(req.queryParams("costo"));
            int cantidad = Integer.parseInt(req.queryParams("cantidad"));

            int id = ProductService.getAllProducts().size() + 1;
            Product newProduct = new Product();
            newProduct.setId(id);
            newProduct.setNombre(nombre);
            newProduct.setDescripcion(descripcion);
            newProduct.setCosto(costo);
            newProduct.setCantidad(cantidad);
            ProductService.addProduct(newProduct);
            res.redirect("/showproducts");
            return null;
        });

        // Edit product form
        get("/products/edit/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Product p = ProductService.getProductById(id);
            if (p == null) {
                res.redirect("/products/html");
                return null;
            }
            Map<String, Object> model = new HashMap<>();
            model.put("action", "/products/update/" + id);
            model.put("isEdit", true);
            model.put("nombre", p.getNombre());
            model.put("descripcion", p.getDescripcion());
            model.put("costo", p.getCosto());
            model.put("cantidad", p.getCantidad());
            return new ModelAndView(model, "form.mustache");
        }, new MustacheTemplateEngine());

        // Update user
        post("/users/update/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            String nombre = req.queryParams("nombre");
            String descripcion = req.queryParams("descripcion");
            double costo = Double.parseDouble(req.queryParams("costo"));
            int cantidad = Integer.parseInt(req.queryParams("cantidad"));

            Product updated = new Product();
            updated.setId(id);
            updated.setNombre(nombre);
            updated.setDescripcion(descripcion);
            updated.setCosto(costo);
            updated.setCantidad(cantidad);
            ProductService.updateProduct(updated);
            res.redirect("/users/html");
            return null;
        });

        // Delete user (from HTML)
        get("/users/delete/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            ProductService.deleteProduct(id);
            res.redirect("/users/html");
            return null;
        });
    }
}