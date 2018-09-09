package unsw.graphics.examples.lab08;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.FreelookApplication3D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;

import java.util.ArrayList;

public class Cylinder extends FreelookApplication3D {


	public Cylinder(String name, int width, int height) {
		super(name, width, height);
	}

	public static void main(String[] args) {
		Cylinder example = new Cylinder("Cylinder", 600, 600);
		example.start();
	}


	@Override
	public void display(GL3 gl) {
		super.display(gl);

		CoordFrame3D.identity().draw(gl);

		gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_LINE);

		// Create all the vertices.
		final int NUM_SLICES = 32;
		ArrayList<Point3D> cylinderPoints = new ArrayList<>();

		// Add the centres.
		cylinderPoints.add(new Point3D(0.0f ,0.0f, -1.0f));
		cylinderPoints.add(new Point3D(0.0f ,0.0f, -3.0f));

		// Add all the other points.
		for (float z = -1.0f; z > -4.0f; z -= 2.0f) {
			for (int i = 0; i < NUM_SLICES; ++i) {
				cylinderPoints.add(new Point3D((float)Math.cos(Math.toRadians(i * 360.0f / NUM_SLICES)), (float)Math.sin(Math.toRadians(i * 360.0f / NUM_SLICES)), z));
			}
		}

		// Now to do each slice.
		for (int i = 0; i < NUM_SLICES; ++i) {
			// Do the top triangle.
			Triangle3D top = new Triangle3D(cylinderPoints.get(0), cylinderPoints.get(2 + i), cylinderPoints.get(2 + ((i + 1) % NUM_SLICES)));
			top.draw(gl);
			Triangle3D side1 = new Triangle3D(cylinderPoints.get(2 + ((i + 1) % NUM_SLICES)), cylinderPoints.get(2 + i), cylinderPoints.get(2 + NUM_SLICES + i));
			side1.draw(gl);
			Triangle3D side2 = new Triangle3D(cylinderPoints.get(2 + ((i + 1) % NUM_SLICES)), cylinderPoints.get(2 + NUM_SLICES + i), cylinderPoints.get(2 + NUM_SLICES + ((i + 1) % NUM_SLICES)));
			side2.draw(gl);
			Triangle3D bot = new Triangle3D(cylinderPoints.get(1), cylinderPoints.get(2 + NUM_SLICES + ((i + 1) % NUM_SLICES)), cylinderPoints.get(2 + NUM_SLICES + i));
			bot.draw(gl);
		}
	}
}
