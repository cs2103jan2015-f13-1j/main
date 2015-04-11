package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

//This adapter class allows the convertion of LocalDateTime object into a readable XML element
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime>{
	
	@Override
	public LocalDateTime unmarshal(String s) throws Exception {
		return LocalDateTime.parse(s,DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
	@Override
	public String marshal(LocalDateTime s) throws Exception {
		return s.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
