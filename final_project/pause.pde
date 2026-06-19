void pause(){
  fill(0); 
  rect(0, 0, width, height);
  
  textAlign(CENTER, CENTER);
  fill(255);
  textSize(40);
  text("PAUSED", width/2, height/2);
  textSize(18);
  text("Click anywhere to resume", width/2, height/2 + 40);
}

void pauseclicks(){
  mode = GAME;
}
