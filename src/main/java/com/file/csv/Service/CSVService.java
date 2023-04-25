package com.file.csv.Service;

import com.opencsv.CSVWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    public void downLoadCSVFile(MultipartFile file) throws Exception{

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT);

        List<CSVRecord> fromFile = csvParser.getRecords();
        int size = fromFile.size();
        String updatedHeader[] = new String[]{"customerNumber", "firstName", "lastName", "email"};
        List<List<String>> ans = new ArrayList<>();
        int i = 1;
        while(i<size){

            int k = 0;
            List<String> l = new ArrayList<>();
            while(k<4){
                l.add(fromFile.get(i).get(k));
                k++;
            }
            ans.add(l);
            i++;
        }
        csvParser.close();

        CSVWriter csvWriter = new CSVWriter(new FileWriter("C:\\Users\\KamalKumar\\Downloads\\customers.csv"));
        csvWriter.writeNext(updatedHeader);
        for(int ii=0; ii<ans.size(); ii++){
            String line[] = ans.get(ii).toArray(new String[0]);
            csvWriter.writeNext(line);
        }
        csvWriter.close();
    }
}