package net.atos.humaj.jakub.Files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CsvFinder {

    public List<File> getFilteredFileList(String dirPath, String suffix, boolean findInSubdirectories){
        if(findInSubdirectories){
            return new ArrayList<>(FileUtils.listFiles(new File(dirPath), new SuffixFileFilter(suffix), TrueFileFilter.INSTANCE));
        } else {
            return new ArrayList<>(FileUtils.listFiles(new File(dirPath), new SuffixFileFilter(suffix), null));
        }
    }
}