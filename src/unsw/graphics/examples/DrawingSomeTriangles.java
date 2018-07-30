package unsw.graphics.examples;

import com.jogamp.opengl.GL3;
import unsw.graphics.Application2D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Triangle2D;

import java.awt.*;
import java.util.ArrayList;

public class DrawingSomeTriangles extends Application2D {
	public DrawingSomeTriangles() {
		super("Drawing some traingles", 600, 600);
		this.setBackground(new Color(1.0f, 1.0f, 0.0f));
	}

	public static void main(String[] args) {
		DrawingSomeTriangles example = new DrawingSomeTriangles();
		example.start();
	}


	@Override
	public void display(GL3 gl) {
		super.display(gl);

		// Both of these triangles have intentionally been drawn anti-clockwise. This means that the front face comes
		// out at us. While not vital in our 2D drawing systems, it's good to encourage vertex ordering right now.
		Triangle2D topTri = new Triangle2D(1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f);
		Triangle2D bottomTri = new Triangle2D(-1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f);

		topTri.draw(gl);
		bottomTri.draw(gl);

		// Another method would be to use the ArrayList method of initialising a triangle. A side effect of our
		// implementation allows one to indicate six vertices for the triangle, which would draw two triangles since it
		// is using GL_TRIANGLES, so you can implement this with one Triangle2D class.
	}
}
