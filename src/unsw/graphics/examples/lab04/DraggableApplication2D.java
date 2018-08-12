package unsw.graphics.examples.lab04;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.Application2D;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point2D;

public class DraggableApplication2D extends Application2D {
	private Point2D cameraPosition;
	private float zoom;
	private float aspectRatio;

	private boolean mouseEnabled;
	private boolean keyboardEnabled;

	public DraggableApplication2D(String title, int width, int height) {
		super(title, width, height);
		cameraPosition = new Point2D(0.0f, 0.0f);
		zoom = 1.0f;

		mouseEnabled = true;
		keyboardEnabled = true;
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);
		getWindow().addMouseListener(new MouseAdapter() {
			private int lastX = 0;
			private int lastY = 0;

			@Override
			public void mouseMoved(MouseEvent ev) {
				lastX = ev.getX();
				lastY = ev.getY();
			}

			@Override
			public void mouseDragged(MouseEvent ev) {
				if (mouseEnabled) {
					Point2D moveDistance = new Point2D((lastX - ev.getX()) * zoom / getWindow().getSurfaceWidth() * 2.0f, (ev.getY() - lastY) * zoom / getWindow().getSurfaceHeight() * 2.0f);
					cameraPosition = new Point2D(cameraPosition.getX() + moveDistance.getX(), cameraPosition.getY() + moveDistance.getY());
					lastX = ev.getX();
					lastY = ev.getY();
				}
			}
		});
		getWindow().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyboardEnabled) {
					switch (keyEvent.getKeyCode()) {
						case KeyEvent.VK_MINUS:
							zoom *= 1.25f;
							break;
						case KeyEvent.VK_EQUALS:
							zoom *= 0.8f;
							break;
						case KeyEvent.VK_0:
							zoom = 1.0f;
							break;
						case KeyEvent.VK_UP:
							cameraPosition = new Point2D(cameraPosition.getX(), cameraPosition.getY() + 0.05f * zoom);
							break;
						case KeyEvent.VK_DOWN:
							cameraPosition = new Point2D(cameraPosition.getX(), cameraPosition.getY() - 0.05f * zoom);
							break;
						case KeyEvent.VK_LEFT:
							cameraPosition = new Point2D(cameraPosition.getX() - 0.05f * zoom, cameraPosition.getY());
							break;
						case KeyEvent.VK_RIGHT:
							cameraPosition = new Point2D(cameraPosition.getX() + 0.05f * zoom, cameraPosition.getY());
							break;
					}
					if (keyEvent.getKeyCode() == KeyEvent.VK_MINUS) {
						zoom *= 1.25f;
					} else if (keyEvent.getKeyCode() == KeyEvent.VK_EQUALS) {
						zoom *= 0.8f;
					} else if (keyEvent.getKeyCode() == KeyEvent.VK_0) {
						zoom = 1.0f;
					}
				}
			}
		});
	}

	private Point2D fromScreenCoords(MouseEvent ev) {
		//TODO: This sometimes is a little off. Can it be improved?
		float x = (2f*ev.getX()/getWindow().getSurfaceWidth() - 1 + cameraPosition.getX()) * zoom;
		float y = (-2f*ev.getY()/getWindow().getSurfaceHeight() + 1 + cameraPosition.getY()) * zoom;
		return new Point2D(x, y);
	}

	public DraggableApplication2D enableKeyboard() {
		keyboardEnabled = true;
		return this;
	}

	public DraggableApplication2D disableKeyboard() {
		keyboardEnabled = false;
		return this;
	}

	public DraggableApplication2D enableMouse() {
		mouseEnabled = true;
		return this;
	}

	public DraggableApplication2D disableMouse() {
		mouseEnabled = false;
		return this;
	}

	@Override
	public void display(GL3 gl) {
		super.display(gl);

		if (aspectRatio >= 1.0f) {
			Shader.setViewMatrix(gl, CoordFrame2D.identity().scale(1.0f / zoom / aspectRatio, 1.0f / zoom).translate(-cameraPosition.getX(), -cameraPosition.getY()).getMatrix());
		} else {
			Shader.setViewMatrix(gl, CoordFrame2D.identity().scale(1.0f / zoom, 1.0f / zoom * aspectRatio).translate(-cameraPosition.getX(), -cameraPosition.getY()).getMatrix());
		}
	}

	@Override
	public void reshape(GL3 gl3, int width, int height) {
		aspectRatio = 1.0f * width / height;
	}
}
