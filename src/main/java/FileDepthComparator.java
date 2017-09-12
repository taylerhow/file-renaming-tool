import java.io.File;
import java.util.Comparator;

public class FileDepthComparator implements Comparator<File> {

    @Override
    public int compare(File a, File b) {
        if (a == null && b == null) {
            return 0;
        } else if (a == null || b == null) {
            return a == null ? 1 : -1;
        }

        int parentComparison = compare(a.getParentFile(), b.getParentFile());
        if (parentComparison == 0) {
            if (a.isDirectory() != b.isDirectory()) {
                return a.isDirectory() ? 1 : -1;
            } else {
                return a.getName().compareTo(b.getName());
            }
        } else {
            return parentComparison;
        }
    }

    @Override
    public boolean equals(Object comparator) {
        return comparator != null && comparator.getClass().equals(getClass());
    }
}
