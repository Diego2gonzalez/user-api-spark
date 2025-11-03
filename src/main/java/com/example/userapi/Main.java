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

        // --------------------------------------------------
        // 🔧 CONFIGURACIÓN INICIAL
        // --------------------------------------------------
        port(4567);
        staticFiles.location("/public");     // Archivos estáticos (CSS, JS, imágenes)
        webSocket("/prices", PriceSocket.class); // WebSocket para actualizaciones en tiempo real

        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        Gson gson = new Gson();

        // --------------------------------------------------
        // 🌐 VISTAS (Mustache)
        // --------------------------------------------------

        // 🏠 Página principal - renderiza index.mustache
        get("/", (req, res) ->
                new ModelAndView(new HashMap<>(), "index.mustache"),
                new MustacheTemplateEngine()
        );

        // 📋 Listado de productos
        get("/products", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("products", ProductService.getAllProducts());
            return new ModelAndView(model, "products.mustache");
        }, new MustacheTemplateEngine());

        // 🆕 Formulario para nuevo producto
        get("/newproduct", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("action", "/savenewproduct");
            model.put("isEdit", false);
            return new ModelAndView(model, "form.mustache");
        }, new MustacheTemplateEngine());

        // 💾Guardar nuevo producto (desde formulario HTML)
        post("/savenewproduct", (req, res) -> {
            String nombre = req.queryParams("nombre");
            String descripcion = req.queryParams("descripcion");
            double costo = Double.parseDouble(req.queryParams("costo"));
            int cantidad = Integer.parseInt(req.queryParams("cantidad"));

            Product nuevo = new Product();
            nuevo.setNombre(nombre);
            nuevo.setDescripcion(descripcion);
            nuevo.setCosto(costo);
            nuevo.setCantidad(cantidad);

            ProductService.addProduct(nuevo);
            res.redirect("/products");
            return null;
        });

        // ✏️ Editar producto (vista de formulario)
        get("/products/edit/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Product p = ProductService.getProductById(id);
            if (p == null) {
                res.redirect("/products");
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

        // 🗞️ Actualizar producto (desde formulario)
        post("/products/update/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            String nombre = req.queryParams("nombre");
            String descripcion = req.queryParams("descripcion");
            double costo = Double.parseDouble(req.queryParams("costo"));
            int cantidad = Integer.parseInt(req.queryParams("cantidad"));

            Product actualizado = new Product();
            actualizado.setId(id);
            actualizado.setNombre(nombre);
            actualizado.setDescripcion(descripcion);
            actualizado.setCosto(costo);
            actualizado.setCantidad(cantidad);

            ProductService.updateProduct(actualizado);
            res.redirect("/products");
            return null;
        });

        // ❌ Eliminar producto (desde HTML)
        get("/products/delete/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            ProductService.deleteProduct(id);
            res.redirect("/products");
            return null;
        });

        // Endpoint de prueba
        get("/hello", (req, res) -> "Hello world from Spark!");

        // --------------------------------------------------
        // ⚙️ REST API (JSON)
        // --------------------------------------------------

        // 🧮 Filtros dinámicos de productos
        get("/productos/filter", (req, res) -> {
            res.type("application/json");
            String nombre = req.queryParams("nombre");
            String minCosto = req.queryParams("minCosto");
            String maxCosto = req.queryParams("maxCosto");
            String minCantidad = req.queryParams("minCantidad");
            String maxCantidad = req.queryParams("maxCantidad");
            try {
                return gson.toJson(ProductService.filterProducts(nombre, minCosto, maxCosto, minCantidad, maxCantidad));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error filtering products: " + e.getMessage()));
            }
        });



        // 🟢 Todos los productos
        get("/productos", (req, res) -> {
            res.type("application/json");
            try {
                return gson.toJson(ProductService.getAllProducts());
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error retrieving products: " + e.getMessage()));
            }
        });

        // 🟢 Producto por ID
        get("/productos/:id", (req, res) -> {
            res.type("application/json");
            try {
                int id = Integer.parseInt(req.params(":id"));
                Product p = ProductService.getProductById(id);
                if (p == null) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Product not found"));
                }
                return gson.toJson(p);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid ID"));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Database error"));
            }
        });

        // 🟢 Crear producto (JSON)
        post("/productos", (req, res) -> {
            res.type("application/json");
            Product product = gson.fromJson(req.body(), Product.class);
            if (product.getNombre() == null || product.getNombre().isEmpty()
                    || product.getCosto() <= 0 || product.getCantidad() < 0) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid or missing fields"));
            }
            try {
                ProductService.addProduct(product);
                res.status(201);
                return gson.toJson(product);
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error adding product"));
            }
        });

        // 🟠 Actualizar producto (JSON)
        put("/productos/:id", (req, res) -> {
            res.type("application/json");
            try {
                int id = Integer.parseInt(req.params(":id"));
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
                // 🧩 Emitir actualización en tiempo real
                PriceSocket.broadcastPriceChange(existing.getId(), existing.getCosto());
                return gson.toJson(existing);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error updating product: " + e.getMessage()));
            }
        });

        // 🔴 Eliminar producto (JSON)
        delete("/productos/:id", (req, res) -> {
            res.type("application/json");
            try {
                int id = Integer.parseInt(req.params(":id"));
                boolean deleted = ProductService.deleteProduct(id);
                if (!deleted) {
                    res.status(404);
                    return gson.toJson(Map.of("error", "Product not found"));
                }
                return gson.toJson(Map.of("message", "Product deleted"));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error deleting product: " + e.getMessage()));
            }
        });

        
        // --------------------------------------------------
        // 🚀 INICIALIZAR SERVIDOR
        // --------------------------------------------------
        init();
        awaitInitialization();   // Asegura que Jetty levante las rutas antes de atender
        System.out.println("🚀 Server running on http://localhost:4567");
    }
}