package optik.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import optik.AppProperties;
import optik.PropertiesReader;

@Controller
public class BaseController {

	@Autowired
    private PropertiesReader propertiesReader;
	
	BaseController(){
		propertiesReader = new PropertiesReader();
	}
	
	public AppProperties getAppProperties() {
		return propertiesReader.getAppProperties();
	}
	public boolean isValidAppid(String appid) {
		return matching(appid,propertiesReader.getAppProperties().getName());
	}
	
	private static boolean matching(String orig, String compare){
	    String md5 = null;
	    try{
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(compare.getBytes());
	        byte[] digest = md.digest();
	        md5 = new BigInteger(1, digest).toString(16);

	        return md5.equals(orig);

	    } catch (NoSuchAlgorithmException e) {
	        return false;
	    }
	}
}
