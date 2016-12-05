package chat;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class openPS{
    public static void open() {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://play.pokemonshowdown.com/"));
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}