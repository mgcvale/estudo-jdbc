package ui;

import util.JFrameUtils;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog {
    private Component parent;
    private JDialog objDialog;
    private String text;

    public LoadingDialog(Component parent) {
        this.parent = parent;
        text = "Loading database...";
    }
    public LoadingDialog(Component parent, String text) {
        this(parent);
        this.text = text;
    }

    public void run() {
        SwingUtilities.invokeLater(() ->{
            objDialog = new JDialog();
            objDialog.setContentPane((new JOptionPane(text, JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION, null, new Object[]{}, null)));
            objDialog.pack();
            objDialog.setLocationRelativeTo(parent);
            objDialog.setVisible(true);
        });
    }

    public static JDialog createDialog(Component parent, String text) {
        JDialog dialog = new JDialog();
        dialog.setContentPane(new JOptionPane(text, JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION, null, new Object[]{}, null));
        dialog.pack();
        dialog.setLocation(JFrameUtils.getCenteredPositionForChild((JFrame) parent, dialog));
        dialog.setVisible(true);
        return dialog;
    }

    public void dispose() {
        objDialog.dispose();
    }
}
