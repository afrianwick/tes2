package com.pertamina.portal.core.utils.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtilsModel {

    private Bitmap resizedImage;
    private int rotate;
    private Matrix matrix;

    public ImageUtilsModel(Bitmap resizedImage, int rotate, Matrix matrix) {
        this.resizedImage = resizedImage;
        this.rotate = rotate;
        this.matrix = matrix;
    }

    public Bitmap getResizedImage() {
        return resizedImage;
    }

    public int getRotate() {
        return rotate;
    }

    public Matrix getMatrix() {
        return matrix;
    }
}

