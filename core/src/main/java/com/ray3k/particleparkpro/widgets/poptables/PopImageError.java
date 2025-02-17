package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.ray3k.particleparkpro.Listeners;
import com.ray3k.particleparkpro.Utils;
import com.ray3k.stripe.PopTable;

import java.io.IOException;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.logFile;

/**
 * PopTable used to display errors during runtime that can be recovered from. These are typically file errors when
 * saving or opening particles and images. A link to the log directory enables users to retrieve the crash log file
 * through their OS file explorer.
 */
public class PopImageError extends PopTable {
    private String message;
    private String error;
    private boolean merge;

    public PopImageError(String message, String error, FileHandle particleFile, boolean merge) {
        super(skin.get(WindowStyle.class));

        this.message = message;
        this.error = error;
        this.merge = merge;

        populate(particleFile);
    }

    private void populate(FileHandle particleFile) {
        clearChildren();
        pad(20);
        defaults().space(10);

        var label = new Label("GDX Particle Editor encountered an error:", skin, "bold");
        add(label);

        row();
        label = new Label(message, skin);
        label.setWrap(true);
        add(label).growX();

        row();
        var scrollPane = new ScrollPane(null, skin);
        add(scrollPane).grow();

        label = new Label(error, skin);
        label.setColor(Color.RED);
        scrollPane.setActor(label);

        row();
        var table = new Table();
        table.defaults().uniformX().fillX().space(10);
        add(table);

        var textButton = new TextButton("Locate images...", skin);
        table.add(textButton);
        Listeners.addHandListener(textButton);
        Listeners.onChange(textButton, () -> {
            hide();
            Gdx.graphics.setSystemCursor(SystemCursor.Arrow);

            var pop = new PopLocateImages(particleFile, merge);
            pop.show(foregroundStage);
        });

        textButton = new TextButton("Close", skin);
        table.add(textButton);
        Listeners.addHandListener(textButton);
        Listeners.onChange(textButton, () -> {
            hide();
            Gdx.input.setInputProcessor(stage);
        });

        textButton = new TextButton("Open log", skin);
        table.add(textButton);
        Listeners.addHandListener(textButton);
        Listeners.onChange(textButton, () -> {
            try {
                Utils.openFileExplorer(logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            hide();
            Gdx.input.setInputProcessor(stage);
        });
    }
}
