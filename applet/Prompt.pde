
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
  
  void setup(){
    
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
  
  void customDraw(){
    // Use fill() to change the value or color of the text
    background(50,0);
    int interval = millis() - mainElapsedTime;
    mainElapsedTime += interval;
    myUpdate(interval);
    for (int i=0; i<console.length;i++){
      text(console[i], xBase, yBase + (yStep*i));
    }
  
  }
  
  void keyPressed() {
    promptAdvance();
  }
  
  void stop()
  {
  
  }
  
  
  
  void myUpdate(int interval){
    type(interval);
    drawPrompt(interval);
  }
  
  boolean mustDrawPrompt=false;
  
  void drawPrompt(int interval){
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
  
  void type(int interval){
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
  
  void promptAdvance(){
     promptX+=1; 
     if (promptX>=lineMaxChars){
       promptReturn();
     }
  }
  
  void promptReturn(){
    promptX=0;
    appendLine(promptLine);
    promptLine="";
    
  }
  
  void appendLine(String newLine){
    for (int i = 0 ; i < console.length ; i++){
      if (i==console.length-1){
        console[i]=newLine;
      }
      else{
        console[i]=console[i+1];
      }
    }
  }
  
  void cls(){
    for (int i = 0 ; i < console.length ; i++){
        console[i]="";
    }
    promptLine="";
  }
  
  void setToType(String newLine){
    toType=newLine;
  }
  
  void addNewText(String myNewText){
    toType += myNewText;
  }

}

