package unsw.graphics.examples.lab04;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Triangle2D;
import unsw.graphics.scene.MathUtil;

import java.awt.*;

public class SpinningShape extends DraggableApplication2D {
	private float rotation1;
	private float rotation2;
	private float rotation3;

	private float radius1;
	private float radius2;
	private float radius3;

	private boolean spinning;

	public SpinningShape() {
		super("Spinning Shape", 600, 600);
		this.enableKeyboard().enableMouse();
		rotation1 = 0.0f;
		rotation2 = 120.0f;
		rotation3 = -120.0f;
		radius1 = 0.5f;
		radius2 = 0.7f;
		radius3 = 0.9f;
		spinning = false;
		setBackground(Color.BLACK);
	}

	public static void main(String[] args) {
		SpinningShape example = new SpinningShape();
		example.start();
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
		getWindow().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
					spinning = !spinning;
				}
			}
		});

	}

	@Override
	public void display(GL3 gl) {
		super.display(gl);

		Triangle2D tri = new Triangle2D((float)Math.cos(Math.toRadians(rotation1)) * radius1, (float)Math.sin(Math.toRadians(rotation1)) * radius1, (float)Math.cos(Math.toRadians(rotation2)) * radius2, (float)Math.sin(Math.toRadians(rotation2)) * radius2, (float)Math.cos(Math.toRadians(rotation3)) * radius3, (float)Math.sin(Math.toRadians(rotation3)) * radius3);

		Shader.setPenColor(gl, Color.BLUE);
		tri.draw(gl);

		if (spinning) {
			rotation1 = MathUtil.normaliseAngle(rotation1 + 1.2f);
			rotation2 = MathUtil.normaliseAngle(rotation2 + 1.1f);
			rotation3 = MathUtil.normaliseAngle(rotation3 + 1.0f);
		}

		CoordFrame2D.identity().draw(gl);
	}
}
