package unsw.graphics.examples.lab03;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.LineStrip2D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.scene.MathUtil;

import java.awt.*;
import java.util.ArrayList;

public class Windmill extends DraggableApplication2D {
	private LineStrip2D pole;
	private LineStrip2D vane;

	public Windmill() {
		super("Windmill", 600, 600);
		this.setBackground(Color.WHITE);

		// Set up points.
		ArrayList<Point2D> polePoints = new ArrayList<>();
		polePoints.add(new Point2D(-0.05f, 0.0f));
		polePoints.add(new Point2D(-0.05f, 2.0f));
		polePoints.add(new Point2D(0.05f, 2.0f));
		polePoints.add(new Point2D(0.05f, 0.0f));
		polePoints.add(new Point2D(-0.05f, 0.0f));

		pole = new LineStrip2D(polePoints);

		ArrayList<Point2D> vanePoints = new ArrayList<>();
		vanePoints.add(new Point2D(0.0f, 0.0f));
		vanePoints.add(new Point2D(0.5f, 0.1f));
		vanePoints.add(new Point2D(1.0f, 0.0f));
		vanePoints.add(new Point2D(0.5f, -0.1f));
		vanePoints.add(new Point2D(0.0f, 0.0f));

		vane = new LineStrip2D(vanePoints);
	}

	public static void main(String[] args) {
		Windmill example = new Windmill();
		example.start();
	}

	public void drawPole(GL3 gl, CoordFrame2D frame) {
		pole.draw(gl, frame);
	}

	public void drawVane(GL3 gl, CoordFrame2D frame) {
		vane.draw(gl, frame);
	}

	public void drawWindmill(GL3 gl, CoordFrame2D frame) {
		// TODO
	}

	public void drawWindmills(GL3 gl, CoordFrame2D frame) {
		// TODO
	}

	public void display(GL3 gl) {
		super.display(gl);

		CoordFrame2D.identity().draw(gl);

		Shader.setPenColor(gl, Color.BLACK);

		// TODO: A draw call right here!

	}
}
