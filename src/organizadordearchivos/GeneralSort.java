package organizadordearchivos;

import java.io.File;
import java.util.Comparator;

public class GeneralSort {
    
    public enum SortCriteria {
        
        NAME,
        DATE,
        SIZE,
        TYPE
        
    }
    
    public static Comparator<File> getComparator(SortCriteria criteria) {
        switch (criteria) {
            
            case DATE:
                return Comparator.comparingLong(File::lastModified).reversed();
                
            case SIZE:
                return Comparator.comparingLong(File::length).reversed();
                
            case TYPE:
                return Comparator.comparing(f -> {
                    if (f.isDirectory()) return "Directory";
                    String name = f.getName();
                    int lastDot = name.lastIndexOf('.');
                    return (lastDot == -1) ? "" : name.substring(lastDot + 1);
                });
                
            case NAME:
            default:
                return Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER);
        }
    }
    
    public static SortCriteria fromDisplayText(String selected) {
        if (selected == null) return SortCriteria.NAME;
        
        switch (selected) {
            
            case "Fecha":
                return SortCriteria.DATE;
                
            case "Tamaño":
                return SortCriteria.SIZE;
                
            case "Tipo":
                return SortCriteria.TYPE;
                
            default:
                return SortCriteria.NAME;
        }
    }
    
    public static SortCriteria fromColumnIndex(int colIndex) {
        switch (colIndex) {
            case 1: return SortCriteria.DATE;
            case 2: return SortCriteria.TYPE;
            case 3: return SortCriteria.SIZE;
            default: return SortCriteria.NAME;
        }
    }
}