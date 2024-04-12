import com.formdev.flatlaf.FlatLightLaf;
import com.github.mgcvale.dbwrapper.DatabaseManager;
import ui.Application;
import ui.LoadingDialog;
import ui.ProgramView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        Application app = new Application("teste");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(1000, 700);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        LoadingDialog thread = new LoadingDialog(app);
        thread.run();
        ProgramView programView = (ProgramView) app.getContentPane();
        programView.populateTablesTable();
        thread.dispose();
    }
}
