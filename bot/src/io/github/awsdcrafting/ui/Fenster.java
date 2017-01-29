package io.github.awsdcrafting.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Fenster extends JFrame 
{
    private static final long serialVersionUID = 155551L;

    private JMenuBar menuBar;
    private JPanel contentPane;

    public Fenster(String title, int width, int height)
    {
        this.setTitle(title);
        this.setSize(width, height);
        generateMenu();
        this.setJMenuBar(menuBar);

        contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(width,height));
        contentPane.setBackground(new Color(225,245,255));
    }

    public void addToContentPane(JComponent component)
    {
        contentPane.add(component);
    }

    public void initialize()
    {
        this.add(contentPane);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    private void generateMenu()
    {
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu tools = new JMenu("Tools");
        JMenu help = new JMenu("Help");

        JMenuItem exit = new JMenuItem("Exit   ");
        exit.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                    System.exit(0);}});
        JMenuItem preferences = new JMenuItem("Preferences   ");
        preferences.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                    System.out.println("Hier ist noch nichts.");}});
        JMenuItem about = new JMenuItem("About   ");
        about.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                    System.out.println("Dies ist ein Weltraumspiel.");
                    System.out.println("Version: ");}});

        file.addSeparator();
        file.add(exit);
        tools.add(preferences);
        help.add(about);

        menuBar.add(file);
        menuBar.add(tools);
        menuBar.add(help);
    }

}
