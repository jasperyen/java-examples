/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelt;

import DataBaseInfo.DataBaseName.db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jasper-Yen
 */
public class MySQLSyntax {
    static Logger logger;
    
    static{
        logger = Logger.getLogger(DataBaseInfo.DataBaseConn.class.getName());
    }
    
    public static void insertDB (Statement stm, String table, String value) throws SQLException {
        List val = new ArrayList<String>();
        val.add(value);
        insertDB(stm, table, val);
    }
    
    public static void insertDB (Statement stm, String table, List<String> values) throws SQLException {
        String ins = "INSERT INTO "+ table +" VALUES (";
        for (Iterator<String> iter = values.iterator(); iter.hasNext(); ) {
            ins = ins + "\"" + iter.next() + "\"" ;
            if (iter.hasNext())
                ins = ins + ", ";
        }
        ins = ins + ")";
        
        logger.log(Level.INFO, "INSERT DataBase : " + ins);
        stm.executeUpdate(ins);
    }
    
    public static void insertDB (Statement stm, String table, String where, String value) throws SQLException {
        List val = new ArrayList<String>();
        val.add(value);
        List wh = new ArrayList<String>();
        wh.add(where);
        
        insertDB(stm, table, wh, val);
    }
    
    public static void insertDB (Statement stm, String table, List<String> where, List<String> values) throws SQLException {
        String ins = "INSERT INTO "+ table +"(";
        
        for (Iterator<String> iter = where.iterator(); iter.hasNext(); ) {
            ins = ins + iter.next();
            if (iter.hasNext())
                ins = ins + ", ";
        }
        
        ins = ins + ") VALUES (";
        
        for (Iterator<String> iter = values.iterator(); iter.hasNext(); ) {
            ins = ins + "\"" + iter.next() + "\"" ;
            if (iter.hasNext())
                ins = ins + ", ";
        }
        ins = ins + ")";
        
        logger.log(Level.INFO, "INSERT DataBase : " + ins);
        stm.executeUpdate(ins);
    }
 
    public static void updateDB (Statement stm, String table, String where, String before , String after) throws SQLException {
        Map wh = new TreeMap<String, String>();
        wh.put(where, before);
        Map set = new TreeMap<String, String>();
        set.put(where, after);
        updateDB(stm, table, wh, set);
    }
    
    public static void updateDB (Statement stm, String table, Map<String, String> where, Map<String, String> set) throws SQLException {
        String upd = "UPDATE " + table + " SET ";
        
        for (Iterator iter = set.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            upd = upd + entry.getKey() + " = \""+ entry.getValue() +"\" ";
            if (iter.hasNext())
                upd = upd + ", ";
        }
        
        upd = upd + " WHERE ";
        
        for (Iterator iter = where.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            upd = upd + entry.getKey() + " = \""+ entry.getValue() +"\" ";
            if (iter.hasNext())
                upd = upd + "AND ";
        }
        logger.log(Level.INFO, "UPDATE DataBase : " + upd);
        stm.executeUpdate(upd);
    }
    
    public static ResultSet getResultSet(Statement stm, String table) throws SQLException {
        return getResultSet(stm, table, new TreeMap<String, String>());
    }
    
    public static ResultSet getResultSet(Statement stm, String table, String where, String equal) throws SQLException {
        Map wh = new TreeMap<String, String>();
        wh.put(where, equal);
        return getResultSet(stm, table, wh);
    }
    
    public static ResultSet getResultSet(Statement stm, String table, Map<String, String> where) throws SQLException{
        String query = "SELECT * FROM "+ table;
        
        if (!where.isEmpty())
            query = query + " WHERE ";
        
        for (Iterator iter = where.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            query = query + entry.getKey() + " = \""+ entry.getValue() +"\" ";
            if (iter.hasNext())
                query = query + "AND ";
        }
        
        logger.log(Level.INFO, "SELECT DataBase : " + query);
        return stm.executeQuery(query);
    }
    
    public static void inserLog (Statement stm, String log) throws SQLException {
        insertDB(stm, db.log_Table.TABLE_NAME, db.log_Table.Log, log);
    }
    
    public static void deleteDB (Statement stm, String table, String where, String equal)  throws SQLException{
        Map wh = new TreeMap<String, String>();
        wh.put(where, equal);
        deleteDB(stm, table, wh);
    }
    
    public static void deleteDB (Statement stm, String table, Map<String, String> where)  throws SQLException{
        
        String del = "DELETE FROM "+ table +" WHERE ";
        
        for (Iterator iter = where.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            del = del + entry.getKey() + " = \""+ entry.getValue() +"\" ";
            if (iter.hasNext())
                del = del + "AND ";
        }
        
        logger.log(Level.INFO, "DELETE DataBase : " + del);
        stm.executeUpdate(del);
    }
    
}
