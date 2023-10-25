import com.rusherdevelopment.rusherhack.RusherHack;
import com.rusherdevelopment.rusherhack.managers.ModuleManager;
import com.rusherdevelopment.rusherhack.module.Module;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class YourPlugin implements ClientModInitializer {
    private static ArrayList<String> motdList = new ArrayList<>();
    private static String motdFolder = "motd"; // The folder where MOTD files are located
    private static String selectedMOTDFile = "motd.txt"; // The MOTD file to start with
    private static final long MOTD_CHANGE_INTERVAL = 300000; // The change interval (e.g., 5 minutes)
    private static String currentMOTD = "";
    private static int currentCharIndex = 0;
    private static boolean reverseDirection = false;
    private static Timer motdChangeTimer = new Timer();

    @Override
    public void onInitializeClient() {
        // Read MOTD entries from the selected file and populate the list
        loadMOTDFromFile(selectedMOTDFile);

        // Set up Discord RPC with the first MOTD and queue information
        String queueInfo = ModuleManager.getModule("Queue").getArrayList().get(1).name; // Replace with the correct code to get queue info
        currentMOTD = motdList.get(0);
        DiscordRPC.start();

        // Schedule automatic MOTD updates at specified intervals
        motdChangeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                changeMOTD();
            }
        }, MOTD_CHANGE_INTERVAL, MOTD_CHANGE_INTERVAL);
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

    // Change the MOTD and update Discord RPC
    private void changeMOTD() {
        int currentIndex = motdList.indexOf(currentMOTD);
        if (currentIndex < motdList.size() - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        currentMOTD = motdList.get(currentIndex);

        String serverName = MinecraftClient.getInstance().getCurrentServerEntry().name; // Get the server name
        String serverIP = MinecraftClient.getInstance().getCurrentServerEntry().address; // Get the server IP address
        String motdStatus = "";

        if (serverName != null && serverIP != null && serverName.equals("2b2t.org")) {
            motdStatus = "Playing 2b2t.org";
            String positionInQueue = getPositionInQueueMessage(); // Get the position in queue message
            DiscordRPC.updateDetails(positionInQueue);
            DiscordRPC.updateState(currentMOTD);
        } else {
            motdStatus = currentMOTD;
            DiscordRPC.updateDetails("");
            DiscordRPC.updateState("");
        }

        DiscordRPC.update("Game Status", motdStatus, "large-image-key");
    }

    // Get the position in queue message
    private String getPositionInQueueMessage() {
        int positionInQueue = getPositionInQueue(); // Replace with code to get the position in queue
        return "Position in queue: " + positionInQueue;
    }

    // Replace with code to get the position in queue
    private int getPositionInQueue() {
        // Implement your logic to get the position in queue here
        return 0;
    }
}
