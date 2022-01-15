import java.util.ArrayList;

public class VerticalSeam extends AbstractSeam {

  public VerticalSeam(SeamCarver sc) {
    super(sc);
  }

  @Override
  protected ArrayList<Integer> adjVertices(int v) {
    ArrayList<Integer> arrayList = new ArrayList<>();
    if (v < numOfVertices - 1) {
      if (v == 0) { // virtual start vertex
        for (int x = 0; x < sc.width(); x++) {
          arrayList.add(vertexOf(x, 0));
        }
      } else {
        int x = (v - 1) % sc.width();
        int y = (v - 1) / sc.width();

        if (y == sc.height() - 1) { // last row
          arrayList.add(numOfVertices - 1);
        } else {
          if (x > 0) {
            arrayList.add(vertexOf(x - 1, y + 1));
          }
          arrayList.add(vertexOf(x, y + 1));
          if (x < sc.width() - 1) {
            arrayList.add(vertexOf(x + 1, y + 1));
          }
        }
      }
    }

    return arrayList;
  }

  @Override
  protected int seamLength() {
    return sc.height();
  }

  @Override
  protected void setSeamValue(int[] seam, int x, int y) {
    seam[y] = x;
  }
}
