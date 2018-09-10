package unsw.graphics.examples.lab08;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.FreelookApplication3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;

import java.util.ArrayList;

import static com.jogamp.newt.event.KeyEvent.*;

public class Extrusion extends FreelookApplication3D {
	private int NUM_STACKS;
	private float depth;

	public Extrusion(String name, int width, int height) {
		super(name, width, height);
		NUM_STACKS = 32;
		depth = 1.0f;
	}

	public static void main(String[] args) {
		Extrusion example = new Extrusion("Extrusion", 600, 600);
		example.start();
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
		getWindow().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				switch (keyEvent.getKeyCode()) {
					case VK_O:
						depth -= 0.1f;
						break;
					case VK_P:
						depth += 0.1f;
						break;
					case VK_MINUS:
						--NUM_STACKS;
						if (NUM_STACKS < 1) {
							NUM_STACKS = 1;
						}
						break;
					case VK_EQUALS:
						++NUM_STACKS;
						break;
				}
			}
		});
	}


	@Override
	public void display(GL3 gl) {
		super.display(gl);

		CoordFrame3D.identity().draw(gl);

		gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_LINE);

		Vector4 p1 = new Vector4(0.0f, 0.0f, 0.0f, 1.0f);
		Vector4 p2 = new Vector4(0.5f, 0.0f, 0.0f, 1.0f);
		Vector4 p3 = new Vector4(0.0f, -0.5f, 0.0f, 1.0f);

		try {
			ArrayList<Matrix4> coordFrames = new ArrayList<>();
			for (int a = 0; a <= NUM_STACKS; ++a) {
				float x = (float)Math.cos(Math.toRadians(a * 360.0f * depth / NUM_STACKS));
				float y = (float)Math.sin(Math.toRadians(a * 360.0f * depth / NUM_STACKS));
				float z = a * depth / NUM_STACKS;
				float dx = (float)-Math.sin(Math.toRadians(a * 360.0f * depth / NUM_STACKS));
				float dy = (float)Math.cos(Math.toRadians(a * 360.0f * depth / NUM_STACKS));
				float dz = depth;

				float dMagnitude = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);

				Point3D k = new Point3D(dx / dMagnitude, dy / dMagnitude, dz / dMagnitude);
				Point3D i = new Point3D(-k.getY(), k.getX(), 0.0f);
				float iMagnitude = (float)Math.sqrt(k.getX() * k.getX() + k.getY() * k.getY());
				i = new Point3D(i.getX() / iMagnitude, i.getY() / iMagnitude, 0.0f);
				Point3D j = new Point3D(k.getY() * i.getZ() - i.getY() * k.getZ(), k.getZ() * i.getX() - i.getZ() * k.getX(), k.getX() * i.getY() - i.getX() * k.getY());

				Matrix4 m = new Matrix4(new float[]{i.getX(), i.getY(), i.getZ(), 0.0f, j.getX(), j.getY(), j.getZ(), 0.0f, k.getX(), k.getY(), k.getZ(), 0.0f, x, y, z, 1.0f});

				coordFrames.add(m);
			}

			for (int a = 0; a < NUM_STACKS; ++a) {
				Matrix4 frame1 = coordFrames.get(a);
				Matrix4 frame2 = coordFrames.get(a + 1);

				Vector4 p1n1 = frame1.multiply(p1);
				Vector4 p2n1 = frame1.multiply(p2);
				Vector4 p3n1 = frame1.multiply(p3);

				Vector4 p1n2 = frame2.multiply(p1);
				Vector4 p2n2 = frame2.multiply(p2);
				Vector4 p3n2 = frame2.multiply(p3);

				// Now to draw the triangles.
				Triangle3D top = new Triangle3D(p1n1.asPoint3D(), p2n1.asPoint3D(), p3n1.asPoint3D());
				Triangle3D side11 = new Triangle3D(p2n1.asPoint3D(), p1n1.asPoint3D(), p1n2.asPoint3D());
				Triangle3D side12 = new Triangle3D(p2n1.asPoint3D(), p1n2.asPoint3D(), p2n2.asPoint3D());
				Triangle3D side21 = new Triangle3D(p2n1.asPoint3D(), p1n1.asPoint3D(), p1n2.asPoint3D());
				Triangle3D side22 = new Triangle3D(p2n1.asPoint3D(), p1n2.asPoint3D(), p2n2.asPoint3D());
				Triangle3D side31 = new Triangle3D(p2n1.asPoint3D(), p1n1.asPoint3D(), p1n2.asPoint3D());
				Triangle3D side32 = new Triangle3D(p2n1.asPoint3D(), p1n2.asPoint3D(), p2n2.asPoint3D());
				Triangle3D bottom = new Triangle3D(p1n2.asPoint3D(), p3n2.asPoint3D(), p2n2.asPoint3D());

				top.draw(gl);
				side11.draw(gl);
				side12.draw(gl);
				side21.draw(gl);
				side22.draw(gl);
				side31.draw(gl);
				side32.draw(gl);
				bottom.draw(gl);

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
