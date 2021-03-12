package Utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used so other classes can query the database
 */
public class DBQuery {
    public static Statement statement;
    //Create Statement Object
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    //Return Statement Object
    public static Statement getStatement()
    {
        return statement;
    }




    }

