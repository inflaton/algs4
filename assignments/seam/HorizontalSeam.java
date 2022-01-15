import java.util.ArrayList;

public class HorizontalSeam implements ISeamHelper {

  @Override
  public ArrayList<Integer> adjVertices(int v, ISeamPicture seamPicture) {
    int numOfVertices = seamPicture.width() * seamPicture.height() + 2;
    ArrayList<Integer> arrayList = new ArrayList<>();
    if (v < numOfVertices - 1) {
      if (v == 0) { // virtual start vertex
        for (int y = 0; y < seamPicture.height(); y++) {
          arrayList.add(vertexOf(seamPicture.width(), 0, y));
        }
      } else {
        int x = (v - 1) % seamPicture.width();
        int y = (v - 1) / seamPicture.width();

        if (x == seamPicture.width() - 1) { // last column
          arrayList.add(numOfVertices - 1);
        } else {
          checkThenAdd(seamPicture, arrayList, x + 1, y - 1);
          checkThenAdd(seamPicture, arrayList, x + 1, y);
          checkThenAdd(seamPicture, arrayList, x + 1, y + 1);
        }
      }
    }

    return arrayList;
  }

  private void checkThenAdd(ISeamPicture seamPicture, ArrayList<Integer> arrayList, int x, int y) {
    if (y >= 0 && y < seamPicture.height()) {
      arrayList.add(vertexOf(seamPicture.width(), x, y));
    }
  }

  @Override
  public int seamLength(ISeamPicture seamPicture) {
    return seamPicture.width();
  }

  @Override
  public void setSeamValue(int[] seam, int x, int y) {
    seam[x] = y;
  }

  private int vertexOf(int width, int x, int y) {
    return y * width + x + 1;
  }
}
