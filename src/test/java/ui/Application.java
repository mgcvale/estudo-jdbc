package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Application extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem exportMenuItem;
    private JCheckBoxMenuItem showLoadingDialogsMenuItem, showConfirmDialogsMenuItem;
    private ProgramView programView;

    public Application(String title) {
        super(title);
        init();
    }

    private void init(){
        programView = new ProgramView(this);
        initMenu();
        setContentPane(programView);
        addListeners();
    }

    private void initMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        exportMenuItem = new JMenuItem("exportar como");
        showLoadingDialogsMenuItem = new JCheckBoxMenuItem("Mostrar diálogos de carregamento", true);
        showConfirmDialogsMenuItem = new JCheckBoxMenuItem("Mostrar diálogos de confirmação", true);


        fileMenu.add(exportMenuItem);
        fileMenu.add(showLoadingDialogsMenuItem);
        fileMenu.add(showConfirmDialogsMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void addListeners() {
        showConfirmDialogsMenuItem.addActionListener(e -> {
            programView.setShowConfirmDialogs(showConfirmDialogsMenuItem.isSelected());
            System.out.println("LSAKFJLSADJK");
        });
        showLoadingDialogsMenuItem.addItemListener(e -> {
            programView.setShowLoadingDialogs(showLoadingDialogsMenuItem.isSelected());
            System.out.println("LSAKFJLSADJK");
        });
    }

}
