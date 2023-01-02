package com.example.snake3d;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Random;

public class Snake3D extends Application
{

    // ALWAYS X++ UNTIL KEYBIND IS PRESSED
    private Point3D direction = new Point3D(1, 0, 0);
    private Point3D nextPosition = new Point3D(0, 0, 0);

    private Group root = new Group();
    private Group snake = new Group();

    private Cube food = new Cube(Color.ORANGE);

    private Random random = new Random();

    private double t = 0;
    private AnimationTimer timer;

    private Scene createScene()
    {

        Cube cube = new Cube(Color.BLUE);
        root.getChildren().addAll(snake , food);

        snake.getChildren().add(cube);

        moveFood();

        Scene scene = new Scene(root , 1280 , 720 , true);

        //MOVE CAMERA POSITION TO SEE CUBE
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Translate(0, -20 , -20) , new Rotate(-45, Rotate.X_AXIS));

        scene.setCamera(camera);

        //MOVE SNAKE EVERY TIME
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                t += 0.016; // TIME CAN BE CHANGED DEPENDENT ON FPS LIMIT

                if(t> 0.1)
                {
                    onUpdate();
                    t=0;
                }
            }
        };

        timer.start();

        return scene;
    }

    // RANDOMLY GENERATE FOOD SPAWN LOCATION
    public void moveFood()
    {
        food.setTranslateX(random.nextInt(10) - 5);
        food.setTranslateY(random.nextInt(10) - 5);
        food.setTranslateZ(random.nextInt(10) - 5);
    }

    // WHEN FOOD IS EATEN, SNAKE SIZE INCREASES
    private void grow()
    {
        moveFood();
        Cube cube = new Cube(Color.BLUE);
        cube.set(nextPosition.add(direction));

        snake.getChildren().add(cube);

        Random colRand = new Random();
        Integer colInt = colRand.nextInt(0xFFFFFF);
        String hexColorCode = String.format("#%06X", (0xFFFFFF & colInt));  // combining HexCode(0xFFFFFF) and Integer then to String
        Color colForCube = Color.web(hexColorCode,1.0); // some random HexCode-String for (Web)Color-code; second value is alpha-lvl (optional)
    }

    //MOVES SNAKE POSITION
    private void onUpdate()
    {
        nextPosition = nextPosition.add(direction);
        Cube c = (Cube) snake.getChildren().remove(0);
        c.set(nextPosition);
        snake.getChildren().add(c);

        boolean collision = snake.getChildren()
                .stream()
                .map(n -> (Cube) n)
                .anyMatch(cube -> cube.isColliding(food));

        if (collision)
        {
            grow();
        }
    }

    //MOVEMENT CONTROLS
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = createScene();

        scene.setOnKeyPressed(e ->
        {
            switch (e.getCode())
            {
                case W:
                    direction = new Point3D(0 , 0 , 1);
                    break;
                case A:
                    direction = new Point3D(-1 , 0 , 0);
                    break;
                case S:
                    direction = new Point3D(0 , 0 , -1);
                    break;
                case D:
                    direction = new Point3D(1 , 0 , 0);
                    break;
                case UP:
                    direction = new Point3D(0 , -1 , 0);
                    break;
                case DOWN:
                    direction = new Point3D(0 , 1 , 0);
                    break;
            }
        });

        stage.setTitle("Snake 3D");
        stage.setScene(scene);
        stage.show();
    }

    //CUBE PROPERTIES
    private static class Cube extends Box
    {
        public Cube(Color color)
        {
            super(1,1,1);
            setMaterial(new PhongMaterial(color));
        }

        public void set(Point3D p)
        {
            setTranslateX(p.getX());
            setTranslateY(p.getY());
            setTranslateZ(p.getZ());
        }

        //RETURNS TRUE IF SNAKE COLLIDES WITH OBJECT
        public boolean isColliding(Cube c)
        {
            return getTranslateX() == c.getTranslateX()
                    && getTranslateY() == c.getTranslateY()
                    && getTranslateZ() == c.getTranslateZ();
        }
    }

    public static void main(String[] args)
    {
        launch();
    }
}