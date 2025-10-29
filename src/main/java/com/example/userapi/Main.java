package com.example.userapi;

import static spark.Spark.*;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        port(4567);
        exception(Exception.class, (e, req, res) -> {
    e.printStackTrace(); // muestra el error completo en la consola
});
        staticFiles.location("/public");
        Gson gson = new Gson();

        // ---------------- REST API (ya existentes) ----------------
        get("/users", (req, res) -> {
            res.type("application/json");
            return gson.toJson(UserService.getAllUsers());
        });

        get("/users/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            User u = UserService.getUserById(id);
            if (u == null) {
                res.status(404);
                return gson.toJson(Map.of("message", "User not found"));
            }
            return gson.toJson(u);
        });

        post("/users/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            User user = gson.fromJson(req.body(), User.class);
            user.setId(id);
            UserService.addUser(user);
            res.status(201);
            return gson.toJson(Map.of("message", "User added", "user", user));
        });

        put("/users/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            if (!UserService.userExists(id)) {
                res.status(404);
                return gson.toJson(Map.of("message", "User not found"));
            }
            User user = gson.fromJson(req.body(), User.class);
            user.setId(id);
            UserService.updateUser(id, user);
            return gson.toJson(Map.of("message", "User updated", "user", user));
        });

        delete("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            if (!UserService.userExists(id)) {
                res.status(404);
                return gson.toJson(Map.of("message", "User not found"));
            }
            UserService.deleteUser(id);
            return gson.toJson(Map.of("message", "User deleted"));
        });

        // ---------------- FRONTEND VIEWS ----------------
        get("/", (req, res) -> new ModelAndView(new HashMap<>(), "index.mustache"), new MustacheTemplateEngine());

        get("/hello", (req, res) -> "Hello world from Spark!");

        // Show all users (HTML list)
        get("/users/html", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("users", UserService.getAllUsers());
            return new ModelAndView(model, "users.mustache");
        }, new MustacheTemplateEngine());

        // New user form
        get("/users/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("action", "/users/save");
            model.put("isEdit", false);
            return new ModelAndView(model, "form.mustache");
        }, new MustacheTemplateEngine());

        // Save new user
        post("/users/save", (req, res) -> {
            String name = req.queryParams("name");
            String email = req.queryParams("email");

            int id = UserService.getAllUsers().size() + 1;
            User newUser = new User(id, name, email);
            UserService.addUser(newUser);
            res.redirect("/users/html");
            return null;
        });

        // Edit user form
        get("/users/edit/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            User u = UserService.getUserById(id);
            if (u == null) {
                res.redirect("/users/html");
                return null;
            }
            Map<String, Object> model = new HashMap<>();
            model.put("action", "/users/update/" + id);
            model.put("isEdit", true);
            model.put("name", u.getName());
            model.put("email", u.getEmail());
            return new ModelAndView(model, "form.mustache");
        }, new MustacheTemplateEngine());

        // Update user
        post("/users/update/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            String name = req.queryParams("name");
            String email = req.queryParams("email");

            User updated = new User(id, name, email);
            UserService.updateUser(id, updated);
            res.redirect("/users/html");
            return null;
        });

        // Delete user (from HTML)
        get("/users/delete/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            UserService.deleteUser(id);
            res.redirect("/users/html");
            return null;
        });
    }
}