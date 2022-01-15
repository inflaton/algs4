interface ISeamPicture {
  // width of current picture
  int width();

  // height of current picture
  int height();

  // energy of pixel at column x and row y
  double energy(int x, int y);
}
