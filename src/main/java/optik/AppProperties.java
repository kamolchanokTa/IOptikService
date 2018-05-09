package optik;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Created by Kamolchanok
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = "app")        // It will read all properties prefix with app
                             // declare as component to allowed autowite
public class AppProperties {
	@Valid
    @NotNull
    private String name;
	
	@Valid
    @NotNull
    private String dateformat;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
       this.name = name;
    }
    
    public String getDateformat() {
        return dateformat;
    }
   
    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }
    @Valid
    private Status status = new Status();
    
    public Status getStatus() {
    		return this.status;
    }
    public class Status{
	    	@NotNull
	    	private String success;
	    	
	    	public String getSuccess() {
	    		return this.success;
	    	}
	    	public void setSuccess(String success) {
	    		this.success = success;
	    	}
	    	
	    	@NotNull
	    	private String fail;
	    	
	    	public String getFail() {
	    		return this.fail;
	    	}
	    	public void setFail(String fail) {
	    		this.fail = fail;
	    	}
	    	
	    	@NotNull
	    	private String unautherized;
	    	
	    	public String getUnautherized() {
	    		return this.unautherized;
	    	}
	    	public void setUnautherized(String unautherized) {
	    		this.unautherized = unautherized;
	    	}
    	
    }
}

