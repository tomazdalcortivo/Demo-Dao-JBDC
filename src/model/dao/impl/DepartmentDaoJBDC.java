package model.dao.impl;

import db.DB;
import java.util.List;
import model.dao.DepartmentDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.entities.Department;

public class DepartmentDaoJBDC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJBDC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "Insert into department "
                    + "(Name) "
                    + "values "
                    + "(?)", PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new RuntimeException("Unexpected error! No rows affected!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "upadate department "
                    + "set Name = ? Id = ?  "
                    + "where Id = ? ");
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareCall("delete from department where Id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "Select * from department where Id = ? ");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department dep = new Department();
                dep.setId(rs.getInt("Id"));
                dep.setName(rs.getString("Name"));
                return dep;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving seller: " + e.getMessage(), e);
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM department ORDER BY Name");
            rs = st.executeQuery();

            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

}
