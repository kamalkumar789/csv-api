package CustomCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class MappingFile {

    private Map<String, String> mp = new HashMap<>();

    public MappingFile(){
        mp.put("f_name", "firstName");
        mp.put("f_lastname", "lastName");
        mp.put("f_email", "email");
        mp.put("f_customer_number", "customerNumber");
    }

}
