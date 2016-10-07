package com.dimnorin.sms.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
/**
 * Utility class
 */
public class Utils {
	/**
	 * REad file into list of string divided by new line
	 * @param filePath absolute path to file
	 * @return list of string read
	 * @throws IOException
	 */
	public static LinkedList<String> readFile(String filePath) throws IOException {
		LinkedList<String> list = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        String line;
        while((line=reader.readLine()) != null){
            list.add(line);
        }
        reader.close();
        return list;
    }
	/**
	 * Write string into file. File will be overwritten.
	 * @param file destination file
	 * @param s string to be written
	 * @throws IOException
	 */
	public static void writeFile(File file, String s) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(s);
		bw.close();
	}
}
