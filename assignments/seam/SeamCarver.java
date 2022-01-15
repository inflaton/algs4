import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

  private Picture picture;

  private final ISeamPicture seamPicture =
      new ISeamPicture() {
        @Override
        public int width() {
          return SeamCarver.this.width();
        }

        @Override
        public int height() {
          return SeamCarver.this.height();
        }

        @Override
        public double energy(int x, int y) {
          return SeamCarver.this.energy(x, y);
        }
      };

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null) {
      throw new IllegalArgumentException();
    }
    this.picture = new Picture(picture);
  }

  // current picture
  public Picture picture() {
    return new Picture(picture);
  }

  // width of current picture
  public int width() {
    return this.picture.width();
  }

  // height of current picture
  public int height() {
    return this.picture.height();
  }

  private static class Color {
    int b;
    int g;
    int r;

    public Color(int argb) {
      b = (argb) & 0xFF;
      g = (argb >> 8) & 0xFF;
      r = (argb >> 16) & 0xFF;
      // a = (argb>>24)&0xFF;
    }
  }

  private Color getColor(int x, int y) {
    int rgb = this.picture.getRGB(x, y);
    return new Color(rgb);
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    if (x < 0 || x > this.picture.width() - 1 || y < 0 || y > this.picture.height() - 1) {
      throw new IllegalArgumentException();
    }

    if (x == 0 || x == this.picture.width() - 1 || y == 0 || y == this.picture.height() - 1) {
      return 1000;
    }

    Color c1 = this.getColor(x + 1, y);
    Color c2 = this.getColor(x - 1, y);

    int rx = c1.r - c2.r;
    int gx = c1.g - c2.g;
    int bx = c1.b - c2.b;
    int yielding = rx * rx + gx * gx + bx * bx;

    c1 = this.getColor(x, y + 1);
    c2 = this.getColor(x, y - 1);

    int ry = c1.r - c2.r;
    int gy = c1.g - c2.g;
    int by = c1.b - c2.b;
    yielding += ry * ry + gy * gy + by * by;

    return Math.sqrt(yielding);
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    HorizontalSeam horizontalSeam = new HorizontalSeam();
    return new SeamFinder(seamPicture, horizontalSeam).seam();
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    VerticalSeam verticalSeam = new VerticalSeam();
    return new SeamFinder(seamPicture, verticalSeam).seam();
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    if (seam == null || seam.length != width() || height() <= 1) {
      throw new IllegalArgumentException();
    }

    Picture newPic = new Picture(width(), height() - 1);
    for (int x = 0; x < width(); x++) {
      if (seam[x] < 0 || seam[x] > height() - 1 || x > 0 && Math.abs(seam[x] - seam[x - 1]) > 1) {
        throw new IllegalArgumentException();
      }
      for (int y = 0; y < height(); y++) {
        if (seam[x] != y) {
          int newY = y > seam[x] ? y - 1 : y;
          newPic.setRGB(x, newY, this.picture.getRGB(x, y));
        }
      }
    }

    this.picture = newPic;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    if (seam == null || seam.length != height() || width() <= 1) {
      throw new IllegalArgumentException();
    }

    Picture newPic = new Picture(width() - 1, height());
    for (int y = 0; y < height(); y++) {
      if (seam[y] < 0 || seam[y] > width() - 1 || y > 0 && Math.abs(seam[y] - seam[y - 1]) > 1) {
        throw new IllegalArgumentException();
      }
      for (int x = 0; x < width(); x++) {
        if (seam[y] != x) {
          int newX = x > seam[y] ? x - 1 : x;
          newPic.setRGB(newX, y, this.picture.getRGB(x, y));
        }
      }
    }

    this.picture = newPic;
  }
}
