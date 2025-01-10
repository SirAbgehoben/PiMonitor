package org.Abgehoben;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main extends JFrame {

    private Webcam webcam;
    private WebcamPanel webcamPanel;
    private JButton switchButton;

    public Main() {
        super("Webcam Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize with the default webcam
        webcam = Webcam.getDefault();
        if (webcam == null) {
            JOptionPane.showMessageDialog(this, "No webcam found!");
            System.exit(1);
        }
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setMirrored(true); // Mirror the image (optional)
        add(webcamPanel, BorderLayout.CENTER);

        // Button to switch webcams
        switchButton = new JButton("Switch Webcam");
        switchButton.setBackground(Color.BLACK);
        switchButton.setForeground(Color.WHITE);
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWebcamSelectionDialog();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(switchButton);
        add(buttonPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void showWebcamSelectionDialog() {
        List<Webcam> webcams = Webcam.getWebcams();
        Webcam[] webcamArray = webcams.toArray(new Webcam[0]);

        Webcam selectedWebcam = (Webcam) JOptionPane.showInputDialog(
                this,
                "Select a webcam:",
                "Webcam Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                webcamArray,
                webcam
        );

        if (selectedWebcam != null && selectedWebcam != webcam) {
            // Stop the current webcam
            webcam.close();

            // Update webcam and panel
            webcam = selectedWebcam;
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.open();

            remove(webcamPanel);
            webcamPanel = new WebcamPanel(webcam);
            webcamPanel.setMirrored(true);
            add(webcamPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        // Use SwingUtilities for thread safety
        SwingUtilities.invokeLater(() -> new Main());
    }
}