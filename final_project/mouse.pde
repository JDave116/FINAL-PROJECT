void mouseReleased() {
  if (mode == INTRO){
    mode = GAME;
  }else if (mode == GAME){
    if (!bulletActive){
      bulletActive = true;
      lockedX  = x1;
      bullety = y1+10;
    }else if (mode == GAMEOVER){
      resetGame();
      mode = GAME;
    }
  }
  
  if(mode == PAUSE){
    mode = GAME;
  }
  
  if(mode == GAMEOVER){
    mode = INTRO;
  }
}
