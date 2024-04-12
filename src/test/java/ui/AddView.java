package ui;

import data.EntryChangeListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddView extends JFrame implements ActionListener {
    private JPanel dataPanel;
    private JPanel[] cards;
    private JLabel[] dataLabels;
    private JTextField[] dataTextFields;
    private int dataQuantity;
    private Object[] data, newData;
    private String[] dataName, newDataName;
    private int[] dataIndexesNoPK;
    private int currentIndex = 0, pkIndex;
    private EntryChangeListener listener;
    private boolean isEditing = false;

    public AddView(Object[] data, String[] dataName, int pkIndex, EntryChangeListener listener) {
        super("Add/Edit entry");
        this.data = data;
        this.dataName = dataName;
        dataQuantity = data.length;
        this.listener = listener;
        this.pkIndex = pkIndex;
        generateDataIndexesNoPK();
        initComponents();
        layComponents();
        setSize(1000, 700);
        setVisible(true);
    }

    private void generateDataIndexesNoPK() {
        dataIndexesNoPK = new int[dataQuantity-1];
        int j=0;
        for(int i=0; i<data.length; i++) {
            if(i==pkIndex)
                continue;
            dataIndexesNoPK[j] = i;
            j++;
        }
    }

    private void initComponents() {
        dataPanel = new JPanel(new CardLayout());
        dataLabels = new JLabel[dataQuantity];
        dataTextFields = new JTextField[dataQuantity];
        for(int i=0; i<dataQuantity; i++) {
            dataLabels[i] = new JLabel(dataName[i]);
            dataTextFields[i] = new JTextField(data[i].toString());
            if(!data[i].toString().isEmpty())
                isEditing = true;
        }
        dataTextFields[pkIndex].setEnabled(false);
    }

    private void layComponents() {
        layDataComponents();
        setLayout(new GridLayout(1, 1));
        add(dataPanel);
    }

    private void layDataComponents() {
        int panelQuantity = (int)  Math.ceil((double) dataQuantity / 5);
        cards = new JPanel[panelQuantity];
        System.out.println("data quantity: " + dataQuantity);
        System.out.println("panel quantity: " + panelQuantity);

        for(int i=0; i<panelQuantity; i++){
            cards[i] = new JPanel(new GridBagLayout());
            layCardComponents(cards[i], i, i==panelQuantity-1);
            dataPanel.add(cards[i], Integer.toString(i));
        }
    }

    private void layCardComponents(JPanel card, int index, boolean isLastCard) {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        int componentQuantity = 5;
        if(isLastCard && index != 0) componentQuantity = (int) Math.ceil((double)dataQuantity/5);

        System.out.println("component quantity: " + componentQuantity + "  is last card: " + isLastCard);

        //firstly, lay stuff onto the bottom panel

        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(new NextBtnListener());
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(new BackBtnListener());
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new CancelBtnListener());

        if(index==0 && !isLastCard) {
            bottomPanel.add(new JLabel());
            bottomPanel.add(new JLabel());
            bottomPanel.add(new JLabel());
            bottomPanel.add(cancelBtn);
            bottomPanel.add(nextBtn);
        } else {
            bottomPanel.add(index!=0 ? backBtn : new JLabel());
            bottomPanel.add(new JLabel());
            bottomPanel.add(new JLabel());
            bottomPanel.add(cancelBtn);
            if(!isLastCard){
                bottomPanel.add(nextBtn);
            } else{
                JButton applyBtn = new JButton("Apply");
                applyBtn.addActionListener(this);
                bottomPanel.add(applyBtn);
            }
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty=0.3;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridwidth = componentQuantity;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        card.add(new JLabel("Adicao/atualizacao de entrada", SwingConstants.CENTER), gbc);

        gbc.fill = GridBagConstraints.BOTH;
        card.add(new JLabel(), gbc);


        gbc.weighty=0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        gbc.gridy++;
        for(int i=0; i<componentQuantity; i++){
            card.add(dataLabels[index*5 + i], gbc);
            gbc.gridx++;
        }

        gbc.gridx=0; gbc.gridy++;
        for(int i=0; i<componentQuantity; i++) {
            card.add(dataTextFields[index*5 + i], gbc);
            gbc.gridx++;
        }

        gbc.weighty=0.3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.gridx=0;
        gbc.gridwidth = componentQuantity;
        gbc.anchor = GridBagConstraints.SOUTH;
        card.add(new JLabel(), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(bottomPanel, gbc);
    }

    private class BackBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentIndex--;
            CardLayout cl = (CardLayout) dataPanel.getLayout();
            cl.show(dataPanel, Integer.toString(currentIndex));
        }
    }

    private class NextBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentIndex++;
            CardLayout cl = (CardLayout) dataPanel.getLayout();
            cl.show(dataPanel, Integer.toString(currentIndex));
        }
    }

    private class CancelBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        getNewData();
        listener.entryChanged(newDataName, newData, !isEditing, dataLabels[pkIndex].getText(), dataTextFields[pkIndex].getText(), this);
    }

    private void getNewData() {
        newData = new Object[dataQuantity-1];
        newDataName = new String[dataQuantity-1];
        int j=0;
        for(int i : dataIndexesNoPK){
            newData[j] = dataTextFields[i].getText();
            newDataName[j] = dataLabels[i].getText();
            j++;
        }
    }
}