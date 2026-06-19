void keyPressed() {
  if ((mode == INTRO) && key == '1') {
    singleplayer = true;
    mode = GAME;
  } else if ((mode == INTRO) && key == '2') {
    singleplayer = false;
    mode = GAME;
  }


  if (key == 'a' || key == 'A') akey = true;
  if (key == 'd' || key == 'D') dkey = true;
  
  // Pause toggle
  if (key == 'p' || key == 'P') {
    if (mode == GAME) {
      mode = PAUSE;
    } else if (mode == PAUSE) {
      mode = GAME;
    }
  }
}


void keyReleased(){
  
  if (mode == GAME) {
    if (key == 'a' || key == 'A') {
      akey = false;
    } else if (key =='d' || key == 'D') {
      dkey = false;
    }
  }
}
