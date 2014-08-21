
// Custom Cube Class

class Cube{
  float w, h, d;

  // Default constructor
  Cube(){ }

  // Constructor 2
  Cube(float w, float h, float d) {
    this.w = w;
    this.h = h;
    this.d = d;
  }
  void create(){
    // Draw cube
   box(w);
  }
  void create(color col){
    // Draw cube

    fill(col);
    box(w);
    
  }
}


