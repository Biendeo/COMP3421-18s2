package unsw.graphics.examples.lab05;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;
import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.scene.MathUtil;

import java.awt.*;

public class PerspectiveExample extends Application3D {
	private float rotation;
	private boolean rotating;

	public PerspectiveExample() {
		super("Perspective Example", 600, 600);
		rotation = 0.0f;
		rotating = false;
	}

	public static void main(String[] args) {
		PerspectiveExample example = new PerspectiveExample();
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

		Matrix4 proj = Matrix4.perspective(60, 1, 1, 10);
		Shader.setProjMatrix(gl, proj);

		gl.glDisable(GL3.GL_CULL_FACE);
		CoordFrame3D viewMatrix = CoordFrame3D.identity().rotateY(rotation).translate((float)(-3.0 * Math.sin(Math.toRadians(-rotation))), 0.0f, (float)(-3.0 * Math.cos(Math.toRadians(-rotation)) + 3.0));
		Shader.setViewMatrix(gl, viewMatrix.getMatrix());

		TriangleFan3D quad1 = new TriangleFan3D(
				1, 1, -2,
				0, 1, -2,
				0, 0, -2,
				1, 0, -2);
		Shader.setPenColor(gl, Color.BLUE);
		quad1.draw(gl);

		TriangleFan3D quad2 = new TriangleFan3D(
				0, 1, -4,
				-1, 1, -4,
				-1, 0, -4,
				0, 0, -4);
		Shader.setPenColor(gl, Color.RED);
		quad2.draw(gl);

		if (rotating) rotation = MathUtil.normaliseAngle(rotation + 0.5f);
	}
}
