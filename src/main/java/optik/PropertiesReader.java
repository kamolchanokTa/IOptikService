package optik;

import org.springframework.beans.factory.annotation.Autowired;
import optik.AppProperties;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
/**
 * Created by Java Developer Zone on 26-08-2017.
 */
@Component  
public class PropertiesReader {
	@Autowired
    private AppProperties appProperties;
	
    public AppProperties getAppProperties(){
        return appProperties;
    }
}
