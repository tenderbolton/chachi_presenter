import ddf.minim.analysis.*;
import ddf.minim.*;

class RedBalloons extends CustomLayer{

PVector[] balloons;
color[] balloonsColor;
float rotY;
float MIC_RADIUS = 20;

 RedBalloons (PApplet parent) {
    super(parent,"RedBalloons",false,0,0);
  }

void setup(){
  rotY=0.0f;
  
  balloons= new PVector[150];
  balloonsColor= new color[150];
  PVector origin = new PVector(0,0,0);
  for (int i=0; i<balloons.length;i++){
    PVector myRandomVector = getRandomVector(origin,balloons, i);
    balloons[i]=myRandomVector;
    balloonsColor[i]=  color(255,255,255);
  }

}

void customDraw(){
  pushMatrix();
   fft.forward(in.mix);
  float mainIntensity = 0;
  
  for(int i = 0; i < fft.specSize(); i++)  {
    float band = fft.getBand(i);
    if (mainIntensity<band){
      mainIntensity = band;
    }
  }
    
  //println(mainIntensity);

  PVector orig = new PVector(0.0f,0.0f,0.0f);
  applyColor(orig, color(255,0,0), mainIntensity);      

  background(0);
  noStroke();
  resetMatrix();
  translate(0.0f,0.0f,-150.0f);
  rotateY(rotY);
  scale(3.0f);
  for (int i=0; i<balloons.length;i++){
    pushMatrix();
    translate(balloons[i].x,balloons[i].y,balloons[i].z);
    fill(balloonsColor[i]);
    //fill(255,255,2455);
    sphere(1.0f);
    popMatrix();
  }
  stroke(1);
  rotY+=0.005;
  popMatrix();
}

PVector getRandomVector(PVector origin,PVector[] balloons,int i){
   PVector result= new PVector(random(-14.0f,14.0f),random(-14.0f,14.0f),random(-14.0f,14.0f));
   //println(result.x + "," + result.y );
   return result;
 }

void applyColor(PVector origin, color col, float intensity) {
  if (true) {
    for (int i=0; i<balloons.length; i++) {
      float dist = origin.dist(balloons[i]);
      float factor = max(MIC_RADIUS - dist, 0);
      factor /= dist;
      factor /= 450;
      factor *= intensity;
      //println(factor);
      float alph = 0.99f;
      float newRed = (red(balloonsColor[i]) * alph) + ((255*factor*10)*1-alph);
      if (newRed>255){
        newRed=255;
      }
      int intNewRed = (int) newRed;
      balloonsColor[i]=color(intNewRed,0,0);
      //println(red(col));
    }
  }  
}
}

