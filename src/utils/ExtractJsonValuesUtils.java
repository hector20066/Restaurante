package utils;

/**
 *
 * @author Andres Cardenas
 */
public class ExtractJsonValuesUtils {

    public static String extractJsonValues(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);

        if (keyIndex == -1) {
            return "No disponibles";
        }

        int valueStartIndex = keyIndex + searchKey.length();
        int valueEndIndex = json.indexOf(",", valueStartIndex);

        if (valueEndIndex == -1) {
            valueEndIndex = json.indexOf("}", valueStartIndex);
        }

        String value = json.substring(valueStartIndex, valueEndIndex);
        return value.replace("\"", "");
    }

}
