import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energyMat;
    private boolean flagEnergyMatCached;
    public SeamCarver(Picture picture) {
        this.picture = picture;
        flagEnergyMatCached = false;
        energyMat = new double[picture.width()][picture.height()];
    }
    public Picture picture()  {
        int w = width();
        int h = height();
        Picture newPicture = new Picture(w, h);
        for (int j = 0; j < h; ++j) {
            for (int i = 0; i < w; ++i) {
                newPicture.set(i, j, picture.get(i, j));
            }
        }
        return newPicture;
    }
    public int width() {
        return picture.width();
    }
    public int height() {
        return picture.height();
    }
    public double energy(int x, int y) {
        int w = width();
        int h = height();
        if (x < 0 || x >= w || y < 0 || y >= h) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        Color colorL = picture.get(Math.floorMod(x - 1, w), y);
        Color colorR = picture.get((x + 1) % w, y);
        Color colorU = picture.get(x, Math.floorMod(y - 1, h));
        Color colorD = picture.get(x, (y + 1) % h);
        int bx = colorR.getBlue() - colorL.getBlue();
        int gx = colorR.getGreen() - colorL.getGreen();
        int rx = colorR.getRed() - colorL.getRed();
        int by = colorU.getBlue() - colorD.getBlue();
        int gy = colorU.getGreen() - colorD.getGreen();
        int ry = colorU.getRed() - colorD.getRed();
        return rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by;
    }
    private void transpose() {
        int w = picture.width();
        int h = picture.height();
        Picture transposedPic = new Picture(h, w);
        double[][] transposedMat = new double[h][w];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                transposedPic.set(j, i, picture.get(i, j));
                transposedMat[j][i] = energyMat[i][j];
            }
        }
        picture = transposedPic;
        energyMat = transposedMat;
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] ret = findVerticalSeam();
        transpose();
        return ret;
    }
    public int[] findVerticalSeam() {
        int w = width();
        int h = height();
        double[][] costMat = new double[w][h];
        int[][] directionMat = new int[w][h];
        int[] seqX = new int[h];
        if (!flagEnergyMatCached) {
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    energyMat[i][j] = energy(i, j);
                }
            }
            flagEnergyMatCached = true;
        }
        for (int i = 0; i < w; i++) {
            costMat[i][0] = energyMat[i][0];
            directionMat[i][0] = 0;
        }
        for (int j = 1; j < h; j++) {
            for (int i = 0; i < w; i++) {
                double minPrevCost = Double.MAX_VALUE;
                int minDirection = 0;
                for (int ii = -1; ii <= 1; ii++) {
                    if (i + ii >= 0 && i + ii < w) {
                        double value = costMat[i + ii][j - 1];
                        if (value < minPrevCost) {
                            minPrevCost = value;
                            minDirection = ii;
                        }
                    }
                }
                directionMat[i][j] = minDirection;
                costMat[i][j] = energyMat[i][j] + minPrevCost;
            }
        }
        int minLastX = -1;
        double minCost = Double.MAX_VALUE;
        for (int i = 0; i < w; i++) {
            double value = costMat[i][h - 1];
            if (value < minCost) {
                minCost = value;
                minLastX = i;
            }
        }
        int currX = minLastX;
        for (int j = h - 1; j >= 0; j--) {
            seqX[j] = currX;
            currX += directionMat[currX][j];
        }
        return seqX;
    }
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException("Input seam array cannot be null.");
        } else if (picture.width() == 1) {
            throw new IllegalArgumentException("Image height is 1.");
        } else if (seam.length != picture.height()) {
            throw new IllegalArgumentException("Seam length does not match image height.");
        } else {
            for (int j = 0; j < seam.length - 1; ++j) {
                if (Math.abs(seam[j] - seam[j + 1]) > 1) {
                    throw new IllegalArgumentException("Invalid seam, "
                                                       + "consecutive horizontal "
                                                       + "indices are greater than one apart.");
                }
            }

            int w = width();
            int h = height();
            Picture newPicture = new Picture(w - 1, h);
            for (int j = 0; j < h; ++j) {
                for (int i = 0; i < seam[j]; ++i) {
                    newPicture.set(i, j, picture.get(i, j));
                }
                for (int i = seam[j] + 1; i < picture.width(); ++i) {
                    newPicture.set(i - 1, j, picture.get(i, j));
                }
            }
            picture = newPicture;
            w = width();
            for (int j = 0; j < h; ++j) {
                int i = seam[j];
                energyMat[Math.floorMod(i - 1, w)][j] = energy(Math.floorMod(i - 1, w), j);
                energyMat[i % w][j] = energy(i % w, j);
                for (int ii = i + 1; ii < w; ii++) {
                    energyMat[i][j] = energyMat[i + 1][j];
                }
            }
        }
    }
}
