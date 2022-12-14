package lab.entity;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lab.enums.BlockState;
import lab.enviroment.Enviroment;
import lab.enviroment.Game;
import lab.enviroment.GridBlock;
import lab.interfaces.Collisionable;

import java.util.Random;

import static lab.Constants.CHERRY_SPRITE;

public class BonusItem extends WorldEntity implements Collisionable {
    private boolean isEaten = true;
    private final Image texture = CHERRY_SPRITE;
    Random random = new Random();


    public BonusItem(Game game) {
        super(game, new Point2D( -20, -20), new Point2D(20, 20));
    }
    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.save();

        if (!isEaten) {
            gc.drawImage(texture, position.getX(), position.getY(), size.getX(), size.getY());
        }

        gc.restore();
    }

    @Override
    public void simulate(double deltaT) {
        if (game.getPacman().getProgress().getAmount() < 20) {
            return;
        }

        if (!isEaten) {
            return;
        }

        spawn();
    }

    public void spawn() {
        int x = random.nextInt(100);
        if (x < 2) {
            Enviroment env = game.getGrid().getBlocks().get(random.nextInt(game.getGrid().getBlocks().size()));
            if (env instanceof GridBlock block && block.getState().equals(BlockState.FILLED)) {
                this.position = block.getPosition();
                isEaten = false;
            } else {
                spawn();
            }
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getX(), size.getY());
    }

    @Override
    public void hit() {
        isEaten = true;
        game.getPacman().getScore().update(100);
    }

    public boolean isEaten() {
        return this.isEaten;
    }
}
