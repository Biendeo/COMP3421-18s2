package unsw.graphics.examples.lab03;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.LineStrip2D;
import unsw.graphics.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;

public class House extends DraggableApplication2D {
	private LineStrip2D house;

	public House() {
		super("House Transformations", 600, 600);
		this.setBackground(Color.WHITE);

		// Set up house.
		ArrayList<Point2D> housePoints = new ArrayList<>();
		housePoints.add(new Point2D(0.0f, 0.0f));
		housePoints.add(new Point2D(0.0f, 1.0f));
		housePoints.add(new Point2D(0.5f, 2.0f));
		housePoints.add(new Point2D(1.0f, 1.0f));
		housePoints.add(new Point2D(0.0f, 1.0f));
		housePoints.add(new Point2D(1.0f, 1.0f));
		housePoints.add(new Point2D(1.0f, 0.0f));
		housePoints.add(new Point2D(0.6f, 0.0f));
		housePoints.add(new Point2D(0.6f, 0.5f));
		housePoints.add(new Point2D(0.8f, 0.5f));
		housePoints.add(new Point2D(0.8f, 0.0f));
		housePoints.add(new Point2D(0.0f, 0.0f));

		house = new LineStrip2D(housePoints);

		this.disableKeyboard().enableMouse();
	}

	public static void main(String[] args) {
		House example = new House();
		example.start();
	}

	public void display(GL3 gl) {
		super.display(gl);

		CoordFrame2D.identity().draw(gl);

		Shader.setPenColor(gl, Color.BLACK);

		// Translate to see the house.
		house.draw(gl);
	}
}
