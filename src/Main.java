import javax.swing.*;

import ui.KlinikFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new KlinikFrame();
            }
        });
    }
}