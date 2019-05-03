package unipos.common.http;

import lombok.Data;

/**
 * Created by ggradnig on 06.02.2017.
 */

@Data
public class KeyValuePair {
    private String key;
    private String value;

    public static KeyValuePair of(String key, String value){
        KeyValuePair keyValuePair = new KeyValuePair();
        keyValuePair.setKey(key);
        keyValuePair.setValue(value);
        return keyValuePair;
    }
}
