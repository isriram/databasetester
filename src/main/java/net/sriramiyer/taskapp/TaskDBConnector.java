package net.sriramiyer.taskapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class TaskDBConnector {
    private String dbURL;
    private Connection databaseConnection;
    private String username = "system";
    private String password = "password";
    private String tableName = "task";
    private String colRowID = "id";
    private String colTaskName = "taskname";
    private String colCompletedStatus = "completed";


    TaskDBConnector() {
        this.dbURL = "jdbc:h2:./task";

        boolean dbExists = this.doesDatabaseExist();
        if (!dbExists) {
            try {
                this.setUpDatabase();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    boolean doesDatabaseExist() {
        String dbURLifExists = this.dbURL + ";IFEXISTS=TRUE";
        try {
            this.databaseConnection = DriverManager.getConnection(dbURLifExists, this.username, this.password);
        } catch (Exception e) {
            //System.err.println(e);
            //System.out.println("Generating new database");
        }
        return (this.databaseConnection != null);
    }

    private void setUpDatabase() {
        try {
            this.openDatabase();
            String createTable = "CREATE TABLE " + this.tableName + " ( "
                    + this.colRowID + " INT AUTO_INCREMENT PRIMARY KEY, "
                    + this.colTaskName + " VARCHAR, "
                    + this.colCompletedStatus + " BOOLEAN ); ";
            PreparedStatement ps;
            ps = this.databaseConnection.prepareStatement(createTable);
            ps.execute();
            this.closeDatabase();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void openDatabase() throws Exception {
        this.databaseConnection = DriverManager.getConnection(this.dbURL, this.username, this.password);
    }

    private void closeDatabase() throws Exception {
        this.databaseConnection.close();
    }

    void addTask(Task task) {
        String addTask = "INSERT INTO " + this.tableName + " ("
                + this.colTaskName + ", "
                + this.colCompletedStatus + ") VALUES (? , ?)";
        PreparedStatement ps;
        try {
            this.openDatabase();
            ps = this.databaseConnection.prepareStatement(addTask);
            ps.setString(1, task.getTaskName());
            ps.setBoolean(2, task.getCompletedStatus());
            ps.executeUpdate();
            this.closeDatabase();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    void updateTask(Task task) {
        String addTask = "UPDATE " + this.tableName + " SET "
                + this.colTaskName + "= ? , "
                + this.colCompletedStatus + "= ? WHERE "
                + this.colRowID + "= ?;";
        PreparedStatement ps;
        try {
            this.openDatabase();
            ps = this.databaseConnection.prepareStatement(addTask);
            ps.setString(1, task.getTaskName());
            ps.setBoolean(2, task.getCompletedStatus());
            ps.setInt(3, task.getId());
            ps.executeUpdate();
            this.closeDatabase();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    Task getTaskByID(int taskID) {
        String getTask = "SELECT * FROM " + this.tableName + " WHERE " + this.colRowID + " = ?;";
        PreparedStatement ps;
        ResultSet rs;
        Task task = null;
        try {
            this.openDatabase();
            ps = this.databaseConnection.prepareStatement(getTask);
            ps.setInt(1, taskID);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String taskName = rs.getString(2);
                boolean taskCompleted = rs.getBoolean(3);
                task = new Task(id, taskName, taskCompleted);
            }
            this.closeDatabase();
        } catch (Exception e) {
            System.err.println(e);
        }
        if(task != null) {
            return task;
        }
        else {
            return new Task(-1, "", false);
        }
    }

    ArrayList<Task> getTaskByTaskName(String taskNameToSearch) {
        String getTask = "SELECT * FROM " + this.tableName + " WHERE " + this.colTaskName + " LIKE ?;";
        PreparedStatement ps;
        ResultSet rs;
        ArrayList<Task> taskList = new ArrayList<Task>();
        Task task = null;
        try {
            this.openDatabase();
            ps = this.databaseConnection.prepareStatement(getTask);
            ps.setString(1, "%" + taskNameToSearch + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String taskName = rs.getString(2);
                boolean taskCompleted = rs.getBoolean(3);
                task = new Task(id, taskName, taskCompleted);
                taskList.add(task);
            }
            this.closeDatabase();
        } catch (Exception e) {
            System.err.println(e);
        }
        return taskList;
    }

    void deleteTaskByID(Task task) {
        String deleteTask = "DELETE FROM " + this.tableName + " WHERE " + this.colRowID + " = ?;";
        PreparedStatement ps;
        try {
            this.openDatabase();
            ps = this.databaseConnection.prepareStatement(deleteTask);
            ps.setInt(1, task.getId());
            //ps.setInt(1, taskRowID);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
