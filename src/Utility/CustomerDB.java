package Utility;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * This class is where data is exchanged for all customer classes and accessing the database
 */
public class CustomerDB {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        allCustomers.clear();
        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                allCustomers.add(new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Division_ID")));

            }
            return allCustomers;
        }
        catch (SQLException e) {
            Logger.getLogger(e.toString());

        }
        return null;
    }

    public static Customer lookupCustomer(int customerID) throws SQLException {
        ObservableList<Customer> customers = CustomerDB.getAllCustomers();
        Customer foundCustomer = null;
        for(Customer customer : customers){
            if(customer.getCustomerID() == customerID){
                foundCustomer = customer;
            }
        }

        return foundCustomer;
    }

    public static ObservableList<Customer> lookupCustomer(String customerName) throws SQLException { // TODO: change void to ObservableList<Product>
        ObservableList<Customer> searchResults = FXCollections.observableArrayList();
        ObservableList<Customer> customers = CustomerDB.getAllCustomers();
        for(Customer customer : customers){
            if(customer.getCustomerName().contains(customerName)){
                searchResults.add(customer);
            }
        }

        return searchResults;

    }
    /**
     * This method is the database interface for deleting customers based on a user's selection in the Customer Main Table View
     * @param id
     * @return
     */
    public static boolean deleteCustomer(int id) {
        /**
         * @see Controller.CustomerMain#sceneDeleteCustomer(ActionEvent) 
         */
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "DELETE FROM appointments WHERE Customer_ID=" + id;
            statement.executeUpdate(query);
            System.out.println(query);
            String queryOne = "DELETE FROM customers WHERE Customer_ID=" + id;
            statement.executeUpdate(queryOne);
            System.out.println(queryOne);


        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    /**
     *This method used to select the customer to be updated in the database (immutable field)
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerPostal
     * @param customerPhone
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID
     * @return
     * @throws SQLException
     */
    public static boolean editCustomer(Integer customerID, String customerName, String customerAddress, String customerPostal, String customerPhone, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, Integer divisionID) throws SQLException {
    /**
     * @see Controller.EditCustomer#editCustomer(ActionEvent) 
     */
        try {
        Statement statement = DBConnection.startConnection().createStatement();
        statement.executeUpdate("UPDATE customers SET  Customer_Name='"+customerName+"', Address='"+customerAddress+"', Postal_Code='"+customerPostal+"', Phone='"+customerPhone+"',Create_Date='"+createDate+"', Created_By='"+createdBy+"', Last_Updated_By='"+lastUpdatedBy+"', Last_Updated_By='"+lastUpdatedBy+"', Division_ID= "+divisionID+" WHERE Customer_ID ="+customerID);

        if(statement.getUpdateCount() >0)
            return true;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public static boolean addCustomer(Integer customerID, String customerName, String customerAddress, String customerPostal, String customerPhone, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, Integer divisionID) {
/**
 * @see Controller.AddCustomer#addCustomer(ActionEvent)
 */
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            statement.executeUpdate("INSERT INTO customers SET Customer_ID='"+customerID+"', Customer_Name='"+customerName+"', Address='"+customerAddress+"', Postal_Code='"+customerPostal+"', Phone='"+customerPhone+"',Create_Date='"+createDate+"', Created_By='"+createdBy+"',Last_Update='"+lastUpdate+"', Last_Updated_By='"+lastUpdatedBy+"', Division_ID= "+divisionID);
            if (statement.getUpdateCount() >0)
                return true;
            }
         catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
}
