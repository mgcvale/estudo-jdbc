package data;

import com.github.mgcvale.dbwrapper.DatabaseManager;
import ui.LoadingDialog;
import util.JFrameUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class TableManager {

    public static void getTablesList(DefaultListModel<String> model, Component parent) {
        model.setSize(0);
        String[] result = DatabaseController.getTables(parent);
        int i=0;
        for(String str : result) {
            model.add(i, str);
            i++;
        }
    }

    private static DefaultTableModel model;
    private static String selectedTable;
    private static Component parent;
    private static String condition;
    private static DatabaseManager dbm;
    private static JDialog dialog;
    public static void getTableTable(DefaultTableModel model1, String selectedTable1, Component parent1, String condition1, boolean showLoadingDialogs) {
        model = model1;
        selectedTable = selectedTable1;
        parent = parent1;
        condition = condition1;
        dbm = DatabaseController.getDatabaseManager();
        model.setRowCount(0);
        model.setColumnCount(0);
        if(showLoadingDialogs)
            dialog = LoadingDialog.createDialog(parent1, "Loading table...");
        SwingUtilities.invokeLater(() ->{
            String[] tableNames;
            LinkedHashMap<Object[], Object[]> tableInfo;
            try {
                tableInfo = dbm.getTable(selectedTable, "", condition, "");
                tableNames = dbm.getColNames(selectedTable);
            } catch(SQLException e) {
                e.printStackTrace();
                model.addColumn("Nenhuma tabela selecionda");
                JOptionPane.showMessageDialog(parent, "Erro ao acessar dados da tabela", "Erro do sistema", JOptionPane.ERROR_MESSAGE);
                if(showLoadingDialogs) dialog.dispose();
                return;
            }
            for(String entry : tableNames) {
                model.addColumn(entry);
            }
            for(Map.Entry<Object[], Object[]> entry : tableInfo.entrySet()) {
                model.addRow(concat(entry.getKey(), entry.getValue()));
            }
            if(showLoadingDialogs) dialog.dispose();
        });
    }

    private static Object[] concat(final Object[] obj1, final Object[] obj2) {
        Object[] result = new Object[obj1.length + obj2.length];
        System.arraycopy(obj1, 0, result, 0, obj1.length);
        System.arraycopy(obj2, 0, result, obj1.length, obj2.length);
        return result;
    }
}
