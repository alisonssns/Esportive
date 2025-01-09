package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CRUD {

    public ResultSet select(String query, ArrayList<Object> values) throws SQLException {
        Connection conn = Conector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);

        setStatementValues(stmt, values);

        return stmt.executeQuery();
    }

    public int insert(String query, ArrayList<Object> values) throws SQLException {
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            setStatementValues(stmt, values);

            return stmt.executeUpdate();
        }
    }

    public int update(String query, ArrayList<Object> values) throws SQLException {
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            setStatementValues(stmt, values);

            return stmt.executeUpdate();
        }
    }

    public int delete(String query, ArrayList<Object> values) throws SQLException {
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            setStatementValues(stmt, values);

            return stmt.executeUpdate();
        }
    }

    private void setStatementValues(PreparedStatement stmt, ArrayList<Object> values) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (value instanceof Integer) {
                stmt.setInt(i + 1, (Integer) value);
            } else if (value instanceof String) {
                stmt.setString(i + 1, (String) value);
            } else if (value instanceof Double) {
                stmt.setDouble(i + 1, (Double) value);
            } else if (value instanceof Float) {
                stmt.setFloat(i + 1, (Float) value);
            } else if (value instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) value);
            } else if (value instanceof Long) {
                stmt.setLong(i + 1, (Long) value);
            } else if (value instanceof java.sql.Date) {
                stmt.setDate(i + 1, (java.sql.Date) value);
            } else if (value instanceof java.sql.Time) {
                stmt.setTime(i + 1, (java.sql.Time) value);
            } else if (value == null) {
                stmt.setNull(i + 1, java.sql.Types.NULL);
            } else {
                throw new IllegalArgumentException("Tipo de dado não suportado: " + value.getClass());
            }
        }
    }

    public void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}