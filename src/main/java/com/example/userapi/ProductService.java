package com.example.userapi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // Obtener todos los productos
    public static List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCosto(rs.getDouble("costo"));
                p.setCantidad(rs.getInt("cantidad"));
                products.add(p);
            }
        }
        return products;
    }

    // Obtener producto por ID
    public static Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCosto(rs.getDouble("costo"));
                p.setCantidad(rs.getInt("cantidad"));
                return p;
            }
        }
        return null;
    }

    // Agregar producto
    public static Product addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, costo, cantidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getNombre());
            ps.setString(2, product.getDescripcion());
            ps.setDouble(3, product.getCosto());
            ps.setInt(4, product.getCantidad());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
        }
        return product;
    }

    // Actualizar producto
    public static void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, costo = ?, cantidad = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getNombre());
            ps.setString(2, product.getDescripcion());
            ps.setDouble(3, product.getCosto());
            ps.setInt(4, product.getCantidad());
            ps.setInt(5, product.getId());
            ps.executeUpdate();
        }
    }

    // Eliminar producto
    public static boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
