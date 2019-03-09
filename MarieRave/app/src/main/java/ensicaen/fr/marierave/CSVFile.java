package ensicaen.fr.marierave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    private InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    public List<String[]> read() throws RuntimeException
    {
        List<String[]> resultList = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");

                resultList.add(row);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                //closing quietly
            }
        }
        return resultList;
    }
}
