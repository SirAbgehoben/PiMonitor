package org.Abgehoben;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class Main extends JFrame {

    private Webcam webcam;
    private WebcamPanel webcamPanel;

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

        // Set initial resolution
        setWebcamResolution();

        webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setMirrored(false); // Mirror the image (optional)
        add(webcamPanel, BorderLayout.CENTER);

        // Button to switch webcams
        JPanel buttonPanel = getJPanel();
        add(buttonPanel, BorderLayout.NORTH);

        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setWebcamResolution();
                revalidate();
                repaint();
            }
        });

        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void setWebcamResolution() {
        Dimension newSize = getContentPane().getSize();
        webcam.setViewSize(newSize);
        webcamPanel.setPreferredSize(newSize);
    }

    private JPanel getJPanel() {
        JButton switchButton = new JButton("Switch Webcam");
        switchButton.setBackground(Color.BLACK);
        switchButton.setForeground(Color.WHITE);
        switchButton.addActionListener(e -> showWebcamSelectionDialog());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(switchButton);
        return buttonPanel;
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
            setWebcamResolution();
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
        SwingUtilities.invokeLater(Main::new);
    }
}