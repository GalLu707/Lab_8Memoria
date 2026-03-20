/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package organizadordearchivos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author USER
 */
public class OrganizadorGui extends JFrame {
    
    private String raizPath;
    private String currentDirPath;
    
    
    private final Color Color_Fondo = new Color(195,195,195);
    private final Color Color_Panel = new Color(159,159,159);
    private final Color Color_Azul = new Color(190, 196, 255);
    private final Color Color_Texto = new Color(0,0,0);
    
    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode raizNodo;
    private DefaultTableModel tableModel;
    private JTable fileTable;
    private JLabel pathLabel;
    
    
    public OrganizadorGui(){
        String userHome = System.getProperty("user.home");
        raizPath = userHome+ File.separator + "Explorador";
        
        File dirRaiz = new File(raizPath);
        if(!dirRaiz.exists()) dirRaiz.mkdirs();
    
        currentDirPath = raizPath;
        
        setTitle("navegador y organizador de Archivos");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5,5));
        getContentPane().setBackground(Color_Fondo);
        
        initComponents();
        
        setVisible(true);
    
    
    }
    
    
     private void initComponents(){
         
         JPanel northPanel = new JPanel(new BorderLayout());
         northPanel.setBackground(Color_Panel);
         
         JToolBar toolBar = createToolBar();
         northPanel.add(toolBar, BorderLayout.NORTH);
         
         pathLabel = new JLabel(" Carpeta actual: " + raizPath);
        pathLabel.setForeground(Color_Texto);
        pathLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        northPanel.add(pathLabel, BorderLayout.SOUTH);

        raizNodo = new DefaultMutableTreeNode(new File(raizPath).getName());
        treeModel = new DefaultTreeModel(raizNodo);
        fileTree = new JTree(treeModel);
        actualizarArbol(new File(raizPath), raizNodo);
        
        // Estilizar Árbol
        fileTree.setBackground(Color_Fondo);
        fileTree.setForeground(Color_Texto);

         fileTree.addTreeSelectionListener(e ->{
             DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (selectedNode != null) {
               
                pathLabel.setText(" Ruta: " + selectedNode.toString());
            }   
             
         });
          JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                new JScrollPane(fileTree), new JScrollPane(new JList())); // JList temporal
        splitPane.setDividerLocation(250);

        add(northPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
         
     }
     
     private JToolBar createToolBar(){
          JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color_Panel);

        JButton btnOrganizar = new JButton("Organizar");
        JButton btnNuevaCarpeta = new JButton("Nueva Carpeta");
        JButton btnRenombrar = new JButton("Renombrar");

        btnOrganizar.addActionListener(e -> ejecutarOrganizacion());

        btnNuevaCarpeta.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre de la carpeta:");
            if (nombre != null && !nombre.isEmpty()) {
                File nueva = new File(currentDirPath, nombre);
                if (nueva.mkdir()) {
                    JOptionPane.showMessageDialog(this, "Carpeta creada");
                    
                }
            }
        });

        toolBar.add(btnOrganizar);
        toolBar.add(btnNuevaCarpeta);
        toolBar.add(btnRenombrar);

        return toolBar;
         
     }
     
     private void ejecutarOrganizacion(){
         File dir = new File(currentDirPath);
         File[] archivos = dir.listFiles();
         
         if(archivos == null) return;
         
         for(File f : archivos){
             String nombre = f.getName().toLowerCase();
             String carpetaDestino = "";
              if (nombre.endsWith(".jpg") || nombre.endsWith(".png")) carpetaDestino = "Imagenes";
                else if (nombre.endsWith(".pdf") || nombre.endsWith(".txt")) carpetaDestino = "Documentos";
                else if (nombre.endsWith(".mp3")) carpetaDestino = "Musica";

                if (!carpetaDestino.isEmpty()) {
                    File nuevaCarpeta = new File(currentDirPath, carpetaDestino);
                    if (!nuevaCarpeta.exists()) nuevaCarpeta.mkdir();
                    
                    try {
                        Files.move(f.toPath(), new File(nuevaCarpeta, f.getName()).toPath(), 
                                   StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        
        JOptionPane.showMessageDialog(this, "¡Archivos Organizados!");

         
         
     }
     
     private void actualizarArbol(File dir, DefaultMutableTreeNode nodo){
         File[] archivos = dir.listFiles();
         if(archivos!= null){
             for(File f : archivos){
                 if(f.isDirectory()){
                     DefaultMutableTreeNode hijo= new DefaultMutableTreeNode(f.getName());
                     nodo.add(hijo);
                     actualizarArbol(f, hijo);
                 }
             }
         }
         
         
         
     }
     
     private void setupContentTable(){
     
     
     
     }
     
     
     
     
     
}
