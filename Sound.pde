
class Sound extends CustomLayer{
  
   Sound(PApplet parent) {
    super(parent,"Sound",false,0,0);
  }
  
  
  void setup()
  {

  }
  
  void customDraw()
  {
    //background(50,0);
    stroke(255);
    
    // draw the waveforms
    for(int i = 0; i < in.bufferSize() - 1; i++)
    {
      //line(i*2, 50 + myAudioIn.left.get(i)*200, (i+1)*2, 50 + myAudioIn.left.get(i+1)*400);
      //line(i*2, 150 + myAudioIn.right.get(i)*200, (i*2)+20, 150 + myAudioIn.right.get(i+1)*400);
      
      line(i*2, 250 + myAudioIn.left.get(i)*200, (i+1)*2, 250 + myAudioIn.left.get(i+1)*200);
      line(i*2, 350 + myAudioIn.right.get(i)*200, (i*2)+20, 350 + myAudioIn.right.get(i+1)*200);
      
      line(i*2, 450 + myAudioIn.left.get(i)*200, (i+1)*2, 450 + myAudioIn.left.get(i+1)*600);
      line(i*2, 550 + myAudioIn.right.get(i)*200, (i*2)+20, 550 + myAudioIn.right.get(i+1)*600);
      
      //line(i*2, 650 + myAudioIn.left.get(i)*200, (i+1)*2, 650 + myAudioIn.left.get(i+1)*400);
      //line(i*2, 750 + myAudioIn.right.get(i)*200, (i*2)+20, 750 + myAudioIn.right.get(i+1)*400);
    }
  }
  
  
}
