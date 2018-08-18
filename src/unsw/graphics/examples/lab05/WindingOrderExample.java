package unsw.graphics.examples.lab05;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;
import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.examples.HelloTriangle3D;
import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.scene.MathUtil;

import java.awt.*;

public class WindingOrderExample extends Application3D {
	private float rotation;
	private boolean rotating;

	public WindingOrderExample() {
		super("Winding Order", 600, 600);
		rotation = 0.0f;
		rotating = false;
	}

	public static void main(String[] args) {
		WindingOrderExample example = new WindingOrderExample();
		example.start();
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
		getWindow().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
					rotating = !rotating;
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {

			}
		});
	}

	@Override
	public void display(GL3 gl) {
		super.display(gl);

		Shader.setViewMatrix(gl, CoordFrame3D.identity().scale(0.5f, 0.5f, 0.5f).translate(0.0f, 0.0f, -2.5f).getMatrix());

		gl.glEnable(GL3.GL_CULL_FACE);
		gl.glFrontFace(GL3.GL_FRONT);

		CoordFrame3D frame = CoordFrame3D.identity().rotateY(rotation);

		// the base
		TriangleFan3D base = new TriangleFan3D(
				1, 1, 0,
				-1, 1, 0,
				-1,-1, 0,
				1, -1, 0);
		Shader.setPenColor(gl, Color.BLACK);
		base.draw(gl, frame);

		// the sides
		Triangle3D side1 = new Triangle3D(
				0, 0, 2,
				1, 1, 0,
				-1, 1, 0);
		Shader.setPenColor(gl, Color.BLUE);
		side1.draw(gl, frame);

		Triangle3D side2 = new Triangle3D(
				0, 0, 2,
				-1, -1, 0,
				-1, 1, 0);
		Shader.setPenColor(gl, Color.GREEN);
		side2.draw(gl, frame);

		Triangle3D side3 = new Triangle3D(
				0, 0, 2,
				-1, -1, 0,
				1, -1, 0);
		Shader.setPenColor(gl, Color.RED);
		side3.draw(gl, frame);

		Triangle3D side4 = new Triangle3D(
				0, 0, 2,
				1, -1, 0,
				1, 1, 0);
		Shader.setPenColor(gl, Color.ORANGE);
		side4.draw(gl, frame);

		if (rotating) rotation = MathUtil.normaliseAngle(rotation + 0.5f);
	}
}
