import processing.video.*;


class Slide{
  
  public String sText;
  public boolean clearScreen;
  public PImage slideImage;
  public boolean hasImage;
  public boolean hasVideo;
  public Movie slideVideo;
  public PApplet p;
  public String slideType;
  
  
  Slide(PApplet ip){
    sText="";
    p=ip;
    slideType="plain";
    clearScreen=false;
    hasImage=false;
    hasVideo=false;
  }
  
  void loadSlideImage(String imageName){
    if (this.hasImage){
      slideImage=loadImage(imageName);
    }
  }
  
  void drawImage(){
    if (this.hasImage){
      image(slideImage, (width-slideImage.width)/2, (height-slideImage.height)/2); 
    }
  }
 
  void loadSlideVideo(String videoName){
    if (this.hasVideo){
      
      this.slideVideo=new Movie(p, videoName);
      this.slideVideo.stop();
      
    }
  }
  
   void playMovie(){
      if (this.hasVideo){
        //slideVideo.loop();
        slideVideo.play();
      }
  }
  
  void drawVideo(){
     if (this.hasVideo){
        float resizeW = width;
        float resizePercent = resizeW/slideVideo.width;
        float resizeH = slideVideo.height * resizePercent;
        int rW = (int) resizeW;
        int rH = (int) resizeH;
        int newYStart= (height - rH)/2;
        
        image(slideVideo, 0, newYStart, rW,rH);
      }
  }
  
  void exitSlide(){
    if (this.hasVideo){
      slideVideo.stop();
    }
  }
 
 
}
