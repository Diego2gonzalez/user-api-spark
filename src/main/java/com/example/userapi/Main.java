package com.example.userapi;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();

        // GET /users
        get("/users", (req, res) -> {
            res.type("application/json");
            return gson.toJson(UserService.getAllUsers());
        });

        // GET /users/:id
        get("/users/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            User u = UserService.getUserById(id);
            if(u == null) {
                res.status(404);
                return gson.toJson(Map.of("message","User not found"));
            }
            return gson.toJson(u);
        });

        // POST /users/:id
        post("/users/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            User user = gson.fromJson(req.body(), User.class);
            user.setId(id);
            UserService.addUser(user);
            res.status(201);
            return gson.toJson(Map.of("message","User added","user",user));
        });

        // PUT /users/:id
        put("/users/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            if(!UserService.userExists(id)) {
                res.status(404);
                return gson.toJson(Map.of("message","User not found"));
            }
            User user = gson.fromJson(req.body(), User.class);
            user.setId(id);
            UserService.updateUser(id, user);
            return gson.toJson(Map.of("message","User updated","user",user));
        });

        // OPTIONS /users/:id
        options("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            res.type("application/json");
            return gson.toJson(Map.of("exists", UserService.userExists(id)));
        });

        // DELETE /users/:id
        delete("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            if(!UserService.userExists(id)) {
                res.status(404);
                return gson.toJson(Map.of("message","User not found"));
            }
            UserService.deleteUser(id);
            return gson.toJson(Map.of("message","User deleted"));
        });
    }
}
