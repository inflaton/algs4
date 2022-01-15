import java.util.ArrayList;

public class HorizontalSeam extends AbstractSeam {

  public HorizontalSeam(ISeamPicture seamPicture) {
    super(seamPicture);
  }

  @Override
  protected ArrayList<Integer> adjVertices(int v, ISeamPicture seamPicture) {
    int numOfVertices = seamPicture.width() * seamPicture.height() + 2;
    ArrayList<Integer> arrayList = new ArrayList<>();
    if (v < numOfVertices - 1) {
      if (v == 0) { // virtual start vertex
        for (int y = 0; y < seamPicture.height(); y++) {
          arrayList.add(vertexOf(0, y));
        }
      } else {
        int x = (v - 1) % seamPicture.width();
        int y = (v - 1) / seamPicture.width();

        if (x == seamPicture.width() - 1) { // last column
          arrayList.add(numOfVertices - 1);
        } else {
          if (y > 0) {
            arrayList.add(vertexOf(x + 1, y - 1));
          }
          arrayList.add(vertexOf(x + 1, y));
          if (y < seamPicture.height() - 1) {
            arrayList.add(vertexOf(x + 1, y + 1));
          }
        }
      }
    }

    return arrayList;
  }

  @Override
  protected int seamLength(ISeamPicture seamPicture) {
    return seamPicture.width();
  }

  @Override
  protected void setSeamValue(int[] seam, int x, int y) {
    seam[x] = y;
  }
}
