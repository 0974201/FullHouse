package fullhousedb21;

import java.sql.Connection;
import java.time.LocalDate;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adeliae
 */
public class Database {
    
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement pStatement;
    private static ResultSet rs;

    public Database() {
    }
    
    public DefaultTableModel getFromDatabase(String query) throws SQLException {
            connection = SimpleDataSourceV2.getConnection();
            statement = connection.createStatement();
            
            rs = statement.executeQuery (query);
            
            ResultSetMetaData metaData = rs.getMetaData();
            Vector<String> columnNames = new Vector<String>();
            
            int columnCount = metaData.getColumnCount();
            
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }
            
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }
            
            connection.close();
            
        return new DefaultTableModel(data, columnNames); //tabel maken om alles in op te slaan;
    }
    
    public void insertSpeler(String naam, String adres, String postcode, String woonplaats, int telefoonnr, String email, boolean bekend) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        
        String query = "INSERT INTO Spelers (naam, adres, postcode, woonplaats, telefoonnr, email, bekend) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pStatement.setString(1, naam);
        pStatement.setString(2, adres);
        pStatement.setString(3, postcode);
        pStatement.setString(4, woonplaats);
        pStatement.setInt(5, telefoonnr);
        pStatement.setString(6, email);
        
        if (bekend) {
            pStatement.setString(7, "Ja");
        } else {
            pStatement.setString(7, "Nee");
        }
        
        pStatement.executeUpdate();
    }
    
    public void insertToernooi(String naam, Date date, String plaats, String soort, int rondes, int max_spelers) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        java.sql.Date datum = new java.sql.Date(date.getTime()); //zet java datum om naar sql datum
        
        String query = "INSERT INTO Toernooi (naam, datum, plaats, soort, rondes, max_spelers) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pStatement.setString(1, naam);
        pStatement.setDate(2, datum);
        pStatement.setString(3, plaats);
        pStatement.setString(4, soort);
        pStatement.setInt(5, rondes);
        pStatement.setInt(6, max_spelers);
        
        pStatement.executeUpdate();
    }
    
    public void insertMClass(String naam, Date date, String soort, String niveau, int max_spelers ,int min_rating) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        java.sql.Date datum = new java.sql.Date(date.getTime()); 
        
        String query = "INSERT INTO Masterclass (naam, datum, soort, niveau, max_spelers, min_rating) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pStatement.setString(1, naam);
        pStatement.setDate(2, datum);
        pStatement.setString(3, soort);
        pStatement.setString(4, niveau);
        pStatement.setInt(5, max_spelers);
        pStatement.setInt(6, min_rating);
        
        pStatement.executeUpdate();
    }
    
    public void insertInschrijving(String speler, String toernooi, int inleggeld) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        
        String query = "INSERT INTO InschrijvingToernooi (speler, toernooi, inleggeld) "
                + "VALUES (?, ?, ?)";
        
        pStatement = connection.prepareStatement(query);
        pStatement.setString(1, speler);
        pStatement.setString(2, toernooi);
        pStatement.setInt(3, inleggeld);
        
        pStatement.executeUpdate();
    }
    
    public void insertInschrijvingM(String speler, String masterclass, int inschrijfgeld) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        
        String query = "INSERT INTO InschrijvingMasterclass (speler, masterclass, inschrijfgeld) "
                + "VALUES (?, ?, ?)";
        
        pStatement = connection.prepareStatement(query);
        pStatement.setString(1, speler);
        pStatement.setString(2, masterclass);
        pStatement.setInt(3, inschrijfgeld);
        
        pStatement.executeUpdate();
    }
    
    public void updateSpeler(String naam, String adres, String postcode, String woonplaats, int telefoonnr, String email, String bekend, int rating, int spelerid) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        
        String query = "UPDATE Spelers "
                + "SET naam = ?, adres = ?, postcode = ?, woonplaats = ?, telefoonnr = ?, email = ?, bekend = ?, rating = ? "
                + "WHERE spelerid = ?";
        
        pStatement = connection.prepareStatement(query);
        pStatement.setString(1, naam);
        pStatement.setString(2, adres);
        pStatement.setString(3, postcode);
        pStatement.setString(4, woonplaats);
        pStatement.setInt(5, telefoonnr);
        pStatement.setString(6, email);
        pStatement.setString(7, bekend);
        pStatement.setInt(8, rating);
        pStatement.setInt(9, spelerid);
        
        pStatement.executeUpdate();
    }
    
    public void updateToernooi(String naam, Date date, String plaats, String soort, int rondes, int max_spelers, int toernooiid) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        java.sql.Date datum = new java.sql.Date(date.getTime()); 
        
        String query = "UPDATE Toernooi "
                + "SET naam = ?, datum = ?, plaats = ?, soort = ?, rondes = ?, max_spelers = ? " 
                + "WHERE toernooiid = ?";
        
        pStatement = connection.prepareStatement(query);
        pStatement.setString(1, naam);
        pStatement.setDate(2, datum);
        pStatement.setString(3, plaats);
        pStatement.setString(4, soort);
        pStatement.setInt(5, rondes);
        pStatement.setInt(6, max_spelers);
        pStatement.setInt(7, toernooiid);
        
        pStatement.executeUpdate();
    }   
    
    public void updateMClass(String naam, Date date, String soort, String niveau, int max_spelers ,int min_rating, int masterclassid) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        java.sql.Date datum = new java.sql.Date(date.getTime()); 
        
        String query = "UPDATE Masterclass "
                + "SET naam = ?, datum = ?, soort = ?, niveau = ?, max_spelers = ?, min_rating = ? " 
                + "WHERE masterclassid = ?";
        
        pStatement = connection.prepareStatement(query);
        pStatement.setString(1, naam);
        pStatement.setDate(2, datum);
        pStatement.setString(3, soort);
        pStatement.setString(4, niveau);
        pStatement.setInt(5, max_spelers);
        pStatement.setInt(6, min_rating);
        pStatement.setInt(7, masterclassid);
        
        pStatement.executeUpdate();
    }

    public void verwijderRij(String query) throws SQLException {
        connection = SimpleDataSourceV2.getConnection();
        
        pStatement = connection.prepareStatement(query);
        pStatement.executeUpdate();
    }

}
