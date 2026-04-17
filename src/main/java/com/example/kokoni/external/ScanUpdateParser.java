package com.example.kokoni.external;



//REVISAR, HECHO CON PRISAS
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class ScanUpdateParser {
    /**
     * Procesa nombres de archivos de scans para extraer Obra y Capítulo.
     */
    public ParsedData parse(String filename) {
        // 1. Quitar extensiones comunes y limpiar símbolos raros
        String cleanName = filename.replace(".pdf", "")
                                   .replace(".rar", "")
                                   .replace(".zip", "")
                                   .replaceAll("[^\\p{L}\\p{N}\\s\\-|:|\\|\\u00B2]", " ")
                                   .trim();
        // 2. Regex: Busca (Números/Rangos/Temporadas) -> Separador -> Título
        // Patrón ajustado para ser más robusto con los ejemplos que vimos
        Pattern pattern = Pattern.compile("^(\\d+(?:[-\\s]*\\d+)?(?:\\s*T\\u00B2)?)\\s*[\\-|:|\\|]\\s*(.*)$");
        Matcher matcher = pattern.matcher(cleanName);
        if (matcher.find()) {
            return new ParsedData(
                matcher.group(2).trim(), // Título: "An Abyss"
                matcher.group(1).trim()  // Capítulo: "107 T²"
            );
        }
        // Si no cumple el patrón, devolvemos todo como título por si se vincula manual
        return new ParsedData(cleanName, "N/A");
    }
    public record ParsedData(String title, String chapter) {}
}