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
 
import megamu.mesh.*;

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
 
void setup() {
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
void customDraw() {
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
 
void keyPressed() {
  if (key =='r') {
    for (int i=0;i<count;i++) {
      particles[i].reset();
    }
  }
}

}

