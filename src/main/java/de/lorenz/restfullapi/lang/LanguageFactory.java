package de.lorenz.restfullapi.lang;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.InputStream;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LanguageFactory {

    Map<String, String> germanTexts;
    Map<String, String> englishTexts;


    public String get(String key, String lang) {
        return switch (lang.toLowerCase()) {
            case "en" -> englishTexts.get(key);
            case "de" -> germanTexts.get(key);
            default -> key;
        };
    }

    @SneakyThrows
    private Map<String, String> loadJson(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error while Loading the lang-file: " + path, e);
        }
    }
}
