void game() {
  drawBG();

  ////P1//
  fill(255);
  noStroke();
  circle(x1, y1, d);
 
  
   if (p1) {
    fill(0, 100, 255); // Blue for P1
    if (!bulletActive) {
      if (akey && x1 > maxleft) x1 -= 4;
      if (dkey && x1 < maxright) x1 += 4;
    }
  } else {
    fill(255, 0, 100); // Pink/Red for P2
    if (!bulletActive) {
      if (akey && x1 > maxleft) x1 -= 4;
      if (dkey && x1 < maxright) x1 += 4;
    }
  }
  
  drawScore();
  
  drawtarget();
  
  if (bulletActive) {
    shoot(); 
  }
  
  if (p1score >= 5 || p2score >= 5){
    mode = GAMEOVER; 
  }

}
