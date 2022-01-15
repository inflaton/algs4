import java.util.ArrayList;

public interface ISeamHelper {
  int seamLength(ISeamPicture seamPicture);

  ArrayList<Integer> adjVertices(int v, ISeamPicture seamPicture);

  void setSeamValue(int[] seam, int x, int y);
}
