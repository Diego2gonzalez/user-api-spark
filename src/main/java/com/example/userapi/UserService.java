package com.example.userapi;

import java.util.*;

public class UserService {
    private static Map<Integer, User> users = new HashMap<>();
    public static Collection<User> getAllUsers() { return users.values(); }
    public static User getUserById(int id) { return users.get(id); }
    public static void addUser(User user) { users.put(user.getId(), user); }
    public static void updateUser(int id, User user) { users.put(id, user); }
    public static void deleteUser(int id) { users.remove(id); }
    public static boolean userExists(int id) { return users.containsKey(id); }
}
