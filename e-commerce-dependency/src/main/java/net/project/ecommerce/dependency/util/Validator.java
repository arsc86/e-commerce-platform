package net.project.ecommerce.dependency.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;


/**
 * Clase genérica con validaciones reutilizables
 *
 * @author Juan Romero <mailto:jromero@telconet.ec>
 * @version 1.0
 * @since 02/06/2023
 */
@Component
public class Validator {

    private Validator() {}


    public static <T> void validateFieldObjectFromTemplate(T object, List<String> requiredFieldList) throws Exception {
        StringBuilder   result = new StringBuilder("");
        if (object != null){
            if (requiredFieldList != null && !requiredFieldList.isEmpty())  {
                for(Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    for (String key : requiredFieldList) {
                        try {
                            if (key.equalsIgnoreCase(field.getName()) && (field.get(object) == null
                                        || field.get(object).toString().isEmpty())) {
                                result.append("Attribute ").append(object.getClass().getSimpleName()).append(".")
                                        .append(field.getName()).append(" should not be null").append("\n");
                            }
                        } catch (IllegalAccessException e) {
                            result.append("Attribute ").append(object.getClass().getSimpleName()).append(".")
                                    .append(field.getName()).append(" couldn't has been validated").append("\n");
                        }
                    }
                }
            } else {
                result.append("Required Field List to validate shouldn't be empty or null");
            }
        } else {

            result.append("Object to validate shouldn't be null");
        }

        if (!result.toString().trim().isEmpty()){
            throw new Exception(result.toString());
        }
    }

//    public static <T> void validateFieldOfObject(T object) throws Exception {
//        StringBuilder   result = new StringBuilder("");
//        if (object != null) {
//            for (Field field : object.getClass().getDeclaredFields()) {
//                field.setAccessible(true);
//                try {
//                    if (field.get(object) == null || field.get(object).toString().isEmpty()) {
//                        result.append("Attribute ").append(object.getClass().getSimpleName()).append(".")
//                                .append(field.getName()).append(" should not be null").append("\n");
//                    }
//                } catch (IllegalAccessException e) {
//                    result.append("Attribute ").append(object.getClass().getSimpleName()).append(".")
//                            .append(field.getName()).append(" couldn't has been validated").append("\n");
//                }
//            }
//        } else {
//            result.append("Object to validate shouldn't be null");
//        }
//        if (!result.toString().trim().isEmpty()){
//            throw new Exception(result.toString());
//        }
//    }


//    public static <T> void validateFieldObjectListFromTemplate(List<T> objectList, List<String> requiredFieldList)
//            throws Exception {
//        StringBuilder   result = new StringBuilder("");
//        if (objectList != null && !objectList.isEmpty()) {
//            if (requiredFieldList != null && !requiredFieldList.isEmpty())  {
//                for (T object : objectList) {
//                    validateFieldObjectFromTemplate(object, requiredFieldList);
//                }
//            } else {
//                result.append("Required Field List to validate shouldn't be empty or null");
//            }
//        } else {
//            result.append("The object list ").append("Should not be null or empty");
//        }
//        if (!result.toString().trim().isEmpty()){
//            throw new Exception(result.toString());
//        }
//    }
//
//    public static void validateParentConfigMap(String MapName, Map<String, Map<String, String>> configMap,
//                                               String templateName, List<String> template) throws Exception {
//        StringBuilder result = new StringBuilder("");
//        validateMapNull(MapName, configMap);
//        validateListNull(templateName, template);
//        for (Map.Entry<String, Map<String, String>> entry : configMap.entrySet()) {
//            validateMapNull(entry.getKey(), configMap.get(entry.getKey()));
//            result.append(validateChildConfigMap(template, entry.getKey(), configMap.get(entry.getKey())));
//        }
//        if (!result.toString().trim().isEmpty()) {
//            throw new Exception(result .toString());
//        }
//    }
//
//    public static String validateChildConfigMap(List<String> template, String keyMap, Map<String, String> map) {
//        String status = null;
//        StringBuilder result = new StringBuilder("");
//        for (String templateName : template) {
//            status = validateAttribute(templateName, map);
//            if (!GeneralConstants.SUCCESS_STATUS_DEFAULT.equals(status)) {
//                status = setDefaultAttribute(templateName, map, status);
//            }
//            if (!GeneralConstants.SUCCESS_STATUS_DEFAULT.equals(status)) {
//                result.append(keyMap).append(".").append(templateName).append(" ").append(status) .append(" | ");
//            }
//        }
//        return result.toString();
//    }
//
//    public static void validateChildConfigMapObject(List<String> template, String keyMap, Map<String, Object> map) throws Exception{
//        StringBuilder result = new StringBuilder("");
//        for (String templateName : template) {
//            if(map.get(templateName) == null || map.get(templateName).toString().isEmpty()){
//                result.append("Key ").append(keyMap).append(".")
//                        .append(templateName).append(" should not be null").append("\n");
//            }
//        }
//        if (!result.toString().trim().isEmpty()){
//            throw new Exception(result.toString());
//        }
//    }
//
//    @SuppressWarnings("rawtypes")
//	public static void validateMapNull(String mapName, Map map) throws Exception {
//        if (map == null || map.isEmpty()) {
//            throw new Exception("The map shouldn't be null or empty");
//        }
//    }
//
//    public static void validateListNull(String templateName, List<String> template) throws Exception {
//        if (template == null || template.isEmpty()) {
//            throw new Exception("The template  shouldn't be null or empty");
//        }
//    }
//
//    public static String validateAttribute(String key, Map<String, String> map) {
//        String status = GeneralConstants.NO_EXIST_STATUS;
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            if (key!= null && key.equals(entry.getKey())) {
//                if (map.get(key).isEmpty()) {
//                    status = GeneralConstants.VALUE_LESS_STATUS;
//                } else {
//                    status = GeneralConstants.SUCCESS_STATUS_DEFAULT;
//                }
//                break;
//            }
//        }
//        return status;
//    }
//
//    public static boolean validateOptionMap(String option, String tagOption,
//                                            Map<String, Map<String, String>> parentMap, int sizeMap) {
//        if (option != null && parentMap != null && !parentMap.isEmpty()) {
//            for (Map.Entry<String, Map<String, String>> entry : parentMap.entrySet()) {
//                if (option.equals(parentMap.get(entry.getKey()).get(tagOption))
//                        && parentMap.get(entry.getKey()) != null
//                        && (sizeMap > 0) ? parentMap.get(entry.getKey()).size() == sizeMap : true){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public static Boolean emailValidate(String email) {
		String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher mather = pattern.matcher(email);
		return mather.matches();
	}
    
//    public static String setDefaultAttribute(String keyMap, Map<String, String> attributeMap, String currenStatus) {
//        String result = GeneralConstants.SUCCESS_STATUS_DEFAULT;
//        switch (keyMap) {
//            case GeneralConstants.SUCCESS_MESSAGE:
//                attributeMap.replace(keyMap, GeneralConstants.SUCCESS_MESSAGE_DEFAULT);
//                break;
//            case GeneralConstants.FAILURE_MESSAGE:
//                attributeMap.replace(keyMap, GeneralConstants.FAILURE_MESSAGE_DEFAULT);
//                break;
//            default:
//                result = currenStatus;
//                break;
//        }
//        return result;
//    }

}
