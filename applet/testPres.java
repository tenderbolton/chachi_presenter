import processing.core.*; 
import processing.xml.*; 

import com.nootropic.processing.layers.*; 
import ddf.minim.*; 
import ddf.minim.*; 
import megamu.mesh.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class testPres extends PApplet {




Minim minim;
AudioInput in;

int w = 250;
int h = w;
int startX, startY;
AppletLayers layers;
int currentLayer=0;
//CustomLayer cubes;
CustomLayer flock;
//CustomLayer extru;
CustomLayer mesh;
CustomLayer audio;
CustomLayer console;


 PFont fontA;

public void setup() {
  
  size(1024, 768, P2D);
 // frameRate(12);
  layers = new AppletLayers(this);
  colorMode(HSB);
  cursor(CROSS);
  
  fontA = loadFont("ModeSeven-40.vlw");
  textFont(fontA, 35);
   
  PVector v = new PVector((mouseX - 10)*0.2f, (mouseY - 10)*0.2f);
  
  //cubes = new CubesWithinCubes(this);
  flock = new Flocking(this);
  mesh= new Mesh(this);
  audio= new Sound(this);
  console= new Prompt(this);
  //extru = new Extrusion(this);
   
  //cubes.setDoDraw(false);
  //cubes.setVisible(false);
 
  flock.setDoDraw(false);
  flock.setVisible(false);
 
  //extru.setDoDraw(false);
  //extru.setVisible(false);
    
  mesh.setDoDraw(false);
  mesh.setVisible(false);
  
  audio.setDoDraw(false);
  audio.setVisible(false);
  
  console.setDoDraw(true);
  console.setVisible(true);
  
  //layers.addLayer(cubes);
  layers.addLayer(flock);
  //layers.addLayer(extru);
  layers.addLayer(mesh);
  
  layers.addLayer(audio);
  layers.addLayer(console);
 
 
   minim = new Minim(this);
   in = minim.getLineIn(Minim.STEREO, 512);
   
   
  
}

public void paint(java.awt.Graphics g) {
  if (layers != null) {
    layers.paint(this);
  } 
  else {
    super.paint(g);
  }
}

public void draw() {
 background(50);
 audio.audioDataUpdate(in);
 
}

public void keyPressed() {
  if (key == 'a' || key == 'A') {
      flock.setDoDraw(true);
      flock.setVisible(true);
      
      //cubes.setDoDraw(false);
      //cubes.setVisible(false);
      
      //extru.setDoDraw(false);
      //extru.setVisible(false);
      
      mesh.setDoDraw(false);
      mesh.setVisible(false);
      
      audio.setDoDraw(false);
      audio.setVisible(false);
    
  }
  if (key == 'b' || key == 'B') {
      flock.setDoDraw(false);
      flock.setVisible(false);
      
      //cubes.setDoDraw(true);
      //cubes.setVisible(true);
      
      //extru.setDoDraw(false);
      //extru.setVisible(false);
      
    mesh.setDoDraw(false);
      mesh.setVisible(false);
      
       audio.setDoDraw(false);
      audio.setVisible(false);
    }
    if (key == 'c' || key == 'C') {
      flock.setDoDraw(false);
      flock.setVisible(false);
      
      //cubes.setDoDraw(false);
      //cubes.setVisible(false);
      
      //extru.setDoDraw(true);
      //extru.setVisible(true);
      
     mesh.setDoDraw(false);
      mesh.setVisible(false);
      
       audio.setDoDraw(true);
      audio.setVisible(true);
    }
    
    if (key == 'd' || key == 'D') {
      flock.setDoDraw(false);
      flock.setVisible(false);
      
      //cubes.setDoDraw(false);
      //cubes.setVisible(false);
      
      //extru.setDoDraw(false);
      //extru.setVisible(false);
      
     mesh.setDoDraw(true);
      mesh.setVisible(true);
      
       audio.setDoDraw(false);
      audio.setVisible(false);
    }
    char[] newChar= new char[1];
    newChar[0]=key;
    String myNewString = new String(newChar);
    console.addNewText(myNewString);
  
}

public void mousePressed() {
 
}

public void mouseReleased() {
  
}

public void stop()
{
  // always close Minim audio classes when you are done with them
  in.close();
  minim.stop();
  
  super.stop();
}
// The Boid class

class Boid {

  PVector loc;
  PVector vel;
  PVector acc;
  float r;
  float maxforce;    // Maximum steering force
  float maxspeed;    // Maximum speed

    Boid(PVector l, float ms, float mf) {
    acc = new PVector(0,0);
    vel = new PVector(random(-1,1),random(-1,1));
    loc = l.get();
    r = 2.0f;
    maxspeed = ms;
    maxforce = mf;
  }

  public void run(ArrayList boids) {
    flock(boids);
    update();
    borders();
    render();
  }

  // We accumulate a new acceleration each time based on three rules
  public void flock(ArrayList boids) {
    PVector sep = separate(boids);   // Separation
    PVector ali = align(boids);      // Alignment
    PVector coh = cohesion(boids);   // Cohesion
    // Arbitrarily weight these forces
    sep.mult(1.5f);
    ali.mult(1.0f);
    coh.mult(1.0f);
    // Add the force vectors to acceleration
    acc.add(sep);
    acc.add(ali);
    acc.add(coh);
  }

  // Method to update location
  public void update() {
    // Update velocity
    vel.add(acc);
    // Limit speed
    vel.limit(maxspeed);
    loc.add(vel);
    // Reset accelertion to 0 each cycle
    acc.mult(0);
  }

  public void seek(PVector target) {
    acc.add(steer(target,false));
  }

  public void arrive(PVector target) {
    acc.add(steer(target,true));
  }

  // A method that calculates a steering vector towards a target
  // Takes a second argument, if true, it slows down as it approaches the target
  public PVector steer(PVector target, boolean slowdown) {
    PVector steer;  // The steering vector
    PVector desired = target.sub(target,loc);  // A vector pointing from the location to the target
    float d = desired.mag(); // Distance from the target is the magnitude of the vector
    // If the distance is greater than 0, calc steering (otherwise return zero vector)
    if (d > 0) {
      // Normalize desired
      desired.normalize();
      // Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
      if ((slowdown) && (d < 100.0f)) desired.mult(maxspeed*(d/100.0f)); // This damping is somewhat arbitrary
      else desired.mult(maxspeed);
      // Steering = Desired minus Velocity
      steer = target.sub(desired,vel);
      steer.limit(maxforce);  // Limit to maximum steering force
    } 
    else {
      steer = new PVector(0,0);
    }
    return steer;
  }

  public void render() {
    // Draw a triangle rotated in the direction of velocity
    float theta = vel.heading2D() + PI/2;
    fill(200,100);
    stroke(255);
    pushMatrix();
    translate(loc.x,loc.y);
    rotate(theta);
    beginShape(TRIANGLES);
    vertex(0, -r*2);
    vertex(-r, r*2);
    vertex(r, r*2);
    endShape();
    popMatrix();
  }

  // Wraparound
  public void borders() {
    if (loc.x < -r) loc.x = width+r;
    if (loc.y < -r) loc.y = height+r;
    if (loc.x > width+r) loc.x = -r;
    if (loc.y > height+r) loc.y = -r;
  }

  // Separation
  // Method checks for nearby boids and steers away
  public PVector separate (ArrayList boids) {
    float desiredseparation = 20.0f;
    PVector steer = new PVector(0,0,0);
    int count = 0;
    // For every boid in the system, check if it's too close
    for (int i = 0 ; i < boids.size(); i++) {
      Boid other = (Boid) boids.get(i);
      float d = PVector.dist(loc,other.loc);
      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
      if ((d > 0) && (d < desiredseparation)) {
        // Calculate vector pointing away from neighbor
        PVector diff = PVector.sub(loc,other.loc);
        diff.normalize();
        diff.div(d);        // Weight by distance
        steer.add(diff);
        count++;            // Keep track of how many
      }
    }
    // Average -- divide by how many
    if (count > 0) {
      steer.div((float)count);
    }

    // As long as the vector is greater than 0
    if (steer.mag() > 0) {
      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalize();
      steer.mult(maxspeed);
      steer.sub(vel);
      steer.limit(maxforce);
    }
    return steer;
  }

  // Alignment
  // For every nearby boid in the system, calculate the average velocity
  public PVector align (ArrayList boids) {
    float neighbordist = 25.0f;
    PVector steer = new PVector(0,0,0);
    int count = 0;
    for (int i = 0 ; i < boids.size(); i++) {
      Boid other = (Boid) boids.get(i);
      float d = PVector.dist(loc,other.loc);
      if ((d > 0) && (d < neighbordist)) {
        steer.add(other.vel);
        count++;
      }
    }
    if (count > 0) {
      steer.div((float)count);
    }

    // As long as the vector is greater than 0
    if (steer.mag() > 0) {
      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalize();
      steer.mult(maxspeed);
      steer.sub(vel);
      steer.limit(maxforce);
    }
    return steer;
  }

  // Cohesion
  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
  public PVector cohesion (ArrayList boids) {
    float neighbordist = 25.0f;
    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
    int count = 0;
    for (int i = 0 ; i < boids.size(); i++) {
      Boid other = (Boid) boids.get(i);
      float d = loc.dist(other.loc);
      if ((d > 0) && (d < neighbordist)) {
        sum.add(other.loc); // Add location
        count++;
      }
    }
    if (count > 0) {
      sum.div((float)count);
      return steer(sum,false);  // Steer towards the location
    }
    return sum;
  }
}



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
  public void create(){
    // Draw cube
   box(w);
  }
  public void create(int col){
    // Draw cube

    fill(col);
    box(w);
    
  }
}


/**
 * Cubes Contained Within a Cube 
 * by Ira Greenberg.  
 * 
 * Collision detection against all
 * outer cube's surfaces. 
 * Uses the Point3D and Cube classes. 
 */
class CubesWithinCubes extends CustomLayer {
  
  Cube stage; // external large cube
  int cubies = 20;
  Cube[]c = new Cube[cubies]; // internal little cubes
  int[] colBox = new int[cubies];
  
  // Controls cubie's movement
  float[]x = new float[cubies];
  float[]y = new float[cubies];
  float[]z = new float[cubies];
  float[]xSpeed = new float[cubies];
  float[]ySpeed = new float[cubies];
  float[]zSpeed = new float[cubies];
  
  // Controls cubie's rotation
  float[]xRot = new float[cubies];
  float[]yRot = new float[cubies];
  float[]zRot = new float[cubies];
  
  // Size of external cube
  float bounds = 600;
  
  CubesWithinCubes(PApplet parent) {
    super(parent,"CubesWithinCubes",true,0,42);
  }
  
  public void setup() {    
    for (int i = 0; i < cubies; i++){
      // Each cube face has a random color component
      float colorShift = random(-75, 75);
      colBox[i] = color(0 + (int) colorShift);
  
      // Cubies are randomly sized
      float cubieSize = random(5, 15);
      c[i] =  new Cube(cubieSize, cubieSize, cubieSize);
  
      // Initialize cubie's position, speed and rotation
      x[i] = 0;
      y[i] = 0; 
      z[i] = 0;
  
      xSpeed[i] = random(-1, 1);
      ySpeed[i] = random(-1, 1); 
      zSpeed[i] = random(-1, 1); 
  
      xRot[i] = random(40, 100);
      yRot[i] = random(40, 100);
      zRot[i] = random(40, 100);
    }
    
    // Instantiate external large cube
    stage =  new Cube(bounds, bounds, bounds);
  }
  
  public void customDraw(){
    //resetMatrix();
    background(0,0);
    lights();
    
    // Center in display window
    translate(width/2, height/2, -330);
   
    // Outer transparent cube
    noFill(); 
    
    // Rotate everything, including external large cube
    rotateX(frameCount * 0.001f);
    rotateY(frameCount * 0.002f);
    rotateZ(frameCount * 0.001f);
    stroke(255);
    
    // Draw external large cube
    //stage.create();
    
    
    // Move and rotate cubies
    for (int i = 0; i < cubies; i++){
      pushMatrix();
      translate(x[i], y[i], z[i]);
      rotateX(frameCount*PI/xRot[i]);
      rotateY(frameCount*PI/yRot[i]);
      rotateX(frameCount*PI/zRot[i]);
      stroke(255);
      c[i].create(colBox[i]);
      x[i] += xSpeed[i];
      y[i] += ySpeed[i];
      z[i] += zSpeed[i];
      popMatrix();
  
      // Draw lines connecting cubbies
      stroke(0);
      if (i < cubies-1){
        line(x[i], y[i], z[i], x[i+1], y[i+1], z[i+1]);
      }
  
      // Check wall collisions
      if (x[i] > bounds/2 || x[i] < -bounds/2){
        xSpeed[i]*=-1;
      }
      if (y[i] > bounds/2 || y[i] < -bounds/2){
        ySpeed[i]*=-1;
      }
      if (z[i] > bounds/2 || z[i] < -bounds/2){
        zSpeed[i]*=-1;
      }
    }
    
  }
}



class CustomLayer extends Layer {
  
  String myId="";
  
  boolean doDraw=false;
  
  AudioInput myAudioIn;
 
  int totalMillis;
  int millisToFrame;
  
 
 CustomLayer(PApplet parent, String id, boolean enable3D, int myTotalMillis, int myMillisToFrame) {
    super(parent, P2D);
    myId=id;
    totalMillis=myTotalMillis;
    millisToFrame=myMillisToFrame;
  }
  
  public String getId(){
    return myId;
  }
  
  public Boolean getDoDraw(){
     return doDraw;
  }
  
  public void setDoDraw(boolean newVal){
     this.doDraw=newVal;
  }
  
  public void setup() {
    
  }
  
  public void draw() {
    
    if (doDraw){
      int elapsed = millis() - totalMillis;
      if (elapsed>=millisToFrame){
        totalMillis+=elapsed;
        customDraw();
      }
       
    }
  }
  
  
  
  public void customDraw(){
  
  }
  
  public void audioDataUpdate(AudioInput in){
    myAudioIn=in;
  
  }
  
  public void addNewText(String myNewText){
    
  }
  
}
/**
 * Extrusion. 
 * 
 * Converts a flat image into spatial data points and rotates the points
 * around the center.
 **/

class Extrusion extends CustomLayer{

  PImage extrude;
  int[][] values;
  float angle = 0;

 Extrusion (PApplet parent) {
    super(parent,"Extrusion", true,0,42);
 }


  public void setup() {
    // Load the image into a new array
    extrude = loadImage("26.jpg");
    extrude.loadPixels();
    values = new int[extrude.width][extrude.height];
    for (int y = 0; y < extrude.height; y++) {
      for (int x = 0; x < extrude.width; x++) {
        int pixel = extrude.get(x, y);
        values[x][y] = PApplet.parseInt(brightness(pixel));
      }
    }
  }

 public void customDraw(){
    background(0,0);
    
    // Update the angle
    angle += 0.005f;
    
    // Rotate around the center axis
    translate(width/2, 0, -128);
    rotateY(angle);  
    translate(-extrude.width/2, 100, -128);
    
    // Display the image mass
    for (int y = 0; y < extrude.height; y++) {
      for (int x = 0; x < extrude.width; x++) {
        stroke(values[x][y]);
        point(x, y, -values[x][y]);
      }
    }
  }
}

// The Flock (a list of Boid objects)

class Flock {
  ArrayList boids; // An arraylist for all the boids

  Flock() {
    boids = new ArrayList(); // Initialize the arraylist
  }

  public void run() {
    for (int i = 0; i < boids.size(); i++) {
      Boid b = (Boid) boids.get(i);  
      b.run(boids);  // Passing the entire list of boids to each boid individually
    }
  }

  public void addBoid(Boid b) {
    boids.add(b);
  }

}

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

  public void setup() {
     flock = new Flock();
    // Add an initial set of boids into the system
    for (int i = 0; i < 600; i++) {
      flock.addBoid(new Boid(new PVector(width/2,height/2), 1.5f, 0.05f));
     }
    smooth();
  }

  public void customDraw() {
    background(0,0);
    flock.run();
  }
  
  // Add a new boid into the System
public void mousePressed() {
  flock.addBoid(new Boid(new PVector(mouseX,mouseY),1.5f,0.05f));
}
}





//Raven Kwok (aka Guo Ruiwen)
//oct22a_2011
/*
  
raystain@gmail.com
twitter.com/ravenkwok
flickr.com/ravenkwok
weibo.com/ravenkwok
the-moor.blogbus.com
  
note: Update on Sep.03, 2012.
_1. Some modification on the motion of each particle, thanks to Ale( http://www.openprocessing.org/portal/?userID=12899 )'s advice :)
_2. Each particle now has it own "tensile force". This "force" is indicated by size of the block.
*/
 


class Mesh extends CustomLayer{ 
 
int count;
float xOffset, yOffset;
float [][] pos;
Particle [] particles;
Delaunay delaunay;
int drawing;
 
 Mesh(PApplet parent) {
    super(parent,"Flocking",false,0,80);
  }
 
public void setup() {
  //smooth();
  rectMode(CENTER);
  count = 75;
  xOffset = width/2;
  yOffset = height/2;
  particles = new Particle[count];
  for (int i=0;i<count;i++) {
    particles[i] = new Particle(xOffset, yOffset);
  }

}
public void customDraw() {
  //background(255,0);
  
  background(50,0);
  
  for (int i=0;i<count;i++) {
    particles[i].update();
  }
  pos = new float[count][2];
  for ( int j=0; j<pos.length;j++) {
    pos[j][0] = particles[j].xCurr;
    pos[j][1] = particles[j].yCurr;
  }
  delaunay = new Delaunay(pos);
  float[][] edges = delaunay.getEdges();
  for (int i=0; i<edges.length; i++)
  {
    float startX = edges[i][0];
    float startY = edges[i][1];
    float endX = edges[i][2];
    float endY = edges[i][3];
    float distance = dist(startX, startY, endX, endY);
    float trans = 255-map(distance,0,60,0,255);
    //float sw = 2/sqrt(distance+1);
    float sw = 1;
    strokeWeight(sw);
    stroke(255);
    line(startX, startY, endX, endY);
  }
   
  for (int i=0;i<count;i++) {
    particles[i].display();
  }
}
 
public void keyPressed() {
  if (key =='r') {
    for (int i=0;i<count;i++) {
      particles[i].reset();
    }
  }
}

}

class Particle {
  float xCurr, yCurr;
  float xInit, yInit;
  float xo,yo;
  float pushForce;
  float recoverForce;
  Particle(float xo, float yo) {
    this.xo = xo;
    this.yo = yo;
     
    float degreeTemp = random(360);
    float rTemp = random(10, 400);
    xInit = cos(radians(degreeTemp))*rTemp+xo;
    yInit = sin(radians(degreeTemp))*rTemp+yo;
    xCurr = xInit;
    yCurr = yInit;
    pushForce = random(10,600);
    recoverForce = random(10,600);
  }
  public void update() {
    float x0 = xCurr;
    float y0 = yCurr;
    float a = mouseX-x0;
    float b = mouseY-y0;
    float r = pushForce/(a*a+b*b);
    float quer_fugir_x = xCurr-a*r;
    float quer_fugir_y = yCurr-b*r;
    float quer_voltar_x = (xInit-x0)/recoverForce;
    float quer_voltar_y = (yInit-y0)/recoverForce;
    xCurr = quer_fugir_x+quer_voltar_x;
    yCurr = quer_fugir_y+quer_voltar_y;
  }
  public void display() {
    pushMatrix();
    translate(xCurr,yCurr);
    rotate(radians(360*noise(xCurr*0.01f,yCurr*0.01f)));
    float diam = (pushForce/recoverForce)/dist(xCurr,yCurr,mouseX,mouseY)*100;
    strokeWeight(2);
    stroke(180);
    fill(50);
    rect(0, 0,diam,diam);
    popMatrix();
  }
  public void reset() {
    float degreeTemp = random(360);
    float rTemp = random(10, 180);
    pushForce = random(10,300);
    recoverForce = random(10,100);
    xInit = cos(radians(degreeTemp))*rTemp+xo;
    yInit = sin(radians(degreeTemp))*rTemp+yo;
  }
}


class Prompt extends CustomLayer{

  //PFont fontA;
  int xBase = 50;
  int xStep = 21;
  int yBase = 60;
  int yStep = 35;
  
  int promptX=0;
  int promptY=18;
  
  int lineMaxY=18;
  int lineMaxChars = 40;
  int promptTLimit=200;
  int typeTLimit=0;
  int promptElapsedTime=0;
  int typeElapsedTime=0;
  int mainElapsedTime=0;
  
  int promptHeigth=40;
  int promptWidth=24;
  
  int promptXoffset = 2;
  int promptYoffset = 6;
  
  String[] console;
  String promptLine;
  String toType;
  
  Prompt(PApplet parent) {
    super(parent,"Prompt",false,0,50);
  }
  
  public void setup(){
    
    console= new String[18];
    
    for (int i=0; i<console.length;i++){
      console[i]="";
    }
  
    toType="";
    promptLine="";
  
    //fontA = loadFont("ModeSeven-40.vlw");
  
    // Set the font and its size (in units of pixels)
    //textFont(fontA, 35);
  
  }
  
  public void customDraw(){
    // Use fill() to change the value or color of the text
    background(50,0);
    int interval = millis() - mainElapsedTime;
    mainElapsedTime += interval;
    myUpdate(interval);
    for (int i=0; i<console.length;i++){
      text(console[i], xBase, yBase + (yStep*i));
    }
  
  }
  
  public void keyPressed() {
    promptAdvance();
  }
  
  public void stop()
  {
  
  }
  
  
  
  public void myUpdate(int interval){
    type(interval);
    drawPrompt(interval);
  }
  
  boolean mustDrawPrompt=false;
  
  public void drawPrompt(int interval){
    if ((promptElapsedTime + interval)>promptTLimit){
      mustDrawPrompt = !mustDrawPrompt;
      promptElapsedTime =0;
    }
    else{
      promptElapsedTime += interval;
      //println(promptElapsedTime);
    }
    
    if(mustDrawPrompt){
      fill(40,180,40);
      noStroke();
      rect((xBase + (promptX*xStep))+promptXoffset, yBase + (yStep*promptY) - promptHeigth + promptYoffset, promptWidth, promptHeigth);
      stroke(1);
    }
  }
  
  public void type(int interval){
    boolean mustType=false;
    if ((typeElapsedTime + interval)>typeTLimit){
      mustType = true;
      typeElapsedTime =0;
    }
    else{
      typeElapsedTime += interval;
    }
    text(promptLine, xBase,yBase+ (yStep*lineMaxY) );
    if(mustType){
      fill(40,180,40);
      if(!toType.equals("")){
        text(toType.charAt(0), xBase + (xStep*promptX), yBase+ (yStep*lineMaxY));
        promptAdvance();
        promptLine+=toType.charAt(0);
        toType=toType.substring(1);
        mustDrawPrompt=true;
      }
    }
  }
  
  public void promptAdvance(){
     promptX+=1; 
     if (promptX>=lineMaxChars){
       promptReturn();
     }
  }
  
  public void promptReturn(){
    promptX=0;
    appendLine(promptLine);
    promptLine="";
    
  }
  
  public void appendLine(String newLine){
    for (int i = 0 ; i < console.length ; i++){
      if (i==console.length-1){
        console[i]=newLine;
      }
      else{
        console[i]=console[i+1];
      }
    }
  }
  
  public void cls(){
    for (int i = 0 ; i < console.length ; i++){
        console[i]="";
    }
    promptLine="";
  }
  
  public void setToType(String newLine){
    toType=newLine;
  }
  
  public void addNewText(String myNewText){
    toType += myNewText;
  }

}


class Sound extends CustomLayer{
  
   Sound(PApplet parent) {
    super(parent,"Spound",false,0,1);
  }
  
  
  public void setup()
  {

  }
  
  public void customDraw()
  {
    background(50,0);
    stroke(255);
    
    // draw the waveforms
    for(int i = 0; i < in.bufferSize() - 1; i++)
    {
      line(i*2, 50 + myAudioIn.left.get(i)*200, (i+1)*2, 50 + myAudioIn.left.get(i+1)*400);
      line(i*2, 150 + myAudioIn.right.get(i)*200, (i*2)+20, 150 + myAudioIn.right.get(i+1)*400);
      
      line(i*2, 250 + myAudioIn.left.get(i)*200, (i+1)*2, 250 + myAudioIn.left.get(i+1)*200);
      line(i*2, 350 + myAudioIn.right.get(i)*200, (i*2)+20, 350 + myAudioIn.right.get(i+1)*200);
      
      line(i*2, 450 + myAudioIn.left.get(i)*200, (i+1)*2, 450 + myAudioIn.left.get(i+1)*600);
      line(i*2, 550 + myAudioIn.right.get(i)*200, (i*2)+20, 550 + myAudioIn.right.get(i+1)*600);
      
      line(i*2, 650 + myAudioIn.left.get(i)*200, (i+1)*2, 650 + myAudioIn.left.get(i+1)*400);
      line(i*2, 750 + myAudioIn.right.get(i)*200, (i*2)+20, 750 + myAudioIn.right.get(i+1)*400);
    }
  }
  
  
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "testPres" });
  }
}
