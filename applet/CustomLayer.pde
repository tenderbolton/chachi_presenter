import ddf.minim.*;


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
  
  void audioDataUpdate(AudioInput in){
    myAudioIn=in;
  
  }
  
  void addNewText(String myNewText){
    
  }
  
}
