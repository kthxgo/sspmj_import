/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bundestagdataimport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeff
 */
public class DBConnection {
    
       private Connection connection;

    public boolean connectToMysql(String host, String database, String user, String password) {
	try {
	    Class.forName("com.mysql.jdbc.Driver").newInstance();
	    String connectionCommand = "jdbc:mysql://" + host + "/" + database + "?user=" + user + "&password=" + password;
	    connection = DriverManager.getConnection(connectionCommand);
	    return true;
	} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex) {
	    ex.printStackTrace();
	}
	return false;
    }
    
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getAbgeordneterIDByName(String name) {
	try {
	    CallableStatement cs = connection.prepareCall("CALL getAbgeordneterIDByName(?)");
	    cs.setNString(1, name);
	    ResultSet resultSet = cs.executeQuery();
	    if (resultSet != null && resultSet.first()) {
		return resultSet.getInt(1);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return -1;
    }
    
    public int getParteiIDByName(String name) {
	try {
	    CallableStatement cs = connection.prepareCall("CALL getParteiIDByName(?)");
	    cs.setNString(1, name);
	    ResultSet resultSet = cs.executeQuery();
	    if (resultSet != null && resultSet.first()) {
		return resultSet.getInt(1);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return -1;
    }
    
    public int getBundeslandIDByName(String name) {
	try {
	    CallableStatement cs = connection.prepareCall("CALL getBundeslandIDByName(?)");
	    cs.setNString(1, name);
	    ResultSet resultSet = cs.executeQuery();
	    if (resultSet != null && resultSet.first()) {
		return resultSet.getInt(1);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return -1;
    }
    
    public void writeToAbgeordneter(String vorname, String nachname, String bild, String geschlecht, String konfession, String familienstand,
                                    int kinder, String wahlkreis, String fraktion, String geburtsdatum, String aktuellerberuf, String berufe,
                                    String addbundestag, String addwahlkreis, String email, String website, int parteiID, int bundeslandID) {
        
        Statement stmt;
        try {
            stmt = connection.createStatement();

            String sql = "INSERT INTO abgeordneter (vorname, nachname, bild, geschlecht, konfession, familienstand, kinder, wahlkreis, " +
                         "fraktion, geburtsdatum, aktuellerberuf, berufe, addbundestag, addwahlkreis, email, website, parteiID, bundeslandID)" +
                         "VALUES ('" + vorname + "', '" + nachname + "', '" + bild + "', '" + geschlecht + "', '" + konfession + "', '" + familienstand + "'," +
                         "'" + kinder + "', '" + wahlkreis + "', '" + fraktion + "', '" + geburtsdatum + "', '" + aktuellerberuf + "', '" + berufe + "'," + 
                         "'" + addbundestag + "', '" + addwahlkreis + "', '" + email + "', '" + website + "', " + parteiID + ", " + bundeslandID + ")";
            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public void writeToPartei(String kuerzung, String name, String parteichef, String fraktion, String fraktionsvorsitzender, String wappen, int mitgliederzahl) {
        
        Statement stmt;
        try {
            stmt = connection.createStatement();

            String sql = "INSERT INTO partei (kuerzung, name, parteichef, fraktion, fraktionsvorsitzender, wappen, mitgliederzahl)" +
                         "VALUES ('" + kuerzung + "', '" + name + "', '" + parteichef + "', '" + fraktion + "'," +
                         " '" + fraktionsvorsitzender + "', '" + wappen + "', " + mitgliederzahl + ")";
            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public void writeToBundesland(String name, String kurzname, String hauptstadt, String wappen) {
        
        Statement stmt;
        try {
            stmt = connection.createStatement();

            String sql = "INSERT INTO Bundesland (name, kurzname, hauptstadt, wappen)" +
                         "VALUES ('" + name + "', '" + kurzname + "', '" + hauptstadt + "', '" + wappen + "')";
            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
}
