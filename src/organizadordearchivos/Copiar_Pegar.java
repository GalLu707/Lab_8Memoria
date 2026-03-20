package organizadordearchivos;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Copiar_Pegar {
    
    private final Component parent;
    
    private final ListaEnlazada clipboard = new ListaEnlazada();
    
    public Copiar_Pegar(Component parent) {
        this.parent = parent;
    }
    
    public void copyFiles(java.util.List<File> files) {
        if (files == null || files.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "Selecciona los archivos o carpetas a copiar.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        clipboard.clear();
        clipboard.addAll(files);
        
        JOptionPane.showMessageDialog(parent,
                files.size() + " elemento(s) preparado(s) para copiar.",
                "Portapapeles", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public boolean pasteFiles(String currentDirPath) {
        if (clipboard.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "El portapapeles está vacío.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        File targetDir = new File(currentDirPath);
        if (!targetDir.isDirectory()) {
            JOptionPane.showMessageDialog(parent,
                    "La ubicación de destino no es una carpeta válida.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        boolean success = true;
        
        for (File sourceFile : clipboard) {
            try {
                File destFile = new File(targetDir, sourceFile.getName());
                
                if (sourceFile.isDirectory()) {
                    recursiveCopy(sourceFile.toPath(), destFile.toPath());
                } else {
                    Files.copy(sourceFile.toPath(), destFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                
            } catch (IOException e) {
                success = false;
                JOptionPane.showMessageDialog(parent,
                        "Error al copiar " + sourceFile.getName() + ": " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (success) {
            JOptionPane.showMessageDialog(parent,
                    "Elementos copiados con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    public boolean hasFiles() {
        return !clipboard.isEmpty();
    }
    
    public int count() {
        return clipboard.size();
    }
    
    private void recursiveCopy(Path source, Path dest) throws IOException {
        if (!Files.exists(dest)) Files.createDirectories(dest);
        
        try (var stream = Files.walk(source)) {
            stream.forEach(srcPath -> {
                try {
                    Path relative = source.relativize(srcPath);
                    Path destPath = dest.resolve(relative);
                    
                    if (Files.isDirectory(srcPath)) {
                        if (!Files.exists(destPath)) Files.createDirectory(destPath);
                    } else {
                        Files.copy(srcPath, destPath,
                                StandardCopyOption.REPLACE_EXISTING,
                                StandardCopyOption.COPY_ATTRIBUTES);
                    }
                    
                } catch (IOException e) {
                    System.err.println("Error copiando " + srcPath + ": " + e.getMessage());
                }
            });
        }
    }
}