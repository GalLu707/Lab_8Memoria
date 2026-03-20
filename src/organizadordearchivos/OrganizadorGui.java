/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package organizadordearchivos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.tree.DefaultTreeCellRenderer;
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

    public OrganizadorGui() {
        String userHome = System.getProperty("user.home");
        raizPath = userHome + File.separator + "Explorador";

        File dirRaiz = new File(raizPath);
        if (!dirRaiz.exists()) {
            dirRaiz.mkdirs();
        }

        currentDirPath = raizPath;

        setTitle("navegador y organizador de Archivos");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(Color_Fondo);

        initComponents();

        setVisible(true);

    }

    private void initComponents() {

          setUpFileTree();
          
        setupContentTable();
        tableModel.addRow(new Object[]{"TEST.txt", "0 KB", "TXT", "HOY"});
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color_Panel);

        JToolBar toolBar = createToolBar();
        northPanel.add(toolBar, BorderLayout.NORTH);

        
        JScrollPane tableScroll = new JScrollPane(fileTable);
        tableScroll.setPreferredSize(new Dimension(700, 400));
        tableScroll.getViewport().setBackground(Color_Fondo);
        
        JScrollPane treeScroll = new JScrollPane(fileTree);
        
        pathLabel = new JLabel(" Carpeta actual: " + raizPath);
        pathLabel.setForeground(Color_Texto);
        pathLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        northPanel.add(pathLabel, BorderLayout.SOUTH);

        
        raizNodo = new DefaultMutableTreeNode(new File(raizPath).getName());
        treeModel = new DefaultTreeModel(raizNodo);
        fileTree = new JTree(treeModel);
        actualizarArbol(new File(raizPath), raizNodo);

        
        fileTree.setBackground(Color_Fondo);
        fileTree.setForeground(Color_Texto);

        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (selectedNode != null) {

                StringBuilder fullPath = new StringBuilder(raizPath);
                Object[] paths = selectedNode.getUserObjectPath();

                for (int i = 1; i < paths.length; i++) {
                    fullPath.append(File.separator).append(paths[i]);
                }

                currentDirPath = fullPath.toString();
                pathLabel.setText(" Ruta Actual: " + currentDirPath);
                
               File carpetaSeleccionada = new File(currentDirPath);
        if (carpetaSeleccionada.exists() && carpetaSeleccionada.isDirectory()) {
            actualizarTabla(carpetaSeleccionada);
        }

                
            }

        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeScroll, tableScroll);
        splitPane.setDividerLocation(250);

        add(northPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        
        actualizarTabla(new File(raizPath));
    }
     
     private JToolBar createToolBar(){
          JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color_Panel);

        JButton btnOrganizar = new JButton("Organizar");
        JButton btnNuevaCarpeta = new JButton("Nueva Carpeta");
        JButton btnRenombrar = new JButton("Renombrar");
        JButton btnCopiar = new JButton("Copiar");
        JButton btnPegar = new JButton("Pegar");
        
        

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
         btnRenombrar.addActionListener(e -> {
             CrearYRenombrar.ejecutarRenombrado(this, currentDirPath, fileTable, fileTree);

             actualizarTabla(new File(currentDirPath));

         });
        toolBar.add(btnOrganizar);
        toolBar.add(btnNuevaCarpeta);
        toolBar.add(btnRenombrar);
        toolBar.add(btnCopiar);
         toolBar.add(btnPegar);
          

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
         
         String[] columnNames= {"Nombre", "Tamaño", "Tipo", "Fecha Modificacion"};
         tableModel= new DefaultTableModel(columnNames, 0){
             @Override
             public boolean isCellEditable(int row, int columna){
                return false; 
             }
         };
         
         fileTable = new JTable(tableModel);
         fileTable.setBackground(Color_Fondo);
         fileTable.setForeground(Color_Texto);
         fileTable.setSelectionBackground(Color_Azul);
         fileTable.setGridColor(Color_Panel);
         fileTable.setRowHeight(25);

     
     }
     
     private void actualizarTabla(File carpeta){
   if (tableModel == null) return;
    
    tableModel.setRowCount(0); 
    File[] archivos = carpeta.listFiles();

    if (archivos != null && archivos.length > 0) {
        for (File f : archivos) {
            String nombre = f.getName();
            String tamaño = f.isDirectory() ? "--" : (f.length() / 1024) + " KB";
            String tipo = f.isDirectory() ? "Carpeta" : getExtension(nombre).toUpperCase();
            
            tableModel.addRow(new Object[]{nombre, tamaño, tipo, "Ver Fecha..."});
        }
    } else {
        System.out.println("La carpeta seleccionada está vacía.");
    }
    
   
    fileTable.revalidate();
    fileTable.repaint();
         
     }
     
     private String getExtension(String fileName) {
    int i = fileName.lastIndexOf('.');
    if (i > 0 && i < fileName.length() - 1) {
        return fileName.substring(i + 1).toLowerCase();
    }
    return "Archivo"; 
}
     
     private void setUpFileTree(){
        File rootFile = new File(raizPath);
        raizNodo = new DefaultMutableTreeNode(rootFile.getName());
        
        actualizarNodosDelArbol(rootFile, raizNodo);
        treeModel = new DefaultTreeModel(raizNodo);
        fileTree = new JTree(treeModel);
      
         fileTree.setBackground(Color_Fondo);
         fileTree.setForeground(Color_Texto);

     DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) fileTree.getCellRenderer();
    renderer.setTextSelectionColor(Color.WHITE);
    renderer.setBackgroundSelectionColor(Color_Azul);
    renderer.setBorderSelectionColor(Color_Azul);
//     fileTree.addTreeSelectionListener(e->{
//         if(selectedNode !=)
//     });
//    
    
    
     }
     private void actualizarNodosDelArbol(File folder, DefaultMutableTreeNode rootNode) {
    File[] files = folder.listFiles();
    if (files == null) return;

    for (File f : files) {
        if (f.isDirectory()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(f.getName());
            rootNode.add(childNode);
         
        }
    }
}
}
