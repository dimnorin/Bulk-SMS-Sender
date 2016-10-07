# Bulk-SMS-Sender
Bulk send sms to a list of phone numbers through cell phone. 
Compatible cell phones you can find [here](http://smslib.org/doc/compatibility/)

Tested on Windows only.

To make this app work you need do the following steps:

### COM port
Attach your cell phone using USB cable. And run this code:
```
java -cp SMS_sender.jar com.dimnorin.sms.CommTest
```
It will show you wat com port is used by your device and what baudRate is supported.

### Phone numbers
You need to save your target phone numbers into **phones.txt**.

### Config
Edit **config.properties** and add your values.

### Bulk Send
Start the program using:
```
java -jar SMS_sender.jar
```