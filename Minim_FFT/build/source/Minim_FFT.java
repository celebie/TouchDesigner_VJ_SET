import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minim_FFT extends PApplet {




Minim minim;
AudioPlayer player; //\u30b5\u30a6\u30f3\u30c9\u30d7\u30ec\u30a4\u30e4\u30fc
FFT fft; //FFT\u30af\u30e9\u30b9
int fftSize; //FFT\u306e\u30b5\u30a4\u30ba

public void setup() {
  
  minim = new Minim(this);
  noStroke();
  //\u6df7\u8272\u306f\u52a0\u7b97\u5408\u6210
  blendMode(ADD);
  //\u8272\u306fHSB\u3067\u6307\u5b9a
  colorMode(HSB, 360, 100, 100, 100);
  //FFT\u30b5\u30a4\u30ba\u8a2d\u5b9a(2\u306e\u51aa\u4e57\u3067\u6307\u5b9a\u3059\u308b\u3053\u3068)
  fftSize = 512;
  //\u30b5\u30a6\u30f3\u30c9\u30d5\u30a1\u30a4\u30eb\u30eb\u30fc\u30d7\u518d\u751f
  player = minim.loadFile("choco.mp3", fftSize);
  player.loop();
  //FFT\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u751f\u6210
  fft = new FFT(player.bufferSize(), player.sampleRate());
}

public void draw() {
  background(0);

  //left ch
  fft.forward(player.left);
  for (int i = 0; i < fft.specSize(); i++) {
    float h = map(i, 0, fft.specSize(), 200, 180);
    float ellipseSize = map(fft.getBand(i), 0, fftSize/16, 0, width);
    float x = map(i, 0, fft.specSize(), width/2, width);
    float w = width/PApplet.parseFloat(fft.specSize())/2;
    noStroke();
    fill(h, 80, 80, 7);
    ellipse(x,height/2,ellipseSize/2,ellipseSize/2);
  }

  //right ch
  fft.forward(player.right);
  for (int i = 0; i < fft.specSize(); i++) {
    float h = map(i, 0, fft.specSize(), 1000, 180);
    float ellipseSize = map(fft.getBand(i), 0, fftSize/16, 0, width);
    float x = map(i, 0, fft.specSize(), width/2, 0);
    float w = width/PApplet.parseFloat(fft.specSize())/2;
    noStroke();
    fill(h, 80, 80, 7);
    ellipse(x,height/2,ellipseSize/2,ellipseSize/2);
  }

  //FFT\u89e3\u6790\u5b9f\u884c
  fft.forward( player.mix );
  //\u30b0\u30e9\u30d5\u751f\u6210
  for (int i = 0; i < fft.specSize(); i++) {
    //\u753b\u9762\u306e\u30b5\u30a4\u30ba\u306b\u5408\u308f\u305b\u3066\u30b9\u30b1\u30fc\u30eb\u3092\u8abf\u6574
    float x = map(i, 0, fft.specSize(),0, width);
    float y = map(fft.getBand(i)/7, 0, 5.0f, height, 0);
    //\u30b0\u30e9\u30d5\u63cf\u753b
    strokeWeight(2.0f);
    //\u8272\u76f8\u3092\u8a2d\u5b9a
    float h = map(i, 0, fft.specSize()/2, 0, 180);
    stroke(h, 100, 100);
    line(x,height,x,y);
  }

}
  public void settings() {  size(1440, 900, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minim_FFT" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
