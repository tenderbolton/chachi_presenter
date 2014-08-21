//import com.nootropic.processing.layers.*;
import ddf.minim.*;
import processing.video.*;
import ddf.minim.analysis.*;

Minim minim;
AudioInput in;
AudioPlayer backNoise;
AudioSample change1;
AudioSample change2;
AudioSample change3;

FFT fft;

int w = 250;
int h = w;
int startX, startY;
int currentLayer=0;
//CustomLayer cubes;
CustomLayer flock;
//CustomLayer extru;
CustomLayer mesh;
CustomLayer audio;
CustomLayer console;
CustomLayer balloons;
Slide[] slides;
int currentSlide=-1;


XML xml;

void setup() {
  size(1024, 768, P3D);
  xml = loadXML("pres.xml");
  //int numSlides = xml.getChildCount();
  XML[] children = xml.getChildren("slide");
  int numSlides=children.length;
  //println(numSlides);
  slides = new Slide[numSlides];
 
  for (int i = 0; i < numSlides; i++) {
    Slide newSlide = new Slide(this);
     
    newSlide.sText= children[i].getContent();
    newSlide.slideType=children[i].getString("type");
    if(children[i].getString("cls").equals("true")){
       newSlide.clearScreen=true;
       //println("true");
    }
     
    if(children[i].getString("hasimage").equals("true")){
       newSlide.hasImage=true;
       newSlide.loadSlideImage(children[i].getString("imagename"));
       //println("true");
    }
    else{
      if(children[i].getString("hasvideo").equals("true")){
        newSlide.hasVideo=true;
        newSlide.loadSlideVideo(children[i].getString("videoname"));
      }
    }
    
    //println(newSlide.sText);    
    slides[i] = newSlide;
  }
  
 // frameRate(12);
  //colorMode(HSB);
  //cursor(CROSS);
 
  PVector v = new PVector((mouseX - 10)*0.2f, (mouseY - 10)*0.2f);
  //println("holasetup");
  //cubes = new CubesWithinCubes(this);
  flock = new Flocking(this);
  //mesh= new Mesh(this);

  balloons= new RedBalloons(this);
  audio= new Sound(this);
  console= new Prompt(this);

  //extru = new Extrusion(this);
   
  //cubes.setDoDraw(false);
  //cubes.setVisible(false);
  //println("holasetup");
   flock.setup();
   //mesh.setup();
   audio.setup();
   balloons.setup();
   console.setup();
 
  flock.setDoDraw(false);
  //mesh.setDoDraw(false);
  audio.setDoDraw(false);
  balloons.setDoDraw(false);
  console.setDoDraw(true);
 
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 512);
  fft = new FFT(in.bufferSize(), in.sampleRate());  
  
  backNoise = minim.loadFile( "nackNoise2.wav");
  backNoise.setVolume(-2.0f);
 change1 = minim.loadSample( "change1.wav", // filename
    512      // buffer size
 );
 change2 = minim.loadSample( "change2.wav", // filename
    512      // buffer size
 );
 change3 = minim.loadSample( "change3.wav", // filename
    512      // buffer size
 );
  //noLoop();
  
  
  
}

void draw() {
  background(0);
  //println("hola");
  fft.forward(in.mix);
  balloons.audioDataUpdate(in,fft);
  audio.audioDataUpdate(in,fft);
  flock.draw();
  //mesh.draw();
  audio.draw();
  hint(ENABLE_DEPTH_TEST);
  balloons.draw();
  hint(DISABLE_DEPTH_TEST);
  if (slides.length>0 && currentSlide!=-1){
    //println(currentSlide);
    if (slides[currentSlide].hasImage){
      slides[currentSlide].drawImage();
    }
    else{
       if (slides[currentSlide].hasVideo){
         slides[currentSlide].drawVideo();
       }
    }
  }
  console.draw();

 
}

void keyPressed() {
  
  if (key == 'a' || key == 'A') {
      flock.setDoDraw(true);
      //mesh.setDoDraw(false);
      balloons.setDoDraw(false);
      audio.setDoDraw(false);
  }
  if (key == 'b' || key == 'B') {
      flock.setDoDraw(false);
      //mesh.setDoDraw(false);
      balloons.setDoDraw(true);
      audio.setDoDraw(false);
    }
    if (key == 'c' || key == 'C') {
      flock.setDoDraw(false);
      //mesh.setDoDraw(false);
      balloons.setDoDraw(false);
      audio.setDoDraw(true);
    }
    
    if (key == 'd' || key == 'D') {
      flock.setDoDraw(false);
      //mesh.setDoDraw(true);
      balloons.setDoDraw(false);
      audio.setDoDraw(false);
    }
    if (key == 'p' || key == 'P') {
      for (int i =0; i<slides.length;i++){
        if (slides[i].hasVideo){
           slides[i].slideVideo.stop();
        }
      }
    }
   if (key == 's' || key == 'S') {
     backNoise.loop();
    }
    if (key == 'n' || key == 'N') {
     backNoise.pause();
    }
    
    
    char[] newChar= new char[1];
    newChar[0]=key;
    String myNewString = new String(newChar);
    //console.addNewText(myNewString);
    
    
    if (key == CODED) {
      int myRandNoise=(int) random(0,3);
      if (keyCode == RIGHT) {
        currentSlide++;
        if (currentSlide>slides.length-1){
          currentSlide--;
        }
        
        if (currentSlide-1>=0){
           slides[currentSlide-1].exitSlide();
        }
        if (slides[currentSlide].clearScreen){
          Prompt myPrompt = (Prompt) console;
          myPrompt.cls();
        }
        
        console.addNewText(slides[currentSlide].sText);
        
        if (slides[currentSlide].hasVideo){
            slides[currentSlide].playMovie();
        }
        setBackground();
        
      } else if (keyCode == LEFT) {
        currentSlide--;
        if (currentSlide+1<slides.length){
           slides[currentSlide+1].exitSlide();
        }
        if (currentSlide<0){
          currentSlide=0;
        }
        if (slides[currentSlide].clearScreen){
          Prompt myPrompt = (Prompt) console;
          myPrompt.cls();
        }
        
        console.addNewText(slides[currentSlide].sText);
        setBackground();
      } 
      
      if (myRandNoise==1){
        change1.trigger();
      }
      else{
        if (myRandNoise==2){
          change2.trigger();
        }
        else{
          change3.trigger();
        }
      }
    } 
    flock.keyPressed();
    //mesh.keyPressed();
    audio.keyPressed();
    balloons.keyPressed();
    console.keyPressed();
    
  
}

void mousePressed() {
 
}

void mouseReleased() {
  
}

void stop()
{
  // always close Minim audio classes when you are done with them
  in.close();
   backNoise.close();
  change1.close();
  change2.close();
  change3.close();
  minim.stop();
  super.stop();
}

void movieEvent(Movie myMovie) {
  myMovie.read();
}

void setBackground(){
  if (slides[currentSlide].slideType.equals("plain")){
    flock.setDoDraw(false);
    audio.setDoDraw(false);
    console.setDoDraw(true);
    balloons.setDoDraw(false);
  }
  else{
    if (slides[currentSlide].slideType.equals("balloons")){
      flock.setDoDraw(false);
      audio.setDoDraw(false);
      console.setDoDraw(true);
      balloons.setDoDraw(true);
    }
    else{
      if(slides[currentSlide].slideType.equals("flock")){
        flock.setDoDraw(true);
        audio.setDoDraw(false);
        console.setDoDraw(true);
        balloons.setDoDraw(false);
      }
      else{
        if(slides[currentSlide].slideType.equals("sound")){
          flock.setDoDraw(false);
          audio.setDoDraw(true);
          console.setDoDraw(true);
          balloons.setDoDraw(false);
        }
        else{
          if(slides[currentSlide].slideType.equals("nothing")){
            flock.setDoDraw(false);
            audio.setDoDraw(false);
            console.setDoDraw(false);
            balloons.setDoDraw(false);
          }
        }
      }
    }
  }
}

