import java.util.ArrayList;

public class VerticalSeam implements ISeamHelper {

  @Override
  public ArrayList<Integer> adjVertices(int v, ISeamPicture seamPicture) {
    int numOfVertices = seamPicture.width() * seamPicture.height() + 2;
    ArrayList<Integer> arrayList = new ArrayList<>();
    if (v < numOfVertices - 1) {
      if (v == 0) { // virtual start vertex
        for (int x = 0; x < seamPicture.width(); x++) {
          arrayList.add(vertexOf(seamPicture.width(), x, 0));
        }
      } else {
        int x = (v - 1) % seamPicture.width();
        int y = (v - 1) / seamPicture.width();

        if (y == seamPicture.height() - 1) { // last row
          arrayList.add(numOfVertices - 1);
        } else {
          checkThenAdd(seamPicture, arrayList, x - 1, y + 1);
          checkThenAdd(seamPicture, arrayList, x, y + 1);
          checkThenAdd(seamPicture, arrayList, x + 1, y + 1);
        }
      }
    }

    return arrayList;
  }

  private void checkThenAdd(ISeamPicture seamPicture, ArrayList<Integer> arrayList, int x, int y) {
    if (x >= 0 && x < seamPicture.width()) {
      arrayList.add(vertexOf(seamPicture.width(), x, y));
    }
  }

  @Override
  public int seamLength(ISeamPicture seamPicture) {
    return seamPicture.height();
  }

  @Override
  public void setSeamValue(int[] seam, int x, int y) {
    seam[y] = x;
  }

  private int vertexOf(int width, int x, int y) {
    return y * width + x + 1;
  }
}