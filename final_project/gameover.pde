void gameover(){
  background(0);
  
  if(p1score> p2score){
    text("Player 1 wins", 250,300);
  }else{
    text("Player 2 wins", 250,300);
  }
}

void gameoverclicks(){
  mode = INTRO;
}
