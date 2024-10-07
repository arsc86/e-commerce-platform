package net.project.ecommerce.dependency.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Format {
	
	static Logger log = LogManager.getLogger(Format.class);


    /** 
     * Convert serialized object to a non serialized
     * @param <T>
     * @param object serialized
     * @param targetType   
     * @return Objeto non serialized
     *   
     */
    public static <T> T objectMapping(Object object, Class<T> targetType) throws Exception {
        try {
            ObjectMapper objMapper = JsonMapper.builder()
                    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)   
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                    .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .build();

            return objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .convertValue(object, targetType);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Convert a serialized list object to a non serialized list object
     *
     * @param <T>
     * @param object 
     * @param targetType  
     * @return list
     *
     */
    public static <T> List<T>  listMapping(Object object, Class<T> targetType) throws Exception {
        try {
            ObjectMapper objMapper =  JsonMapper.builder()
                    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)                   
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .build();
            return objMapper.readValue(objMapper.writeValueAsString(object),
                    objMapper.getTypeFactory().constructCollectionType(List.class, targetType));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
      
 
    public static boolean validateDateformat(String date, String format) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(format);
        try {
            formateador.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
  
	public static boolean validateDaysBetweenDates(String startDate, String endDate, int numberDays, String format) {
		try {
			SimpleDateFormat formatoFechaInicio = new SimpleDateFormat(format);
			formatoFechaInicio.setLenient(false);
			Date fechaInicioDate = formatoFechaInicio.parse(startDate);
			
			SimpleDateFormat formatoFechaFin = new SimpleDateFormat(format);
			formatoFechaFin.setLenient(false);
			Date fechaFinDate = formatoFechaFin.parse(endDate);
			
			int diasRango = (int) ((fechaFinDate.getTime() - fechaInicioDate.getTime()) / 86400000);
			if (diasRango > numberDays) {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

  
    public static LocalDate getLocalDateFromString(String date, String format) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, dateFormatter);
    }
  
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String date = element.getAsString();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("America/Guayaquil"));
		
		try {
			return format.parse(date);
		} catch (ParseException exp) {
			return null;
		}
	}
    
	public static String getStringToCLOB(Clob clob) {
		String clobAsString = "";
		try {
			Reader in = clob.getCharacterStream();
			StringWriter w = new StringWriter();
			IOUtils.copy(in, w);
			clobAsString = w.toString().trim();
		} catch (IOException | SQLException e) {
			log.error("Error en formato getStringToCLOB " + e.getLocalizedMessage());
		}
		return clobAsString;
	}
	
	 public static Date getDateByString(String fecha, String formatoFecha) {
	    SimpleDateFormat formato = new SimpleDateFormat(formatoFecha);
	    Date fechaDate = null;
	    try {
	      fechaDate = formato.parse(fecha);
	    } catch (ParseException e) {
	      log.error("Error en formato getDateByString " + e.getLocalizedMessage());
	    }
	    return fechaDate;
	  }
	 
	@SuppressWarnings("deprecation")
	public static <T> T getObjectToJson(String jsonString, Class<T> tipoValueClass) {
	    T objecto = null;
	    ObjectMapper objMapper = new ObjectMapper();
	    objMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	    objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    objMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	    try {
	      objecto = objMapper.readValue(jsonString, tipoValueClass);
	    } catch (JsonProcessingException e) {
	      log.error("Error en formato mapearObjectToString : {}",e.getLocalizedMessage());
	    }
	    return objecto;
	}

	public static List<String> getListBySplit(String cadena, String delimitador) {
		List<String> response = new ArrayList<String>();
		if (delimitador.equalsIgnoreCase("|")) {
			delimitador = "\\|";
		}
		String[] arrayCadena = cadena.split(delimitador);
		for (String valor : arrayCadena) {
			response.add(valor);
		}
		return response;
	}


}
