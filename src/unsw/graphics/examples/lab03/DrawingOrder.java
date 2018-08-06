package unsw.graphics.examples.lab03;

import com.jogamp.opengl.GL3;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Triangle2D;

import java.awt.*;

public class DrawingOrder extends DraggableApplication2D {
	public DrawingOrder() {
		super("Drawing Order", 600, 600);
		this.setBackground(Color.WHITE);
	}

	public static void main(String[] args) {
		DrawingOrder example = new DrawingOrder();
		example.start();
	}

	@Override
	public void display(GL3 gl) {
		super.display(gl);
		Triangle2D tri1 = new Triangle2D(0.5f, 0.0f, 0.0f, 0.5f, -0.5f, 0.0f);
		Triangle2D tri2 = new Triangle2D(0.3f, -0.2f, 0.0f, 0.2f, -0.3f, -0.2f);

		Shader.setPenColor(gl, Color.BLUE);
		tri1.draw(gl);

		Shader.setPenColor(gl, Color.RED);
		tri2.draw(gl);
	}
}
