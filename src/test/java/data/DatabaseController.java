package data;

import com.github.mgcvale.dbwrapper.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public abstract class DatabaseController {
    static DatabaseManager dbm = new DatabaseManager("jdbc:mysql://ip/dbname", "user", "password");

    public static String[] getTables(Component parent) {
        String[] result;
        try{
            result = dbm.getTables();
        } catch(SQLException e) {
            e.printStackTrace();
            result = new String[]{};
            JOptionPane.showMessageDialog(parent, "O sistema não conseguiu se conectar com o banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public static String removeEntry(String tableName, String pkName, Object pkValue) {
        boolean isNumber = pkValue instanceof Number;
        try{
            String where = pkName + "=" + (isNumber ? pkValue : "'" + pkValue + "'");
            dbm.deleteFromTable(tableName, where, true);
        } catch (SQLException e) {
            e.printStackTrace();
            if(e.toString().contains("foreign key constraint fails"))
                return "Alguma outra entrada está utilizando a chave estrangeira dessa entrada.";
            if(e.toString().contains("Communications link failure"))
                return "O Sistema não conseguiu acessar o banco de dados.\nVerifique sua conexão com a Internet";
            return "O erro não pode ser determinado";
        }
        return "";
    }

    public static DatabaseManager getDatabaseManager() {
        return dbm;
    }

    public static DatabaseManager createDatabaseManager() {
        return new DatabaseManager("jdbc:mysql://143.106.241.3:3306/cl203001", "cl203001", "cl*20022008");
    }

}
