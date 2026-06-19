int mode;
final int INTRO = 0;
final int GAME = 1;
final int PAUSE = 2;
final int GAMEOVER = 3;
boolean singleplayer;
int x1, y1, d;
float targetX, targetY, targetD;
boolean akey, dkey;
int maxleft, maxright;
int bullety, bulletd;
boolean bulletActive = false;
int lockedX;
boolean p1 = true;
boolean p2 = false;
int p1score = 0;
int p2score = 0;
boolean target = true;


void setup() {
  size(500, 700);
  mode = INTRO;
  textAlign(CENTER, CENTER);
  singleplayer = true;
  x1 = 250;
  y1 = 100;
  d = 20;
  bullety = y1+10;
  bulletd = 10;

  targetX = random(50, 450);
  targetY = random(300, 660);
  targetD = random(8, 50);
  maxleft = 50;
  maxright = 450;
}

void draw() {
  if (mode == INTRO) {
    intro();
  } else if (mode == GAME) {
    game();
  } else if (mode == PAUSE) {
    pause();
  } else if (mode == GAMEOVER) {
    gameover();
  } else {
    println("ERROR : MODE = " + mode);
  }
}


void drawScore() {
  textAlign(CENTER, CENTER);
  textSize(16);
  fill(0, 100, 255);
  text("P1 Score: " + p1score, 50, 50);
  text("P2 Score: " + p2score, 400, 60);
}


void shoot() {
  fill(255, 255, 0);
  noStroke();
  circle(lockedX, bullety, bulletd);

  bullety += 5;

  // 1. FIXED TYPO: targetD changed to targetX
  float distance = dist(lockedX, bullety, targetX, targetY); 
  
  if (distance < (bulletd/2 + targetD/2)) {
    if (p1) {
      p1score += 1;
    } else {
      p2score += 1;
    }

    // 2. ADDED: Respawn the target somewhere fresh for the next player
    targetX = random(50, 450);
    targetY = random(300, 660);
    targetD = random(8, 50);

    bulletActive = false;
    swapplayers();
  }

  if (bullety > height) {
    bulletActive = false;
    swapplayers();
  }
}


void swapplayers() {
  if (p1) {
    p1 = false;
    p2 = true;
  } else {
    p1 = true;
    p2 = false;
  }
}

void resetGame() {
  x1 = 250;
  y1 = 100;
  targetX = width / 2;
  targetY = 100;
  p1score = 0;
  p2score = 0;
  p1 = true;
  p2 = false;
  bulletActive = false;
}

void drawtarget() {
  if (target == true) {
    if (p1) {
      fill(255, 0, 0);
      circle(targetX, targetY, targetD);

      float distance = dist(lockedX, bullety, targetX, targetY);
      if (distance < (bulletd/2 + targetD/2)) {
        target = false;
      }
    } else {
      fill(0, 0, 255);
      circle(targetX, targetY, targetD);

      float distance = dist(lockedX, bullety, targetX, targetY);
      if (distance < (bulletd/2 + targetD/2)) {
        target = false;
      }
    }
  }
  
  if(target == false){
    circle(targetX, targetY, targetD);
    target = true;
  }
}






//void gameclicks() {
//  if (!bulletActive) {
//    bulletActive = true;
//    lockedX = x1;       
//    bullety = y1;       
    
//    // Swap players HERE so it only happens ONCE per click!

//  }
//}




void drawBG() {
  background(40, 40, 40);


  fill(30, 140, 30);
  noStroke();
  rect(0, 0, 50, height);
  rect(width-50, 0, 50, height);

  stroke(255);
  strokeWeight(4);
  line(50, 0, 50, height);
  line(width-50, 0, width -50, height);
}
