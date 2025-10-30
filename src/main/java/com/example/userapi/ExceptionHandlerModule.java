package com.example.userapi;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.Map;

public class ExceptionHandlerModule {
    private static final Gson gson = new Gson();

    public static void registerExceptionHandlers() {

        // ❌ Datos inválidos
        exception(InvalidInputException.class, (ex, req, res) -> {
            res.status(400);
            res.type("application/json");
            res.body(gson.toJson(Map.of("error", ex.getMessage())));
        });

        // ❌ Producto no encontrado
        exception(ProductNotFoundException.class, (ex, req, res) -> {
            res.status(404);
            res.type("application/json");
            res.body(gson.toJson(Map.of("error", ex.getMessage())));
        });

        // ⚠️ Cualquier otro error no controlado
        exception(Exception.class, (ex, req, res) -> {
            res.status(500);
            res.type("application/json");
            res.body(gson.toJson(Map.of("error", "Error interno del servidor")));
            ex.printStackTrace(); // para depuración
        });
    }
}