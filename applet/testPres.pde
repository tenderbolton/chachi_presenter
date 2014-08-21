import com.nootropic.processing.layers.*;
import ddf.minim.*;

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

void setup() {
  
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

void paint(java.awt.Graphics g) {
  if (layers != null) {
    layers.paint(this);
  } 
  else {
    super.paint(g);
  }
}

void draw() {
 background(50);
 audio.audioDataUpdate(in);
 
}

void keyPressed() {
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

void mousePressed() {
 
}

void mouseReleased() {
  
}

void stop()
{
  // always close Minim audio classes when you are done with them
  in.close();
  minim.stop();
  
  super.stop();
}
