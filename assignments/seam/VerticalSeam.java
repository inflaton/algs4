import java.util.ArrayList;

public class VerticalSeam extends AbstractSeam {

  public VerticalSeam(ISeamPicture seamPicture) {
    super(seamPicture);
  }

  @Override
  protected ArrayList<Integer> adjVertices(int v, ISeamPicture seamPicture) {
    int numOfVertices = seamPicture.width() * seamPicture.height() + 2;
    ArrayList<Integer> arrayList = new ArrayList<>();
    if (v < numOfVertices - 1) {
      if (v == 0) { // virtual start vertex
        for (int x = 0; x < seamPicture.width(); x++) {
          arrayList.add(vertexOf(x, 0));
        }
      } else {
        int x = (v - 1) % seamPicture.width();
        int y = (v - 1) / seamPicture.width();

        if (y == seamPicture.height() - 1) { // last row
          arrayList.add(numOfVertices - 1);
        } else {
          if (x > 0) {
            arrayList.add(vertexOf(x - 1, y + 1));
          }
          arrayList.add(vertexOf(x, y + 1));
          if (x < seamPicture.width() - 1) {
            arrayList.add(vertexOf(x + 1, y + 1));
          }
        }
      }
    }

    return arrayList;
  }

  @Override
  protected int seamLength(ISeamPicture seamPicture) {
    return seamPicture.height();
  }

  @Override
  protected void setSeamValue(int[] seam, int x, int y) {
    seam[y] = x;
  }
}
