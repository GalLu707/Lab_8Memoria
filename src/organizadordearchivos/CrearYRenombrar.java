/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package organizadordearchivos;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;

/**
 *
 * @author USER
 */
public class CrearYRenombrar {

     public static void ejecutarRenombrado(JFrame parent, String currentPath, JTable table, JTree tree) {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(parent, "Selecciona un archivo en la tabla.");
            return;
        }

        String nombreViejo = (String) table.getValueAt(fila, 0);
        File archivoViejo = new File(currentPath, nombreViejo);

        String nuevoNombre = JOptionPane.showInputDialog(parent, "Nuevo nombre:", nombreViejo);
        
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            File archivoNuevo = new File(archivoViejo.getParent(), nuevoNombre);
            if (archivoViejo.renameTo(archivoNuevo)) {
               JOptionPane.showMessageDialog(parent, "¡Renombrado!");
            }
        }
    }
    
    public static void crearCarpeta(JFrame parent, String currentPath) {
        String nombre = JOptionPane.showInputDialog(parent, "Nombre de la carpeta:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            File nueva = new File(currentPath, nombre);
            if (nueva.mkdir()) {
                JOptionPane.showMessageDialog(parent, "Carpeta creada.");
            }
        }
    }

    
    
}
