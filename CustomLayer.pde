import ddf.minim.*;
import ddf.minim.analysis.*;

class CustomLayer {
  
  String myId="";
  
  boolean doDraw=false;
  
  AudioInput myAudioIn;
  FFT myFFT;
 
  int totalMillis;
  int millisToFrame;
  
    PApplet p;

 CustomLayer(PApplet parent, String id, boolean enable3D, int myTotalMillis, int myMillisToFrame) {
    myId=id;
    totalMillis=myTotalMillis;
    millisToFrame=myMillisToFrame;
    p=parent;
  }
  
  String getId(){
    return myId;
  }
  
  Boolean getDoDraw(){
     return doDraw;
  }
  
  void setDoDraw(boolean newVal){
     this.doDraw=newVal;
  }
  
  void setup() {
    
  }
  
  void draw() {
    
    if (doDraw){
      int elapsed = millis() - totalMillis;
      if (elapsed>=millisToFrame){
        totalMillis+=elapsed;
        customDraw();
      }
       
    }
  }
  
  
  
  void customDraw(){
  
  }
  
  void audioDataUpdate(AudioInput in, FFT newFFT){
    myAudioIn=in;
    myFFT=newFFT;
  }
  
  void addNewText(String myNewText){
    
  }
  
  void keyPressed(){
  }
  
}
