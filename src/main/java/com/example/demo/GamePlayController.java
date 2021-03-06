package com.example.demo;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GamePlayController implements Initializable {
    private GameMain myGame = GameMain.getInstance();
    private ArrayList<ImageView> myPlatformImages = new ArrayList<>();
    private ArrayList<Pair<ImageView, Integer>> myObjWithType = new ArrayList<>();

    //    ---------------------------------------- Animations ------------------------------------------

    @FXML
    ImageView heroHome;
    @FXML
    ImageView greenOrcHome;
    @FXML
    ImageView redOrcHome;

    @FXML
    ImageView heroGamePlay;
    @FXML
    ImageView platform1;
    @FXML
    ImageView platform2;
    @FXML
    ImageView platform3;
    @FXML
    ImageView platform4;
    @FXML
    ImageView platform5;
    @FXML
    ImageView greenOrcGamePlay;
    @FXML
    ImageView redOrcGamePlay;
    @FXML
    ImageView bossOrcGamePlay;
    @FXML
    ImageView princessGamePlay;
    @FXML
    ImageView throwingKnifeGamePlay;
    @FXML
    ImageView swordGamePlay;
    @FXML
    ImageView coinChest;
    @FXML
    ImageView weaponChest;
    @FXML
    ImageView tnt;
    @FXML
    ImageView blastGame;
    @FXML
    ImageView finalCoinChest;
    @FXML
    ImageView swordLogo;
    @FXML
    ImageView knivesLogo;
    @FXML
    ImageView winGameOver;

    @FXML
    Label coinsCollectedGamePlay;
    @FXML
    Label pointsScoredGamePlay;
    @FXML
    Button pauseBtnGamePlay;


    @FXML
    Button closeBtnPauseGame;
    @FXML
    Button playBtnPauseGame;
    @FXML
    Button reviveBtnGameOver;
    @FXML
    Button skipBtnGameOver;

    private int p_go = 10;
    private int p_ro = 100;
    private int p_bo = 50;
    private boolean orc_falling = false;
    private boolean tnt_active = false;
    private boolean sword_active = false;
    private boolean knives_active = false;
    private boolean coin_touched = false;
    private boolean weapon_touched = false;
    private boolean game_done = false;
    private boolean game_over = false;
    private boolean moveX = false;
    RotateTransition coinRotate = new RotateTransition();
    ScaleTransition scaleWinLogo = new ScaleTransition();


    PlayMusic btnClick = new PlayMusic("src/main/resources/assets/Audio/btnClick.wav");
    PlayMusic gameStart = new PlayMusic("src/main/resources/assets/Audio/game.mp3");
    PlayMusic chestSound = new PlayMusic("src/main/resources/assets/Audio/chest.wav");
    PlayMusic gameOverSound = new PlayMusic("src/main/resources/assets/Audio/gameOver.wav");
    PlayMusic gameWinSound = new PlayMusic("src/main/resources/assets/Audio/gameWin.wav");
    PlayMusic moveHeroSound = new PlayMusic("src/main/resources/assets/Audio/moveHero.wav");
    PlayMusic orcDieSound = new PlayMusic("src/main/resources/assets/Audio/orcDie.wav");
    PlayMusic tntSound = new PlayMusic("src/main/resources/assets/Audio/tnt.wav");

    public void game_over() throws IOException {
        gameOverSound.play();
        timeline.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_over.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 380);
        Stage stage = (Stage) heroGamePlay.getScene().getWindow();
        if(stage!=null){
            myGame.getMyMusic().stop();
            myGame.setMyMusic(new PlayMusic("src/main/resources/assets/Audio/relax.mp3"));
            myGame.getMyMusic().play();

            stage.setTitle("Will Hero - Game Over");
            stage.setScene(scene);
        }
    }

    public void show_result_game_win() throws IOException {
        myGame.setMyMusic(new PlayMusic("src/main/resources/assets/Audio/relax.mp3"));
        myGame.getMyMusic().play();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_result.fxml"));
        Scene scene = null;
        scene = new Scene(fxmlLoader.load(), 680, 380);
        Stage stage = (Stage) princessGamePlay.getScene().getWindow();
        stage.setTitle("Will Hero - Game Result");
        stage.setScene(scene);
    }

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>(){
        double deltaY = 0.8;
        double g = 0.02;
        double deltaX = 3;
        double deltaXR = 1.5;
        @Override
        public void handle(ActionEvent actionEvent) {
            if(game_done==true){
                if(myGame.getMyMusic()!=null){
                    myGame.getMyMusic().stop();
                    myGame.setMyMusic(null);
                    gameWinSound.play();
                }
                if(heroGamePlay.getLayoutX()<princessGamePlay.getLayoutX()-100){
                    heroGamePlay.setLayoutX(heroGamePlay.getLayoutX() + 0.5);
                }
                else{
                    try {
                        timeline.stop();
                        show_result_game_win();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(game_over){
                game_over = false;
                try {
                    timeline.stop();
                    game_over();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(moveX){
                if(myGame.getCurrPlayer().getMoveX()==1){
                    heroGamePlay.setLayoutX(heroGamePlay.getLayoutX()+deltaX);
                    if(sword_active==false) {
                        swordGamePlay.setLayoutX(heroGamePlay.getLayoutX() - 50);
                    }
                    if(heroGamePlay.getLayoutX()>=80){
                        myGame.getCurrPlayer().toggleMoveX();
                    }
                }
                else{
                    heroGamePlay.setLayoutX(heroGamePlay.getLayoutX()-deltaXR);
                    if(sword_active==false) {
                        swordGamePlay.setLayoutX(heroGamePlay.getLayoutX() - 50);
                    }
                    if(heroGamePlay.getLayoutX()<=60){
                        moveX = false;
                        myGame.getCurrPlayer().toggleMoveX();
                    }
                    return;
                }
            }
            heroGamePlay.setLayoutY(heroGamePlay.getLayoutY() + deltaY);
            swordGamePlay.setLayoutY(heroGamePlay.getLayoutY()+30);
            deltaY += g;
            int isCollided = -1;
            try {
                isCollided = checkCollision();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (isCollided == 0) {
                deltaY *= -1;
                myGame.getCurrPlayer().setPrevStart((int) heroGamePlay.getLayoutY());
            } else if (heroGamePlay.getLayoutY() + myGame.getCurrPlayer().getJumpHeight() < myGame.getCurrPlayer().getPrevStart()) {
                deltaY *= -1;
            }
        }
    }));

    public void makeVisible(ImageView img_v, Platform p){
        img_v.setVisible(true);
        img_v.setLayoutX(p.getInf().getX()+p.getInf().getLen()/2);
        img_v.setLayoutY(p.getInf().getY()-img_v.getFitHeight()+1);
    }

    public void makeHide(ImageView img_v, int type){
        for (int i = 0; i < myObjWithType.size(); i++) {
            if (myObjWithType.get(i).getKey() == img_v && myObjWithType.get(i).getValue() == type) {
                myObjWithType.remove(i);
                break;
            }
        }
        img_v.setVisible(false);
    }

    public void setupPlatform(int idx){
        int i = (idx%4 >= 0 ? idx%4: idx%4 + 4);

//        hide obj previously placed
        if(myObjWithType.size()!=0 && myObjWithType.get(0).getKey().getLayoutX()<0){
            makeHide(myObjWithType.get(0).getKey(), myObjWithType.get(0).getValue());
        }

//        setup platform to extreme right
        myGame.getCurrPlatforms().get(i).getInf().setX(myGame.getCurrPlatforms().get(i-1<0?3:i-1).getInf().getX() + (i-1<0?280-40:280));
        myPlatformImages.get(i).setLayoutX(myGame.getCurrPlatforms().get(i).getInf().getX());
        Platform p = myGame.getCurrPlatforms().get(i);

        switch (myGame.getObjOnPlatforms().get(idx)){
            // 0 - E; 1 - GO; 2 - RO; 3 - CC; 4 - WC; 5 - TNT; 6 - B,FC,P
            case 1:
                p_go = 1;
                makeVisible(greenOrcGamePlay, p);
                myObjWithType.add(new Pair(greenOrcGamePlay, 1));
                break;
            case 2:
                p_ro = 2;
                makeVisible(redOrcGamePlay, p);
                myObjWithType.add(new Pair(redOrcGamePlay, 2));
                break;
            case 3:
                makeVisible(coinChest, p);
                myObjWithType.add(new Pair(coinChest, 3));
                break;
            case 4:
                makeVisible(weaponChest, p);
                myObjWithType.add(new Pair(weaponChest, 4));
                break;
            case 5:
                makeVisible(tnt, p);
                myObjWithType.add(new Pair(tnt, 5));
                break;
        }
    }

    public void degradePlatform(int i){
        myPlatformImages.get(i).setVisible(false);
    }

    public void lastStep(){
        platform5.setLayoutX(platform4.getLayoutX()+280);
        platform5.setLayoutY(240);
        platform5.setFitWidth(platform5.getFitWidth()+20);
        platform5.setVisible(true);

        bossOrcGamePlay.setVisible(true);
        bossOrcGamePlay.setLayoutX(platform5.getLayoutX()+60);
        bossOrcGamePlay.setLayoutY(platform5.getLayoutY()-bossOrcGamePlay.getFitHeight()+1);
        princessGamePlay.setVisible(true);
        princessGamePlay.setLayoutX(platform5.getLayoutX()+250);
        princessGamePlay.setLayoutY(platform5.getLayoutY()-princessGamePlay.getFitHeight()+1);
        finalCoinChest.setVisible(true);
        finalCoinChest.setLayoutX(platform5.getLayoutX()+340);
        finalCoinChest.setLayoutY(platform5.getLayoutY()-finalCoinChest.getFitHeight());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(myGame.getMyMusic()==null){
            myGame.setMyMusic(gameStart);
            gameStart.play();
        }
//        Green orc
        {
            TranslateTransition translateGOrc = new TranslateTransition();
            translateGOrc.setNode(greenOrcGamePlay);
            translateGOrc.setDuration(Duration.millis(800));
            translateGOrc.setCycleCount(TranslateTransition.INDEFINITE);
            translateGOrc.setByY(-60);
            translateGOrc.setAutoReverse(true);
            translateGOrc.play();
        }
//        Red orc
        {
            TranslateTransition translateROrc = new TranslateTransition();
            translateROrc.setNode(redOrcGamePlay);
            translateROrc.setDuration(Duration.millis(800));
            translateROrc.setCycleCount(TranslateTransition.INDEFINITE);
            translateROrc.setByY(-60);
            translateROrc.setAutoReverse(true);
            translateROrc.play();
        }
//        Boss orc
        {
            TranslateTransition translateROrc = new TranslateTransition();
            translateROrc.setNode(bossOrcGamePlay);
            translateROrc.setDuration(Duration.millis(800));
            translateROrc.setCycleCount(TranslateTransition.INDEFINITE);
            translateROrc.setByY(-40);
            translateROrc.setAutoReverse(true);
            translateROrc.play();
        }
//        Princess
        {
            TranslateTransition translateROrc = new TranslateTransition();
            translateROrc.setNode(princessGamePlay);
            translateROrc.setDuration(Duration.millis(800));
            translateROrc.setDelay(Duration.millis(400));
            translateROrc.setCycleCount(TranslateTransition.INDEFINITE);
            translateROrc.setByY(-50);
            translateROrc.setAutoReverse(true);
            translateROrc.play();
        }
////        Throw Knives Game Play
//        {
//            TranslateTransition translateKnivesGamePlay = new TranslateTransition();
//            translateKnivesGamePlay.setNode(throwKnivesGamePlay);
//            translateKnivesGamePlay.setDuration(Duration.millis(1600));
//            translateKnivesGamePlay.setCycleCount(TranslateTransition.INDEFINITE);
//            translateKnivesGamePlay.setByX(150);
//            translateKnivesGamePlay.play();
//        }
//        Play Btn Home
        {
            ScaleTransition scalePlayBtnPauseGame = new ScaleTransition();
            scalePlayBtnPauseGame.setNode(playBtnPauseGame);
            scalePlayBtnPauseGame.setDuration(Duration.millis(500));
            scalePlayBtnPauseGame.setCycleCount(ScaleTransition.INDEFINITE);
            scalePlayBtnPauseGame.setByX(0.12);
            scalePlayBtnPauseGame.setByY(0.12);
            scalePlayBtnPauseGame.setInterpolator(Interpolator.LINEAR);
            scalePlayBtnPauseGame.setAutoReverse(true);
            scalePlayBtnPauseGame.play();
        }
//        Close Btn Load game
        {
            ScaleTransition scaleCloseBtnPauseGame = new ScaleTransition();
            scaleCloseBtnPauseGame.setNode(closeBtnPauseGame);
            scaleCloseBtnPauseGame.setDuration(Duration.millis(500));
            scaleCloseBtnPauseGame.setCycleCount(ScaleTransition.INDEFINITE);
            scaleCloseBtnPauseGame.setByX(0.15);
            scaleCloseBtnPauseGame.setByY(0.15);
            scaleCloseBtnPauseGame.setInterpolator(Interpolator.LINEAR);
            scaleCloseBtnPauseGame.setAutoReverse(true);
            scaleCloseBtnPauseGame.play();
        }
//        Revive Btn Game Over
        {
            RotateTransition rotateReviveBtnGameOver = new RotateTransition();
            rotateReviveBtnGameOver.setNode(reviveBtnGameOver);
            rotateReviveBtnGameOver.setDuration(Duration.millis(2000));
            rotateReviveBtnGameOver.setCycleCount(ScaleTransition.INDEFINITE);
            rotateReviveBtnGameOver.setToAngle(-359);
            rotateReviveBtnGameOver.setInterpolator(Interpolator.LINEAR);
            rotateReviveBtnGameOver.play();
        }

//        Win Logo Game Over
        {
            scaleWinLogo.setNode(winGameOver);
            scaleWinLogo.setDuration(Duration.millis(700));
            scaleWinLogo.setCycleCount(ScaleTransition.INDEFINITE);
            scaleWinLogo.setByX(0.16);
            scaleWinLogo.setByY(0.16);
            scaleWinLogo.setInterpolator(Interpolator.LINEAR);
            scaleWinLogo.setAutoReverse(true);
            scaleWinLogo.play();
        }

        if(heroGamePlay!=null){
            heroGamePlay.setLayoutX(myGame.getCurrPlayer().getCurrX());
            swordGamePlay.setLayoutX(heroGamePlay.getLayoutX()-50);
            heroGamePlay.setLayoutY(130);
            swordGamePlay.setLayoutY(heroGamePlay.getLayoutY()+30);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
//        Set platform images
        if(platform1!=null){
            platform5.setVisible(false);
            redOrcGamePlay.setVisible(false);
            greenOrcGamePlay.setVisible(false);
            bossOrcGamePlay.setVisible(false);
            princessGamePlay.setVisible(false);
            coinChest.setVisible(false);
            weaponChest.setVisible(false);
            finalCoinChest.setVisible(false);
            throwingKnifeGamePlay.setVisible(false);
            swordGamePlay.setVisible(false);
            tnt.setVisible(false);
            blastGame.setVisible(false);
            winGameOver.setVisible(false);

            myPlatformImages = new ArrayList<>();
            myObjWithType = new ArrayList<>();
            platform1.setLayoutX(myGame.getCurrPlatforms().get(0).getInf().getX());
            platform1.setLayoutY(myGame.getCurrPlatforms().get(0).getInf().getY());
            myPlatformImages.add(platform1);
            platform2.setLayoutX(myGame.getCurrPlatforms().get(1).getInf().getX());
            platform2.setLayoutY(myGame.getCurrPlatforms().get(1).getInf().getY());
            myPlatformImages.add(platform2);
            platform3.setLayoutX(myGame.getCurrPlatforms().get(2).getInf().getX());
            platform3.setLayoutY(myGame.getCurrPlatforms().get(2).getInf().getY());
            myPlatformImages.add(platform3);
            platform4.setLayoutX(myGame.getCurrPlatforms().get(3).getInf().getX());
            platform4.setLayoutY(myGame.getCurrPlatforms().get(3).getInf().getY());
            myPlatformImages.add(platform4);

            if(myGame.getDoneTill()==4) {
                makeVisible(greenOrcGamePlay, myGame.getCurrPlatforms().get(2));
                myObjWithType.add(new Pair(greenOrcGamePlay, 1));
                makeVisible(coinChest, myGame.getCurrPlatforms().get(3));
                myObjWithType.add(new Pair(coinChest, 3));
            }
            if(myGame.getCurrPlayer().getHero().getMyHelmet().getWeaponCnt()>=2){
                if(myGame.getCurrPlayer().getHero().getMyHelmet().getSelectedIdx()==1){
                    swordGamePlay.setVisible(true);
                }
                swordLogo.setVisible(true);
                knivesLogo.setVisible(true);
            }
            else{
                if(myGame.getCurrPlayer().getHero().getMyHelmet().getSelectedIdx()==0) {
                    knivesLogo.setVisible(true);
                    swordLogo.setVisible(false);
                }
                else if(myGame.getCurrPlayer().getHero().getMyHelmet().getSelectedIdx()==1){
                    knivesLogo.setVisible(false);
                    swordLogo.setVisible(true);
                    swordGamePlay.setVisible(true);
                }
                else{
                    swordLogo.setVisible(false);
                    knivesLogo.setVisible(false);
                }
            }

            pauseBtnGamePlay.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.RIGHT) {
                    move();
                }
            });

        }
        if(pointsScoredGamePlay!=null){
            pointsScoredGamePlay.setText(Integer.toString((int)myGame.getCurrPlayer().getPoints()));
        }
        if(coinsCollectedGamePlay!=null){
            coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
        }
    }

    public void throw_knives(){
        throwingKnifeGamePlay.setLayoutY(heroGamePlay.getLayoutY()+30);
        throwingKnifeGamePlay.setLayoutX(heroGamePlay.getLayoutX()+80);
        throwingKnifeGamePlay.setVisible(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
//                        for (int i = 0; i < 20; i++) {
//                            throwingKnifeGamePlay.setLayoutX(throwingKnifeGamePlay.getLayoutX()+10);
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
                        throwingKnifeGamePlay.setVisible(false);
                        knives_active = false;
                    }
                },
                200
        );
    }

    public void rotate_sword(){
        swordGamePlay.setLayoutX(swordGamePlay.getLayoutX()+60);
        RotateTransition r_s = new RotateTransition();
        r_s.setNode(swordGamePlay);
        r_s.setDuration(Duration.millis(500));
        r_s.setFromAngle(0);
        r_s.setToAngle(360);
        r_s.play();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                       sword_active = false;
                    }
                },
                550
        );

    }

    public void check_collision_orcs(ImageView orc){
        if(sword_active==false && swordGamePlay.isVisible()){
            sword_active = true;
            rotate_sword();
        }
        if(orc_falling==false && ((heroGamePlay.getLayoutY()>orc.getLayoutY()-20 && orc!=bossOrcGamePlay) || (heroGamePlay.getLayoutY()>orc.getLayoutY()+orc.getFitHeight()/3 && orc==bossOrcGamePlay))){
            swordGamePlay.setVisible(false);
            Path path = new Path();
            path.getElements().add(new MoveTo(0,0));
            ArcTo myArc = new ArcTo(); myArc.setX(0); myArc.setY(250); myArc.setRadiusX(20); myArc.setRadiusY(80);
            path.getElements().add(myArc);

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1200));
            pathTransition.setPath(path);
            pathTransition.setNode(heroGamePlay);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.play();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            game_over = true;
                        }
                    },
                    1200
            );
        }
    }

    public void fall_orcs(ImageView orc)  {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            orc.setLayoutX(orc.getLayoutX()+5);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                1
        );
        TranslateTransition fall_Orc = new TranslateTransition();
        fall_Orc.setNode(orc);
        fall_Orc.setDuration(Duration.millis(1200));
        fall_Orc.setCycleCount(1);
        fall_Orc.setByY(500);
        fall_Orc.play();
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    orc_falling = false;
                    orc.setVisible(false);
                    orc.setLayoutY(500);
                    orc.setLayoutX(orc.getLayoutX()-100);
                }
            },
        1200
        );
    }

    public void fall_orcs_knives(ImageView orc)  {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            orc.setLayoutX(orc.getLayoutX()+2);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                1
        );
        TranslateTransition fall_Orc = new TranslateTransition();
        fall_Orc.setNode(orc);
        fall_Orc.setDuration(Duration.millis(1200));
        fall_Orc.setCycleCount(1);
        fall_Orc.setByY(500);
        fall_Orc.play();
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    orc_falling = false;
                    orc.setVisible(false);
                    orc.setLayoutY(500);
                    orc.setLayoutX(orc.getLayoutX()-40);
                }
            },
        1200
        );
    }

//    0 for normal with platform; 1 for orc (win); 2 for orc (lose); 3 for coins chest; 4 for weapon chest
    public int checkCollision() throws IOException, InterruptedException {
//        fall
        if(heroGamePlay.getLayoutY()>260){
            timeline.stop();
            game_over();
        }
//        collision with platforms
        for (int i = 0; i < 4; i++) {
            if(heroGamePlay.getBoundsInParent().intersects(myPlatformImages.get(i).getBoundsInParent())){
                if((heroGamePlay.getLayoutY()+heroGamePlay.getFitHeight()) - myPlatformImages.get(i).getLayoutY() < 5){
                    return 0;
                }
            }
        }
//        last platform
        if(myGame.getDoneTill()>=37){
            if(heroGamePlay.getBoundsInParent().intersects(platform5.getBoundsInParent())){
                if((heroGamePlay.getLayoutY()+heroGamePlay.getFitHeight()) - platform5.getLayoutY() < 7){
                    return 0;
                }
            }
        }
//        collision with orcs
        if(heroGamePlay.getBoundsInParent().intersects(greenOrcGamePlay.getBoundsInParent()) && heroGamePlay.getLayoutY()>greenOrcGamePlay.getLayoutY()-60) {
            check_collision_orcs(greenOrcGamePlay);
            p_go -= 1;
            if(p_go==0){
                orcDieSound.play();
                orc_falling = true;
                fall_orcs(greenOrcGamePlay);
                myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + 1));
                coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
            }
        }
        if(heroGamePlay.getBoundsInParent().intersects(redOrcGamePlay.getBoundsInParent()) && heroGamePlay.getLayoutY()>redOrcGamePlay.getLayoutY()-70) {
            check_collision_orcs(redOrcGamePlay);
            p_ro -= 1;
            if(p_ro==0){
                orcDieSound.play();
                orc_falling = true;
                fall_orcs(redOrcGamePlay);
                myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + 1));
                coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
            }
        }
        if(heroGamePlay.getBoundsInParent().intersects(bossOrcGamePlay.getBoundsInParent())){
            check_collision_orcs(bossOrcGamePlay);
            p_bo -= 1;
            if(p_bo==0){
                orcDieSound.play();
                orc_falling = true;
                fall_orcs(bossOrcGamePlay);
                myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + 10));
                coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
                winGameOver.setVisible(true);
                game_done = true;
                swordGamePlay.setVisible(false);
                throwingKnifeGamePlay.setVisible(false);
            }
        }
//        collision with coin chest
        if(coin_touched==false && heroGamePlay.getBoundsInParent().intersects(coinChest.getBoundsInParent())){
            chestSound.play();
            coin_touched = true;
            myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + (Math.random()<0.7?5:10)));
            coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));

            coinRotate.setNode(coinChest);
            coinRotate.setDuration(Duration.millis(500));
            coinRotate.setFromAngle(0);
            coinRotate.setToAngle(360);
            coinRotate.play();

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            coin_touched = false;
                            coinRotate.stop();
                        }
                    },
                    2500
            );
        }
//        collision with weapon chest
        if(weapon_touched==false && heroGamePlay.getBoundsInParent().intersects(weaponChest.getBoundsInParent())){
            chestSound.play();
            weapon_touched = true;
            myGame.getCurrPlayer().getHero().getMyHelmet().setWeaponCnt();
            myGame.getCurrPlayer().getHero().getMyHelmet().setSelectedIdx();

            if(myGame.getCurrPlayer().getHero().getMyHelmet().getSelectedIdx()==0){
                knivesLogo.setVisible(true);
                swordGamePlay.setVisible(false);
                swordGamePlay.setVisible(false);
            }
            else{
                throwingKnifeGamePlay.setVisible(false);
                swordLogo.setVisible(true);
                swordGamePlay.setVisible(true);
            }

            coinRotate.setNode(weaponChest);
            coinRotate.setDuration(Duration.millis(500));
            coinRotate.setFromAngle(0);
            coinRotate.setToAngle(360);
            coinRotate.play();

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            weapon_touched = false;
                            coinRotate.stop();
                        }
                    },
                    2500
            );
        }
//        collision with tnt
        if(heroGamePlay.getBoundsInParent().intersects(tnt.getBoundsInParent())){
            if(!tnt_active) {
                myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + 1));
                coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
                tnt_active = true;
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                tntSound.play();
                                blastGame.setLayoutX(tnt.getLayoutX());
                                blastGame.setLayoutY(tnt.getLayoutY());
                                tnt.setVisible(false);
                                blastGame.setVisible(true);
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                blastGame.setVisible(false);
                                                tnt_active = false;
                                            }
                                        },
                                        250
                                );
                                if (heroGamePlay.getLayoutX() - tnt.getLayoutX() < 60) {
                                    tnt_active = false;
                                    new java.util.Timer().schedule(
                                            new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                    game_over = true;
                                                }
                                            },
                                            250
                                    );
                                }
                            }
                        },
                        1200
                );
            }
        }
//        knife collision with orc
        if(knives_active==true && throwingKnifeGamePlay.getBoundsInParent().intersects(greenOrcGamePlay.getBoundsInParent())){
            orcDieSound.play();
            fall_orcs_knives(greenOrcGamePlay);
            myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + 1));
            coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
        }
        if(knives_active==true && throwingKnifeGamePlay.getBoundsInParent().intersects(redOrcGamePlay.getBoundsInParent())){
            orcDieSound.play();
            fall_orcs_knives(redOrcGamePlay);
            myGame.getCurrPlayer().setCoins((myGame.getCurrPlayer().getCoins() + 1));
            coinsCollectedGamePlay.setText(Integer.toString(myGame.getCurrPlayer().getCoins()));
        }

        return -1;
    }

    //    ---------------------------------------- Game Play ------------------------------------------
    public void move(){
        if(game_done==false) {
            moveHeroSound.play();
            int changeXBy = 40;
            moveX = true;
            myGame.getCurrPlayer().changePoints(myGame.getCurrPlayer().getPoints() < 130 ? 0.5 : 0);
            pointsScoredGamePlay.setText(Integer.toString((int) myGame.getCurrPlayer().getPoints()));
            if (knives_active == false && myGame.getCurrPlayer().getHero().getMyHelmet().getSelectedIdx() == 0) {
                knives_active = true;
                throw_knives();
            }
            for (int i = 0; i < 4; i++) {
                Inf tempInf = myGame.getCurrPlatforms().get(i).getInf();
                if (tempInf.getX() + tempInf.getLen() < 0) {
//              System.out.println(myPlatformImages.get(i).getLayoutX()+" oops");
//                myGame.getCurrPlatforms().get(i).getInf().setX(myGame.getCurrPlatforms().get(i-1<0?3:i-1).getInf().getX() + (i-1<0?280-changeXBy:280));
                    if (myGame.getDoneTill() >= 36) {
                        if (myGame.getDoneTill() == 36) {
                            lastStep();
                            myGame.changeDoneTill();
                        }
                        degradePlatform(i);
                    } else {
                        setupPlatform(myGame.getDoneTill());
                        myGame.changeDoneTill();
                    }
                } else {
//              System.out.println(myPlatformImages.get(i).getLayoutX()+" ok");
                    myGame.getCurrPlatforms().get(i).getInf().changeX(-changeXBy);
                    myPlatformImages.get(i).setLayoutX(myGame.getCurrPlatforms().get(i).getInf().getX());
                }
            }
            for (int i = 0; i < myObjWithType.size(); i++) {
                myObjWithType.get(i).getKey().setLayoutX(myObjWithType.get(i).getKey().getLayoutX() - 40);
            }
            if (myGame.getDoneTill() >= 37 && myGame.getDoneTill() <= 58) {
                platform5.setLayoutX(platform5.getLayoutX() - 40);
                bossOrcGamePlay.setLayoutX(bossOrcGamePlay.getLayoutX() - 40);
                princessGamePlay.setLayoutX(princessGamePlay.getLayoutX() - 40);
                finalCoinChest.setLayoutX(finalCoinChest.getLayoutX() - 40);
                myGame.changeDoneTill();
            }
        }
    }

    public void clicked_pause_btn_game_play(ActionEvent e) throws IOException {
        btnClick.play();
        timeline.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_pause.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 380);
        Stage stage = (Stage) pauseBtnGamePlay.getScene().getWindow();
        stage.setTitle("Will Hero - Game Paused");
        stage.setScene(scene);
    }

    //    ---------------------------------------- Pause Game ------------------------------------------
    public void clicked_close_btn_pause_game(ActionEvent e) throws IOException {
        btnClick.play();
        timeline.stop();
        Alert myAlert = new Alert(Alert.AlertType.CONFIRMATION);
        myAlert.setTitle("Confirmation Prompt");
        myAlert.setHeaderText("Are you sure, you want to exit this current game?");
        myAlert.setContentText("Your data for this game will be lost!");
        ButtonType t = myAlert.showAndWait().get();
        btnClick.play();
        if(t == ButtonType.OK){
            if(myGame.getMyMusic()!=null) {
                myGame.getMyMusic().stop();
                myGame.setMyMusic(null);
            }
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home_screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 680, 380);
            Stage stage = (Stage) closeBtnPauseGame.getScene().getWindow();
            stage.setTitle("Will Hero - Home");
            stage.setScene(scene);
        }
    }

    public void clicked_play_btn_pause_game(ActionEvent e) throws IOException {
        btnClick.play();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_play.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 380);
        Stage stage = (Stage) playBtnPauseGame.getScene().getWindow();
        stage.setTitle("Will Hero - Playing Game");
        stage.setScene(scene);
    }

    @FXML
    Button saveBtnPauseGame;
    public void clicked_save_btn_pause_game(ActionEvent e) throws IOException {
        btnClick.play();
        timeline.stop();
        if(myGame.getMyMusic()!=null) {
            myGame.getMyMusic().stop();
            myGame.setMyMusic(new PlayMusic("src/main/resources/assets/Audio/gamePlay.mp3"));
            myGame.getMyMusic().play();
        }
        SaveGame.serialize();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 380);
        Stage stage = (Stage) saveBtnPauseGame.getScene().getWindow();
        stage.setTitle("Will Hero - Home");
        stage.setScene(scene);
    }

    //    ---------------------------------------- Game Over ------------------------------------------
    public void revive() throws InsufficientCoinsException, IOException {
        if(myGame.getCurrPlayer().getCoins()<100){
            throw new InsufficientCoinsException("Oops! You don't have sufficient coins to revive the Will Hero.");
        }
        else{
            myGame.getCurrPlayer().setCoins(myGame.getCurrPlayer().getCoins()-100);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_play.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 680, 380);
            Stage stage = (Stage) reviveBtnGameOver.getScene().getWindow();
            stage.setTitle("Will Hero - Playing Game");
            stage.setScene(scene);
        }
    }

    public void clicked_revive_btn_game_over(ActionEvent e) throws IOException {
        btnClick.play();
        try {
            revive();
        }
        catch (InsufficientCoinsException ee){
            Alert myAlert = new Alert(Alert.AlertType.INFORMATION);
            myAlert.setTitle("Game Over");
            myAlert.setHeaderText(ee.getMessage());
            myAlert.setContentText("Good luck... try next time!");
            try {
                ButtonType b = myAlert.showAndWait().get();
                btnClick.play();
            }catch (NoSuchElementException eee){}
            finally {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_result.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 680, 380);
                Stage stage = (Stage) reviveBtnGameOver.getScene().getWindow();
                stage.setTitle("Will Hero - Game Result");
                stage.setScene(scene);
            }
        }
    }

    public void clicked_skip_btn_game_over(ActionEvent e) throws IOException {
        btnClick.play();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_result.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 380);
        Stage stage = (Stage) reviveBtnGameOver.getScene().getWindow();
        stage.setTitle("Will Hero - Game Result");
        stage.setScene(scene);
    }

}