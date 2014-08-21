/**
 * Flocking 
 * by Daniel Shiffman.  
 * 
 * An implementation of Craig Reynold's Boids program to simulate
 * the flocking behavior of birds. Each boid steers itself based on 
 * rules of avoidance, alignment, and coherence.
 * 
 * Click the mouse to add a new boid.
 */

class Flocking extends CustomLayer {
  Flock flock;
  String myId="Flocking";

  Flocking(PApplet parent) {
    super(parent,"Flocking",false, 0,1);
  }

  void setup() {
     flock = new Flock();
    // Add an initial set of boids into the system
    for (int i = 0; i < 600; i++) {
      flock.addBoid(new Boid(new PVector(width/2,height/2), 1.5f, 0.05));
     }
    smooth();
  }

  void customDraw() {
    background(0,0);
    flock.run();
  }
  
  // Add a new boid into the System
void mousePressed() {
  flock.addBoid(new Boid(new PVector(mouseX,mouseY),1.5f,0.05f));
}
}





