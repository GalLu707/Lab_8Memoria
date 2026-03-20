package organizadordearchivos;

import java.io.File;

public class FileNode {
    public File data;
    public FileNode next;

    public FileNode(File data) {
        this.data = data;
        this.next = null;
    }
}
