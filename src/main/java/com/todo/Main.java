package com.todo;
import java.sql.Connection;
import java.sql.SQLException;
import com.todo.util.DatabaseConnection;
public class Main{
    public static void main(String args [])
    {
        DatabaseConnection db_Connection = new DatabaseConnection();
        try{
            
            Connection cn = db_Connection.getDBConnection();
            System.err.println("The database connection success");
        }
        catch(SQLException e){
            System.err.println("The database connection has failed");
        }
    }
}