package unsw.graphics;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.scene.MathUtil;

import static com.jogamp.newt.event.KeyEvent.*;

public class FreelookApplication3D extends Application3D {
	private Point3D cameraPosition;
	private float cameraTilt;
	private float cameraYaw;
	private float vFov;
	private float zNear;
	private float zFar;

	private float aspectRatio;

	private boolean mouseEnabled;
	private boolean keyboardEnabled;

	public FreelookApplication3D(String title, int width, int height) {
		super(title, width, height);
		cameraPosition = new Point3D(0.0f, 0.0f, 0.0f);
		cameraTilt = 0.0f;
		cameraYaw = 0.0f;

		vFov = 60.0f;
		zNear = 0.1f;
		zFar = 10.0f;
		aspectRatio = 1.0f;

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
					/*
					Point2D moveDistance = new Point2D((lastX - ev.getX()) * zoom / getWindow().getSurfaceWidth() * 2.0f, (ev.getY() - lastY) * zoom / getWindow().getSurfaceHeight() * 2.0f);
					cameraPosition = new Point2D(cameraPosition.getX() + moveDistance.getX(), cameraPosition.getY() + moveDistance.getY());
					lastX = ev.getX();
					lastY = ev.getY();
					*/
				}
			}
		});
		getWindow().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyboardEnabled) {
					switch (keyEvent.getKeyCode()) {
						case VK_W:
						case VK_UP:
							cameraPosition = cameraPosition.translate((float)-Math.sin(Math.toRadians(cameraYaw)) * 0.05f, 0.0f, (float)-Math.cos(Math.toRadians(cameraYaw)) * 0.05f);
							break;
						case VK_S:
						case VK_DOWN:
							cameraPosition = cameraPosition.translate((float)Math.sin(Math.toRadians(cameraYaw)) * 0.05f, 0.0f, (float)Math.cos(Math.toRadians(cameraYaw)) * 0.05f);
							break;
						case VK_D:
							cameraPosition = cameraPosition.translate((float)Math.cos(Math.toRadians(cameraYaw)) * 0.05f, 0.0f, (float)-Math.sin(Math.toRadians(cameraYaw)) * 0.05f);
							break;
						case VK_A:
							cameraPosition = cameraPosition.translate((float)-Math.cos(Math.toRadians(cameraYaw)) * 0.05f, 0.0f, (float)Math.sin(Math.toRadians(cameraYaw)) * 0.05f);
							break;
						case VK_R:
							cameraPosition = cameraPosition.translate(0.0f, 0.05f, 0.0f);
							break;
						case VK_F:
							cameraPosition = cameraPosition.translate(0.0f, -0.05f, 0.0f);
							break;
						case VK_LEFT:
							cameraYaw = MathUtil.normaliseAngle(cameraYaw + 3.0f);
							break;
						case VK_RIGHT:
							cameraYaw = MathUtil.normaliseAngle(cameraYaw - 3.0f);
							break;
						case VK_T:
							cameraTilt = MathUtil.clamp(cameraTilt + 3.0f, -90.0f, 90.0f);
							break;
						case VK_G:
							cameraTilt = MathUtil.clamp(cameraTilt - 3.0f, -90.0f, 90.0f);
							break;
					}
				}
			}
		});
	}


	@Override
	public void display(GL3 gl) {
		super.display(gl);

		Shader.setProjMatrix(gl, Matrix4.perspective(vFov, aspectRatio, zNear, zFar));
		Shader.setViewMatrix(gl, CoordFrame3D.identity().rotateX(-cameraTilt).rotateY(-cameraYaw).translate(-cameraPosition.getX(), -cameraPosition.getY(), -cameraPosition.getZ()).getMatrix());
	}
}
