package com.raven.engine.launcher;

import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cookedbird on 11/26/17.
 */
public class GameLauncher {

    public static void Open(Game game) {

        // Doesn't work on linux
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        for (GraphicsDevice device : devices) {
            System.out.println(device.getIDstring() + ":");
            for (DisplayMode mode : device.getDisplayModes()) {
                System.out.println(mode.getWidth() + "x" + mode.getHeight());
            }
        }

        // Window
        JFrame winMain = new JFrame(game.getTitle());

        winMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        winMain.setResizable(false);
        winMain.setLocationRelativeTo(null);
        winMain.setLayout(new GridLayout(3, 1));

        // Water Quality
        Container conWaterQuality = new Container();
        conWaterQuality.setLayout(new FlowLayout());
        winMain.add(conWaterQuality);

        JLabel lblWaterQuality = new JLabel("Water Quality");
        conWaterQuality.add(lblWaterQuality);

        JComboBox cbWaterQuality = new JComboBox(new String[]{"High", "Medium", "Low", "Very Low"});
        conWaterQuality.add(cbWaterQuality);

        JCheckBox chbReflectTerrain = new JCheckBox("Reflect Terrain");
        chbReflectTerrain.setSelected(true);
        conWaterQuality.add(chbReflectTerrain);

        JCheckBox chbReflectObjects = new JCheckBox("Reflect Objects");
        chbReflectObjects.setSelected(true);
        conWaterQuality.add(chbReflectObjects);

        chbReflectTerrain.addActionListener(actionEvent -> {
            if (!chbReflectTerrain.isSelected()) {
                chbReflectObjects.setSelected(false);
            }
        });

        chbReflectObjects.addActionListener(actionEvent -> {
            if (chbReflectObjects.isSelected()) {
                chbReflectTerrain.setSelected(true);
            }
        });

        // Multisample
        Container conMultisample = new Container();
        conMultisample.setLayout(new FlowLayout());
        winMain.add(conMultisample);

        JCheckBox chbMultisample = new JCheckBox("Multisample");
        chbMultisample.setSelected(true);
        conMultisample.add(chbMultisample);

        JLabel lblMultisampleCount = new JLabel("Samples");
        conMultisample.add(lblMultisampleCount);

        JComboBox cbMultisampleCount = new JComboBox(new String[]{"2", "4", "8", "16", "32"});
        conMultisample.add(cbMultisampleCount);

        chbMultisample.addActionListener(actionEvent ->
                cbMultisampleCount.setEnabled(chbMultisample.isSelected()));

        // Launch Button
        Container conLaunch = new Container();
        conLaunch.setLayout(new FlowLayout());
        winMain.add(conLaunch);

        JButton btnLaunch = new JButton("Launch");
        conLaunch.add(btnLaunch);

        btnLaunch.addActionListener(actionEvent -> {
            if (chbMultisample.isSelected()) {
                GameProperties.setMultisampleCount(Integer.parseInt(cbMultisampleCount.getSelectedItem().toString()));
            } else {
                GameProperties.setMultisampleCount(0);
            }

            switch (cbWaterQuality.getSelectedIndex()) {
                case 1:
                    GameProperties.setWaterQuality(2);
                    break;
                case 2:
                    GameProperties.setWaterQuality(4);
                    break;
                case 3:
                    GameProperties.setWaterQuality(8);
                    break;
                case 0:
                default:
                    GameProperties.setWaterQuality(1);
                    break;
            }

            GameProperties.setReflectTerrain(chbReflectTerrain.isSelected());
            GameProperties.setReflectObjects(chbReflectObjects.isSelected());

            GameEngine.Launch(game);
            winMain.setVisible(false);
        });

        // Show
        winMain.pack();
        winMain.setVisible(true);
    }
}
