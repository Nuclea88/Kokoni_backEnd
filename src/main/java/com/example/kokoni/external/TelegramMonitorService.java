package com.example.kokoni.external;

//REVISAR PARA ENTENDER
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.Init;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.kokoni.service.MediaUpdateService;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramMonitorService {
    private final MediaUpdateService updateService;
    // Ponemos un 0 por defecto por si aún no lo has puesto en tu .env y evitar que pete
    @Value("${TELEGRAM_API_ID:0}") private int apiId;
    @Value("${TELEGRAM_API_HASH:}") private String apiHash;
    @PostConstruct
    public void startMonitor() throws Exception {
        if (apiId == 0 || apiHash.isEmpty()) {
            log.warn("Telegram Monitor NO INICIADO: Faltan credenciales en el archivo .env");
            return;
        }
        // Inicializa los nativos
        Init.init();
        // 1. OBLIGATORIO en v3: Usar la Factory
        SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory();
        
        APIToken apiToken = new APIToken(apiId, apiHash);
        TDLibSettings settings = TDLibSettings.create(apiToken);
        
        Path sessionPath = Paths.get("tdlight-session");
        settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));
        // 2. ¡ATENCIÓN!: Se usa la factory para llamar al builder, NO usamos 'new'
        SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);
        // 3. Suscribimos al manejador de mensajes
        clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, update -> {
            TdApi.MessageContent content = update.message.content;
            String text = "";
            if (content instanceof TdApi.MessageText mt) {
                text = mt.text.text;
            } else if (content instanceof TdApi.MessageDocument md) {
                text = md.caption.text;
            }
            if (text != null && !text.isEmpty()) {
                log.info("Scrap de Grupo detectado: {}", text);
                updateService.processExternalUpdate(text, "Telegram");
            }
        });
        // 4. ¡ATENCIÓN!: La autenticación de la consola se pasa DENTRO del build()
        SimpleTelegramClient client = clientBuilder.build(AuthenticationSupplier.consoleLogin());
    }
}