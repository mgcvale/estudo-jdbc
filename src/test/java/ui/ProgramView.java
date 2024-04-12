package ui;
import data.DatabaseController;
import data.EntryChangeListener;

import data.TableManager;
import util.ColorUtils;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;

public class ProgramView extends JPanel {
    private JScrollPane tablePane;
    private JList<String> tablesList;
    private DefaultListModel<String> tablesListModel;
    private JTable tableTbl;
    private DefaultTableModel tableModel;
    private String selectedtable = "";
    private JButton addEntryBtn, deleteEntryBtn, editEntryBtn;
    private JPanel buttonsPanel;
    private JLabel selectedTableLbl;
    private Component parent;
    private boolean showLoadingDialogs = true, showConfirmDialogs = true;
    private int selectedRowIndex;


    class UneditableTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public ProgramView(Component parent) {
        this.parent = parent;
        initComponents();
        layComponents();
        addListeners();
    }

    public void setShowLoadingDialogs(boolean showLoadingDialogs) {
        this.showLoadingDialogs = showLoadingDialogs;
    }

    public void setShowConfirmDialogs(boolean showConfirmDialogs) {
        this.showConfirmDialogs = showConfirmDialogs;
    }

    private void initComponents() {
        initTables();

        //addEntryButton
        addEntryBtn = new JButton("Adicionar");

        //deleteEntryBtn
        deleteEntryBtn = new JButton("Deletar");
        deleteEntryBtn.setEnabled(false);

        //editEntryBtn
        editEntryBtn = new JButton("Editar");
        editEntryBtn.setEnabled(false);

        //buttonsPanel
        buttonsPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        //selectedTableLbl
        selectedTableLbl = new JLabel("Nenhuma tabela selecionada!", SwingConstants.CENTER);
    }

    private void initTables() {
        DefaultTableCellRenderer centeredRenderer = new DefaultTableCellRenderer();
        centeredRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        Color bgColor = UIManager.getColor("Panel.background");
        Color borderColor = UIManager.getColor("Panel.background");
        if(ColorUtils.isColorDark(bgColor)) {
            bgColor = bgColor.darker();
            borderColor = ColorUtils.saturate(borderColor, 50).brighter();
        } else {
            bgColor = bgColor.brighter();
            borderColor = ColorUtils.saturate(borderColor, 50).darker();
        }

        //table table (lol)
        tableModel = new UneditableTableModel();
        tableModel.addColumn("No selected table!");
        tableTbl = new JTable(tableModel);
        //center table header text
        JLabel headerLabels = (JLabel) tableTbl.getTableHeader().getDefaultRenderer();
        headerLabels.setHorizontalAlignment(SwingConstants.CENTER);
        tablePane = new JScrollPane(tableTbl);
        tablePane.getViewport().setBackground(bgColor);
        tablePane.setBorder(new LineBorder(borderColor, 1));

        tablesListModel = new DefaultListModel<>();
        tablesList = new JList(tablesListModel);
        tablesList.setBorder(new LineBorder(borderColor, 1));
        tablesList.setBackground(bgColor);
        //center list text
        DefaultListCellRenderer listRenderer = (DefaultListCellRenderer) tablesList.getCellRenderer();
        listRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void layComponents() {
        //buttonsPanel
        buttonsPanel.add(addEntryBtn);
        buttonsPanel.add(deleteEntryBtn);
        buttonsPanel.add(editEntryBtn);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel label = new JLabel("Tabelas", SwingConstants.CENTER);
        add(label, gbc);

        gbc.insets.bottom = 10;
        gbc.insets.top = 0;
        gbc.gridy++;
        gbc.weighty = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(tablesList, gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.gridheight = 1;
        gbc.gridy = 0; gbc.gridx++;
        gbc.weightx = 1;
        add(selectedTableLbl, gbc);

        gbc.weighty = 0.95;
        gbc.insets.top = 0;
        gbc.insets.bottom = 10;
        gbc.gridheight = 1;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        add(tablePane, gbc);

        gbc.weighty = 0.05;
        gbc.gridy++;
        add(buttonsPanel, gbc);
    }

    private void addListeners() {
        tablesList.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            String table = tablesList.getSelectedValue();
            if(tablesList.getSelectedIndex() != -1 && !table.equals(selectedtable)) {
                selectedtable = table;
                selectedTableLbl.setText("Tabela " + selectedtable + ":");
                TableManager.getTableTable(tableModel, selectedtable, parent, "", showLoadingDialogs);
            }
        });
        tableTbl.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            selectedRowIndex = tableTbl.getSelectedRow();
            if(selectedRowIndex!=-1) {
                deleteEntryBtn.setEnabled(true);
                editEntryBtn.setEnabled(true);
            } else {
                deleteEntryBtn.setEnabled(false);
                editEntryBtn.setEnabled(false);
            }
        });
        deleteEntryBtn.addActionListener(actionEvent -> {
            if(showConfirmDialogs)
                if(JOptionPane.showConfirmDialog(parent, "Você deseja mesmo deletar essa(s) entrada(s)?", "Confirmação", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                    return;
            String pkName = tableModel.getColumnName(0);
            System.out.println(tableTbl.getSelectedRowCount());
            if(tableTbl.getSelectedRowCount()<2) {
                Object pkValue = tableModel.getValueAt(tableTbl.getSelectedRow(), 0);
                String res = DatabaseController.removeEntry(selectedtable, pkName, pkValue);
                if (!res.isEmpty()) {
                    JOptionPane.showMessageDialog(parent, "Erro ao deletar a entrada.\n" + res, "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    JOptionPane.showMessageDialog(parent, "Entrada deletada com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                String res = "";
                for (int i : tableTbl.getSelectedRows()) {
                    Object pkValue = tableModel.getValueAt(i, 0);
                    String tempRes = DatabaseController.removeEntry(selectedtable, pkName, pkValue);
                    if (!tempRes.isEmpty()) {
                        res = tempRes;
                    }
                }
                if (!res.isEmpty()) {
                    JOptionPane.showMessageDialog(parent, "Erro ao deletar uma ou mais entradas.\n" + res, "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    JOptionPane.showMessageDialog(parent, "Entradas deletadas com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            TableManager.getTableTable(tableModel, selectedtable, parent, "", showLoadingDialogs);
        });
        addEntryBtn.addActionListener(actionEvent -> {
            try {
                String[] dataName = DatabaseController.getDatabaseManager().getColNames(selectedtable);
                Object[] data = new Object[dataName.length];
                for(int i=0; i<data.length; i++) {
                    data[i] = "";
                }
                new AddView(data, dataName, 0, new EntryUpdatedListener());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        editEntryBtn.addActionListener(actionEvent -> {
            try {
                String[] dataName = DatabaseController.getDatabaseManager().getColNames(selectedtable);
                Object[] data = new Object[dataName.length];
                for (int i = 0; i < data.length; i++) {
                    data[i] = tableModel.getValueAt(selectedRowIndex, i);
                }
                new AddView(data, dataName, 0, new EntryUpdatedListener());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void populateTablesTable() {
        TableManager.getTablesList(tablesListModel, this);
    }

    private class EntryUpdatedListener implements EntryChangeListener {
        @Override
        public void entryChanged(String[] colNames, Object[] values, boolean isNewValue, String pkName, Object pkValue, Frame instance){
            try {
                if (isNewValue) {
                    DatabaseController.getDatabaseManager().insertInto(selectedtable, colNames, values);
                }else {
                    DatabaseController.getDatabaseManager().updateTable(selectedtable, colNames, values, pkName + "=" + pkValue.toString());
                }
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(parent, "Ocorreu um erro ao atualizar ou adicionar uma entrada na tabela. \n Informacoes do erro:\n" + e.toString(), "Erro", JOptionPane.OK_OPTION);
                instance.requestFocus();
                return;
            }
            instance.dispose();
            TableManager.getTableTable(tableModel, selectedtable, parent, "", showLoadingDialogs);
        }
    }
}
