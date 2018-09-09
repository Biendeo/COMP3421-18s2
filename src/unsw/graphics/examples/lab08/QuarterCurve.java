package unsw.graphics.examples.lab08;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.FreelookApplication3D;
import unsw.graphics.geometry.Line3D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;

import java.util.ArrayList;

import static com.jogamp.newt.event.KeyEvent.*;

public class QuarterCurve extends FreelookApplication3D {
	private int NUM_SLICES;
	private int NUM_STACKS;

	public QuarterCurve(String name, int width, int height) {
		super(name, width, height);
		NUM_SLICES = 32;
		NUM_STACKS = 32;
	}

	public static void main(String[] args) {
		QuarterCurve example = new QuarterCurve("QuarterCurve", 600, 600);
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
						--NUM_SLICES;
						if (NUM_SLICES < 1) {
							NUM_SLICES = 1;
						}
						break;
					case VK_P:
						++NUM_SLICES;
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

		// The original points.
		ArrayList<Point2D> originalPoints = new ArrayList<>();
		for (int u = 0; u <= NUM_STACKS; ++u) {
			originalPoints.add(new Point2D((float)Math.cos(Math.toRadians(u * 90.0f / NUM_STACKS)), (float)Math.sin(Math.toRadians(u * 90.0f / NUM_STACKS))));
		}


		// Now to create the round part.
		try {
			for (int v = 0; v < NUM_SLICES; ++v) {
				for (int u = 0; u < NUM_STACKS; ++u) {
					// We need four points.
					Point3D p1 = new Point3D(originalPoints.get(u).getX() * (float)Math.cos(Math.toRadians(v * 360.0f / NUM_SLICES)), originalPoints.get(u).getY(), originalPoints.get(u).getX() * (float)Math.sin(Math.toRadians(v * 360.0f / NUM_SLICES)));
					Point3D p2 = new Point3D(originalPoints.get(u).getX() * (float)Math.cos(Math.toRadians((v + 1) * 360.0f / NUM_SLICES)), originalPoints.get(u).getY(), originalPoints.get(u).getX() * (float)Math.sin(Math.toRadians((v + 1) * 360.0f / NUM_SLICES)));
					Point3D p3 = new Point3D(originalPoints.get(u + 1).getX() * (float)Math.cos(Math.toRadians(v * 360.0f / NUM_SLICES)), originalPoints.get(u + 1).getY(), originalPoints.get(u + 1).getX() * (float)Math.sin(Math.toRadians(v * 360.0f / NUM_SLICES)));
					Point3D p4 = new Point3D(originalPoints.get(u + 1).getX() * (float)Math.cos(Math.toRadians((v + 1) * 360.0f / NUM_SLICES)), originalPoints.get(u + 1).getY(), originalPoints.get(u + 1).getX() * (float)Math.sin(Math.toRadians((v + 1) * 360.0f / NUM_SLICES)));

					Triangle3D t1 = new Triangle3D(p2, p1, p3);
					t1.draw(gl);
					Triangle3D t2 = new Triangle3D(p2, p3, p4);
					t2.draw(gl);

				}
			}
		} catch (Throwable a) {
			a.printStackTrace();
		}

	}
}
