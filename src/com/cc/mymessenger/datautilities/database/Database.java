/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Xrysa
 */
public class Database {

    private static Class dbClass;
    private static final String SERVER = "localhost:3306";
    private static final String JDBCURL = "jdbc:mysql://";
    private static final String DATABASE = "messenger";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String OPTIONS = "?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&useSSL=false";
    private static final String FULL_CONNECTION_URL = JDBCURL + SERVER + "/" + DATABASE + OPTIONS;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public Database() {  // default constructor ston opoio kalw methodo gia na kanw register ton driver
        this.registerDriver();
    }

    public void registerDriver() {
        try {
            dbClass = Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return a Connection Object
     */
    public Connection connectToDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(FULL_CONNECTION_URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No working database! \nContact System Administrator.");
            ex.printStackTrace();
        }
        return conn;
    }

    /**
     *
     * @return a Statement Object
     */
    public Statement createStatement() {
        try {
            return connectToDB().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    public String preparedInsertQuery(String table, LinkedHashMap<String, Object> DAOMap) {
        String tablePath = "`" + DATABASE + "`.`" + table + "`";

        StringBuilder query = new StringBuilder()
                .append("INSERT INTO ")
                .append(tablePath)
                .append(" (");
        StringBuilder values = new StringBuilder()
                .append("(");

        int i = 0;
        for (String key : DAOMap.keySet()) {
            i++;
            if (i == 1) {
                continue;
            }
            query.append(key);
            values.append("?");
            if (i < DAOMap.size()) {
                query.append(", ");
                values.append(", ");
            }
        }
        query.append(") VALUES ");
        values.append(");");
        String svalues = values.toString();
        query.append(svalues).toString();
        String completeInsertQuery = query.toString();

        //System.out.println(completeInsertQuery);
        return completeInsertQuery;
    }

    public int insert(String table, LinkedHashMap<String, Object> DAOMap) {

        String completeInsertQuery = preparedInsertQuery(table, DAOMap);

        PreparedStatement statement = null;
        try {
            statement = connectToDB().prepareStatement(completeInsertQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        int num = 0, i = 0;

        for (String key : DAOMap.keySet()) {
            i++;
            if (i == 1) {
                continue;
            }
            num++;
            try {
                statement.setString(num, DAOMap.get(key).toString());
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        int rowsInserted = 0;
        try {
            rowsInserted = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return rowsInserted;
    }

    public String preparedUpdateQuery(String table, LinkedHashMap<String, Object> DAOMap) {
        String tablePath = "`" + DATABASE + "`.`" + table + "`";

        StringBuilder query = new StringBuilder()
                .append("UPDATE ")
                .append(tablePath)
                .append(" SET ");

        int i = 0;

        for (String key : DAOMap.keySet()) {
            i++;
            if (i == 1) {
                continue;
            }
            query.append(key)
                    .append(" = ?");
            if (i < DAOMap.size()) {
                query.append(", ");
            }
        }
        query.append(" WHERE `id`= ?");

        String completeUpdateQuery = query.toString();

        //System.out.println(completeUpdateQuery);
        return completeUpdateQuery;
    }

    public int update(String table, LinkedHashMap<String, Object> DAOMap) {

        String completeUpdateQuery = preparedUpdateQuery(table, DAOMap);

        PreparedStatement statement = null;
        try {
            statement = connectToDB().prepareStatement(completeUpdateQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        int num = 0, i = 0;
        String id = null;
        for (String key : DAOMap.keySet()) {
            i++;
            if (i == 1) {
                id = DAOMap.get(key).toString();
                continue;
            }
            num++;
            try {
                statement.setString(num, DAOMap.get(key).toString());
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
        try {
            statement.setString(++num, id);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        int rowsUpdated = 0;
        try {
            rowsUpdated = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return rowsUpdated;
    }

    public int deleteById(String table, int id) {
        String deleteQuery = "DELETE FROM `" + DATABASE + "`.`" + table + "` WHERE `id`='" + id + "';";
        Statement statement = null;
        int result = 0;
        try {
            statement = connectToDB().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        result = executeUpdate(statement, deleteQuery);
        return result;
    }
    
    public ResultSet preparedSelect(String selectQuery, ArrayList<String> parameters){
        PreparedStatement statement = null;
        try {
            statement = connectToDB().prepareStatement(selectQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        for (int i=0; i < parameters.size(); i++){
            try {
                statement.setString(i+1, parameters.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        ResultSet result = null ;
        try {
            result = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return result;
    }
    
    public ResultSet Select(String query) {
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connectToDB().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ResultSet SelectAllById(String table, int id) {
        String query = "Select * from `" + DATABASE + "`.`" + table + "` WHERE `id`='" + id + "';";
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connectToDB().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int insertOrUpdate(String query) {
        int result;
        Statement statement = null;
        try {
            statement = connectToDB().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        result = executeUpdate(statement, query);
        return result;
    }

    private ResultSet executeQuery(Statement statement, String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    private Integer executeUpdate(Statement statement, String query) {
        try {
            return statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean connectAndTest() {
        boolean isConnectionOK = false;
        String query;
        query = "SELECT * FROM `messenger`.`users` WHERE `id` ='1';";
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connectToDB().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            isConnectionOK = result.first();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isConnectionOK;
    }
}
