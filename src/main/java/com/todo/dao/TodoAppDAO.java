package com.todo.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import com.todo.util.DatabaseConnection;
import com.todo.model.Todo;

public class TodoAppDAO {
    
    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos order by created_at DESC";
    private static final String INSERT_TODO="Insert INTO todos(title,description,completed,created_at,updated_at) VALUES(?,?,?,?,?)";
    //create a new todo
    public int createtodo(Todo todo) throws SQLException
    {
        try(
            Connection conn=DatabaseConnection.getDBConnection();
            PreparedStatement stmt=conn.prepareStatement(INSERT_TODO,Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1,todo.getTitle());
            stmt.setString(2, todo.getDescription());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setTimestamp(4,Timestamp.valueOf(todo.getCreated_at()));
            stmt.setTimestamp(5, Timestamp.valueOf(todo.getUpdated_at()));
            int rowAffected=stmt.executeUpdate();
            if(rowAffected==0){
               throw new SQLException("Creating todo is failed ,no row is Insertes");
            }
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Creating todo failed,No id obtained");
                    
                }

            }
            
        }
    }


    private Todo getTodoRow(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String title= rs.getString("title");
        String description =rs.getString("description");
        boolean completed= rs.getBoolean("completed");
        LocalDateTime created_at = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updated_at = rs.getTimestamp("updated_at").toLocalDateTime();
        Todo todo=new Todo(id,title,description,completed,created_at,updated_at);
        return todo;

    }
    public List<Todo> getAllTodos() throws SQLException {
        // Implement database retrieval logic here
        List<Todo> todos=new ArrayList<>();

        try (Connection conn= DatabaseConnection.getDBConnection();
        PreparedStatement stmt=conn.prepareStatement(SELECT_ALL_TODOS);
        ResultSet res=stmt.executeQuery();)
        {
            while(res.next())
            {
                todos.add(getTodoRow(res));
            }
        } catch(SQLException e) {
            System.err.println("Error fetching todos: "+e.getMessage());
            throw e; // rethrow the exception after logging

        }
        return todos;
    }

}