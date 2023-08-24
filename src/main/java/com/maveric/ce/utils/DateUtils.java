package com.maveric.ce.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {

	public static String dateFormat(String inputDate) {
		String inputFormatStr = "E, dd MMM yyyy HH:mm:ss +0000";
		String outputFormatStr = "yyyy-MM-dd HH:mm:ss";
		String formattedDate = "";
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormatStr);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormatStr);

		try {
			LocalDateTime parsedDate = LocalDateTime.parse(inputDate, inputFormatter);
			formattedDate = outputFormatter.format(parsedDate);
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}
 
	public static String currentDateTimeFormat() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String orderPlacedDateTime = LocalDateTime.now().format(format);
		return orderPlacedDateTime;
	}
}
