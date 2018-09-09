package unsw.graphics.examples.lab08;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.Application2D;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Point2DBuffer;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static com.jogamp.newt.event.KeyEvent.VK_R;
import static com.jogamp.newt.event.KeyEvent.VK_SPACE;

public class BezierExample extends Application2D {
	private ArrayList<Point2D> currentPoints;
	private ArrayList<Color> colors;

	private int t;

	public BezierExample(String title, int width, int height) {
		super(title, width, height);
		currentPoints = new ArrayList<>();
		colors = new ArrayList<>();

		randomiseColors();

		t = 0;
	}

	public static void main(String[] args) {
		BezierExample example = new BezierExample("Bezier example", 600, 600);
		example.start();
	}



	@Override
	public void init(GL3 gl) {
		super.init(gl);
		getWindow().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				currentPoints.add(fromScreenCoords(ev));
			}
		});

		getWindow().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				switch (keyEvent.getKeyCode()) {
					case VK_SPACE:
						currentPoints.clear();
						break;
					case VK_R:
						randomiseColors();
						break;
				}
			}
		});


	}


	private Point2D fromScreenCoords(MouseEvent ev) {
		//TODO: This sometimes is a little off. Can it be improved?
		float x = 2f*ev.getX()/getWindow().getSurfaceWidth() - 1;
		float y = -2f*ev.getY()/getWindow().getSurfaceHeight() + 1;
		return new Point2D(x, y);
	}

	private void randomiseColors() {
		colors.clear();
		Random r = new Random();
		for (int i = 0; i < 100; ++i) {
			colors.add(Color.getHSBColor(r.nextFloat(), 1.0f, 1.0f));
		}
	}

	private static Point2D getPoint(Line2D line, float t) {
		return new Point2D(line.getStart().getX() * (1.0f - t) + line.getEnd().getX() * t, line.getStart().getY() * (1.0f - t) + line.getEnd().getY() * t);
	}

	@Override
	public void display(GL3 gl) {
		super.display(gl);

		int colorIndex = 0;

		ArrayList<ArrayList<ArrayList<Line2D>>> finalLines = new ArrayList<>();

		for (int newT = 0; newT <= 100; ++newT) {
			finalLines.add(new ArrayList<>());
			for (int i = 0; i < currentPoints.size() - 1; ++i) {
				finalLines.get(newT).add(new ArrayList<>());
				for (int j = 0; j <= i; ++j) {
					if (j == 0) {
						finalLines.get(newT).get(j).add(new Line2D(currentPoints.get(i), currentPoints.get(i + 1)));
					} else {
						finalLines.get(newT).get(j).add(new Line2D(getPoint(finalLines.get(newT).get(j - 1).get(i - j), newT / 100.0f), getPoint(finalLines.get(newT).get(j - 1).get(i - j + 1), newT / 100.0f)));
					}
				}
			}
		}

		try {
			for (int j = 0; j < finalLines.get(t).size(); ++j) {
				for (int i = 0; i < finalLines.get(t).get(j).size(); ++i) {
					Line2D currentLine = finalLines.get(t).get(j).get(i);

					Shader.setPenColor(gl, colors.get(colorIndex % colors.size()));
					currentLine.draw(gl);
					++colorIndex;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {
			if (currentPoints.size() >= 2) {
				Point2DBuffer buffer = new Point2DBuffer(101);
				for (int newT = 0; newT <= 100; ++newT) {
					buffer.put(newT, getPoint(finalLines.get(newT).get(currentPoints.size() - 2).get(0), newT / 100.0f));
				}
				int[] names = new int[1];
				gl.glGenBuffers(1, names, 0);

				gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, names[0]);

				gl.glBufferData(GL3.GL_ARRAY_BUFFER, 2 * 101 * Float.BYTES, buffer.getBuffer(),
						GL3.GL_STATIC_DRAW);

				gl.glVertexAttribPointer(Shader.POSITION, 2, GL3.GL_FLOAT, false, 0, 0);
				Shader.setModelMatrix(gl, CoordFrame2D.identity().getMatrix());
				gl.glDrawArrays(GL3.GL_LINE_STRIP, 0, 101);

				gl.glDeleteBuffers(1, names, 0);
			}
		} catch (Throwable a) {
			a.printStackTrace();
			System.out.println(t);
		}

		t += 1;
		if (t > 100) {
			t = 0;
		}
	}
}
