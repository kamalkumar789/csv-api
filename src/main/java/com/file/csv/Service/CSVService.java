package com.file.csv.Service;

import CustomCode.MappingFile;
import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.client.error.BadRequestException;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.customer.CustomerDraftImpl;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.commercetools.importapi.models.errors.DuplicateFieldError;
import com.commercetools.importapi.models.errors.DuplicateFieldErrorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.csv.Exception.CommercetoolException;
import com.opencsv.CSVWriter;
import io.vrap.rmf.base.client.ApiHttpResponse;
import io.vrap.rmf.base.client.error.BadGatewayException;
import okhttp3.internal.http2.ErrorCode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CSVService {

    private Map<String, String> mp = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ProjectApiRoot projectApiRoot;
    public void downLoadCSVFile(MultipartFile file) throws Exception{

        mp.put("f_name", "firstName");
        mp.put("f_lastname", "lastName");
        mp.put("f_email", "email");
        mp.put("f_customer_number", "customerNumber");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT);

        List<CSVRecord> fromFile = csvParser.getRecords();
        int size = fromFile.size();
        List<List<String>> ans = new ArrayList<>();
        int i = 1, t = 0;

        List<String> header = new ArrayList<>();
        System.out.println(header);
        while(t<4){
            header.add(mp.containsKey(fromFile.get(0).get(t))==true?mp.get(fromFile.get(0).get(t)):fromFile.get(0).get(t));
            t++;
        }
        String updatedHeader[] = header.toArray(new String[0]);
        //getting data
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

    public void mappingJson(MultipartFile file) {

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader("src/main/resources/customer-data.json"));
            JSONObject jsonObject = (JSONObject)obj;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT);
            List<CSVRecord> fromFile = csvParser.getRecords();
            List<String> header = new ArrayList<>();
            int t = 0;
            while(t<fromFile.get(0).size()){
                if(jsonObject.containsKey(fromFile.get(0).get(t))){
                    header.add((String)jsonObject.get(fromFile.get(0).get(t)));
                }else {
                    header.add(fromFile.get(0).get(t));
                }
                t++;
            }
            String updatedHeader[] = header.toArray(new String[0]);
//            CSVWriter csvWriter = new CSVWriter(new FileWriter("C:\\Users\\KamalKumar\\Downloads\\customers.csv"));
//            csvWriter.writeNext(updatedHeader);
//            csvWriter.close();

            int i = 0, k = 0, totalRecords = 1;
            Map<String, String> mp = new HashMap<>();
            File file1 = new File("C:\\Users\\KamalKumar\\Downloads\\customer_errors.csv");
            file1.createNewFile();
            CSVWriter csvWriter = new CSVWriter(new FileWriter("C:\\Users\\KamalKumar\\Downloads\\customer_errors.csv"));
            csvWriter.writeNext(new String[]{"CustomerNumber", "ErrorCode", "ErrorMessage"});
            while(totalRecords < fromFile.size()){
                k = 0; mp.clear();
                while(k<fromFile.get(totalRecords).size()){
                    mp.put(updatedHeader[k],fromFile.get(totalRecords).get(k));
                    k++;
                }
                CustomerDraft customerDraft = new CustomerDraftImpl();
                customerDraft.setCustomerNumber(mp.get("customerNumber"));
                customerDraft.setEmail(mp.get("email"));
                customerDraft.setFirstName(mp.get("firstName"));
                customerDraft.setLastName(mp.get("lastName"));
                customerDraft.setPassword(mp.get("password"));
                customerDraft.setMiddleName(mp.getOrDefault("middleName",null));
                customerDraft.setCompanyName(mp.getOrDefault("companyName",null));
                try {
                    ApiHttpResponse<CustomerSignInResult> customer = projectApiRoot.customers().post(customerDraft).executeBlocking();
                    System.out.println("Customer added sucessfully..");
                }catch (BadRequestException exception){
                    CommercetoolException commercetoolException = objectMapper.readValue(exception.getBody(), CommercetoolException.class);
                    csvWriter.writeNext(new String[]{mp.get("customerNumber"), String.valueOf(commercetoolException.getStatusCode())
                            , commercetoolException.getMessage()});
                }
                totalRecords+=1;
            }
            csvWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}