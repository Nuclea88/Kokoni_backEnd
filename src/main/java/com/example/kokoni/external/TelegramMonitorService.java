package com.example.kokoni.external;

//REVISAR
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.Init;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.example.kokoni.service.MediaUpdateService;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramMonitorService implements CommandLineRunner {
    private final MediaUpdateService updateService;
    @Value("${TELEGRAM_API_ID:0}") private int apiId;
    @Value("${TELEGRAM_API_HASH:}") private String apiHash;
    @Override
    public void run(String... args) throws Exception {
        if (apiId == 0 || apiHash.isEmpty()) {
            log.warn("Telegram Monitor NO INICIADO: Faltan credenciales en el archivo .env");
            return;
        }
      
        new Thread(() -> {
            try {
           
                Init.init();
                SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory();
                APIToken apiToken = new APIToken(apiId, apiHash);
                TDLibSettings settings = TDLibSettings.create(apiToken);
                
                Path sessionPath = Paths.get("tdlight-session");
                settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
                settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));
                SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);
                clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, update -> {
                    TdApi.MessageContent content = update.message.content;
                    String text = "";
                    if (content instanceof TdApi.MessageText mt) {
                        text = mt.text.text;
                    } else if (content instanceof TdApi.MessageDocument md) {
                        text = md.caption.text;
                    }
                    if (text != null && !text.isEmpty()) {
                        log.info("Mensaje interceptado en Telegram: {}", text);
                        updateService.queueExternalUpdate(text, "Telegram");
                    }
                });
                log.info("Iniciando Login en Telegram por consola...");
         
                SimpleTelegramClient client = clientBuilder.build(AuthenticationSupplier.consoleLogin());
                
            } catch (Exception e) {
                log.error("Fallo al arrancar el bot de Telegram", e);
            }
        }).start();
    }
}