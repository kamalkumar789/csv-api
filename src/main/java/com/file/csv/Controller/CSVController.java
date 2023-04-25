package com.file.csv.Controller;


import com.file.csv.Service.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@Slf4j
public class CSVController {

    @Autowired
    private CSVService csvService;

    @PostMapping("/csv/file")
    public void downLoadCSVFile(@RequestParam MultipartFile file) throws Exception{
        if(!file.getContentType().equals("text/csv")){
            throw new Exception("File should be CSV typed");
        }
        csvService.downLoadCSVFile(file);
    }
}
