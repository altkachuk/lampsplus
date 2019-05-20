package com.atproj.zugara.lampsplus.utils;

import android.widget.ImageView;

import com.atproj.zugara.lampsplus.model.Source;
import com.squareup.picasso.Picasso;

import java.io.File;

public class SourceLoader {

    public static void loadSource(Picasso picasso, Source source, ImageView target) {
        if (source.getSourceType() == Source.FILE) {
            picasso.load(new File(source.getSource())).into(target);
        }
    }
}
