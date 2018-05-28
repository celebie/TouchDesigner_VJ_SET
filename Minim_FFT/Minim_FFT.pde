import codeanticode.syphon.*;

import ddf.minim.*;
import ddf.minim.analysis.*;

Minim minim;
AudioPlayer player; //サウンドプレイヤー
AudioInput in;  //マイク入力用の変数
FFT fft; //FFTクラス
int fftSize; //FFTのサイズ

SyphonServer server; // syphonサーバーを立ち上げる

void settings() {
  size(720, 450, P3D);
  PJOGL.profile=1;
}

void setup() {
  minim = new Minim(this);
  //バッファ（メモリ上のスペース。この場合は512要素のfloat型の配列）を確保し、マイク入力用の変数inを設定する。
  in = minim.getLineIn(Minim.STEREO, 512);
  
  server = new SyphonServer(this, "Processing Syphon");
  
  noStroke();
  //混色は加算合成
  blendMode(ADD);
  //色はHSBで指定
  colorMode(HSB, 360, 100, 100, 100);
  //FFTサイズ設定(2の冪乗で指定すること)
  fftSize = 512;
  //サウンドファイルループ再生
  //player = minim.loadFile("choco.mp3", fftSize);
  //player.loop();
  
  //FFTオブジェクトの生成
  //fft = new FFT(player.bufferSize(), player.sampleRate());
  fft = new FFT(in.bufferSize(), in.sampleRate());
}

void draw() {
  server.sendScreen(); // syphonサーバーに映像を送る
  background(0);

  //left ch
  //fft.forward(player.left);
  fft.forward(in.left);
  for (int i = 0; i < fft.specSize(); i++) {
    float h = map(i, 0, fft.specSize(), 200, 180);
    float ellipseSize = map(fft.getBand(i)*2, 0, fftSize/16, 0, width);
    float x = map(i, 0, fft.specSize(), width/2, width);
    float w = width/float(fft.specSize())/2;
    noStroke();
    fill(h, 80, 80, 7);
    ellipse(x,height/2,ellipseSize/2,ellipseSize/2);
  }

  //right ch
  //fft.forward(player.right);
  fft.forward(in.right);
  for (int i = 0; i < fft.specSize(); i++) {
    float h = map(i, 0, fft.specSize(), 1000, 180);
    float ellipseSize = map(fft.getBand(i)*2, 0, fftSize/16, 0, width);
    float x = map(i, 0, fft.specSize(), width/2, 0);
    float w = width/float(fft.specSize())/2;
    noStroke();
    fill(h, 80, 80, 7);
    ellipse(x,height/2,ellipseSize/2,ellipseSize/2);
  }
}
