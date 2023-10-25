import com.rusherdevelopment.rusherhack.RusherHack;
import com.rusherdevelopment.rusherhack.managers.ModuleManager;
import com.rusherdevelopment.rusherhack.module.Module;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class YourPlugin implements ClientModInitializer {
    private static ArrayList<String> motdList = new ArrayList<>();
    private static String motdFolder = "motd"; // MOTD dosyalarının bulunduğu klasörün adı
    private static String selectedMOTDFile = "motd.txt"; // Başlangıçta kullanılacak MOTD dosyası
    private static final long MOTD_CHANGE_INTERVAL = 600000; // Değişim aralığı (örneğin, 10 dakika)
    private static int currentIndex = 0; // Başlangıçta gösterilecek karakter indeksi
    private static String currentMOTD = ""; // Şu anki MOTD

    @Override
    public void onInitializeClient() {
        // Read MOTD entries from the selected file and populate the list
        loadMOTDFromFile(selectedMOTDFile);

        // Schedule automatic MOTD updates at specified intervals
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the current MOTD character by character
                if (currentIndex < currentMOTD.length()) {
                    String currentCharacter = String.valueOf(currentMOTD.charAt(currentIndex));
                    currentIndex++;
                    DiscordRPC.start();
                    DiscordRPC.update("Game Status", currentCharacter, "large-image-key");
                } else {
                    // If the entire MOTD has been displayed, reset the index
                    currentIndex = 0;
                }
            }
        }, 0, 500); // 0.5 saniye (500 milisaniye) aralıklarla karakteri güncelle

        // Set up Discord RPC with the first MOTD and queue information
        String queueInfo = ModuleManager.getModule("Queue").getArrayList().get(1).name; // Replace with the correct code to get queue info
        currentMOTD = motdList.get(0);
        DiscordRPC.start();
        DiscordRPC.update("Game Status", String.valueOf(currentMOTD.charAt(0), "large-image-key");
        DiscordRPC.updateDetails(queueInfo);
    }

    // Load MOTD entries from the specified file
    private void loadMOTDFromFile(String filename) {
        try {
            Path minecraftDirectory = Paths.get(MinecraftClient.getInstance().runDirectory.toURI());
            File motdFile = new File(new File(minecraftDirectory.toFile(), motdFolder), filename);
            if (motdFile.exists()) {
                motdList.clear();
                BufferedReader reader = new BufferedReader(new FileReader(motdFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    motdList.add(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
