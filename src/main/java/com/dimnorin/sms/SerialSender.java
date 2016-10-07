package com.dimnorin.sms;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.smslib.Library;
import org.smslib.Message.MessageEncodings;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

import com.dimnorin.sms.util.Utils;

public class SerialSender {
	final static Logger logger = Logger.getLogger(SerialSender.class);
	
	/**
	 * Cell phone connected port number
	 */
	private String port;
	/**
	 * COM port baudRate
	 */
	private int baudRate;
	/**
	 * Text to be send as SMS
	 */
	private String smsText;
	/**
	 * Phone numbers list of SMS recipients
	 */
	private LinkedList<String> phoneNumbers;
	
	/**
	 * Start point.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new SerialSender().bulkSend();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	/**
	 * Send SMS to all phone numbers
	 * @throws Exception
	 */
	public void bulkSend() throws Exception{
		init();
		sendSms();
		Service.getInstance().stopService();
	}
	
	private void init() throws Exception{
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("./config.properties"));
			
			port = prop.getProperty("com.port");
			baudRate = Integer.parseInt(prop.getProperty("com.port.speed"));
			smsText = prop.getProperty("sms.text");
			
			phoneNumbers = Utils.readFile("./phones.txt"); //TODO remove hard coded file name
		} catch (Exception e) {
			logger.error(e);
		}
		
		System.out.println(Library.getLibraryDescription());
		System.out.println("Version: " + Library.getLibraryVersion());
		
		//Initialize serial gateway
		SerialModemGateway gateway = new SerialModemGateway("modem.com1", port, baudRate, "Samsung", "");
		gateway.setOutbound(true);
		//Configure and start sms send service
		Service.getInstance().getSettings().DISABLE_COPS = true;
		Service.getInstance().addGateway(gateway);
		Service.getInstance().startService();
	}
	
	/**
	 * Send SMS one after one to every phone number in list
	 */
	private void sendSms(){
		while(!phoneNumbers.isEmpty()){
			
			String number = phoneNumbers.poll();
			logger.info("Trying to send to "+number);
			
			OutboundMessage msg = new OutboundMessage(number, smsText);
			msg.setEncoding(MessageEncodings.ENCUCS2); //TODO ENCUCS2 needed to specify non-latin encoding, if you send latin sms remove this line 
	
			boolean sendResult = false;
			try{
				//Trying to send sms
				sendResult = Service.getInstance().sendMessage(msg);
			}catch(Exception e){}
			//If send was un success, put this number at the numbers list end to try once more in the future
			if(!sendResult){
				phoneNumbers.addLast(number);
				logger.info("FAILED to "+number+ ", sleep and resend");
				
				try {
					Thread.sleep(15000); //Need to sleep here as after unsuccess send service need some time to be ready to send
				} catch (Exception e) {}
			}else
				logger.info("SUCCESFULL to "+number);
		}
	}
}
