package com.dimnorin.sms;

import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;

import com.dimnorin.sms.util.Utils;
/**
 * Split list of all phone numbers into separate files for every operator.
 * <p> Will work for Ukranian cell operators: KYIVSTAR, MTS, LIFE 
 */
public class PhonesSplitter {
	final static Logger logger = Logger.getLogger(PhonesSplitter.class);
	/**
	 * Regexp patterns to match phone and determine what operator it relates to
	 */
	public static final String KYIVSTAR_PATTERN = "^(039|067|068|096|097|098).*";
	public static final String MTS_PATTERN = "^(050|066|095|099).*";
	public static final String LIFE_PATTERN = "^(063|093|073).*";
	
	private List<String> phones;
	
	private File kFile;
	private File mFile;
	private File lFile;
	/**
	 * Starting point.
	 * @param args
	 */
	public static void main(String[] args){
		PhonesSplitter splitter = new PhonesSplitter();
		splitter.init();
		splitter.process();
	}
	
	private void init(){
		try {
			//TODO make file names not hard coded
			phones = Utils.readFile("./phones.txt");
			
			kFile = new File("./k_phones.txt");
			mFile = new File("./m_phones.txt");
			lFile = new File("./l_phones.txt");
			
			if(!kFile.exists()) kFile.createNewFile();
			if(!mFile.exists()) mFile.createNewFile();
			if(!lFile.exists()) lFile.createNewFile();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	/**
	 * Main phone list splitting process
	 */
	private void process(){
		StringBuilder kSb = new StringBuilder();
		StringBuilder mSb = new StringBuilder();
		StringBuilder lSb = new StringBuilder();
		
		for(String phone : phones){
			phone = phone.trim().replaceAll(" ", "");
			if(phone.matches(KYIVSTAR_PATTERN)){
				kSb.append(phone).append("\n");
			}else if(phone.matches(MTS_PATTERN)){
				mSb.append(phone).append("\n");
			}else if(phone.matches(LIFE_PATTERN)){
				lSb.append(phone).append("\n");
			}
		}
		try {
			Utils.writeFile(kFile, kSb.toString());
			Utils.writeFile(mFile, mSb.toString());
			Utils.writeFile(lFile, lSb.toString());
		} catch (Exception e) {
			logger.error(e);
		}
		
	}
}
