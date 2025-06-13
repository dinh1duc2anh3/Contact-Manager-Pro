package com.hello.shared.formatter;

public class ContactInfoFormatter {
    public static String formatName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        //to lowercase + split by space letter
        String[] parts = name.trim().toLowerCase().split("\\s+");
        
        StringBuilder formattedName = new StringBuilder();
        for (String part : parts){
        	if (!part.isEmpty()) {
        		//capitalize first letter of each word
        		formattedName.append(Character.toUpperCase(part.charAt(0)))
        						.append(part.substring(1))
        						.append(" ");
        	}
        }
      //remove trailing space
    	return formattedName.toString().trim();
    }

    
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "";
        }

      //to lowercase + split by space letter
        String[] parts = phoneNumber.trim().toLowerCase().split("\\s+");


        // Join parts with no space
        return String.join("", parts).trim();
        
    }



}