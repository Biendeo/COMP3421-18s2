package unsw.graphics.examples.lab09;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.FreelookApplication3D;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.Triangle3D;
import unsw.graphics.geometry.TriangleMesh;

import java.awt.*;
import java.util.ArrayList;

import static com.jogamp.newt.event.KeyEvent.*;

public class Cylinder extends FreelookApplication3D {
	private int NUM_SLICES;
	private int lastSliceCount;
	private TriangleMesh mesh;

	private String textureFileName1 = "res/textures/kittens.jpg";
	private String textureExt1 = "jpg";

	private Texture texture;

	private Shader shader;

	public Cylinder(String name, int width, int height) {
		super(name, width, height);
		NUM_SLICES = 32;
		lastSliceCount = 0;
		mesh = null;
		texture = null;
	}

	public static void main(String[] args) {
		Cylinder example = new Cylinder("Cylinder", 600, 600);
		example.start();
	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);

		shader = new Shader(gl, "shaders/vertex_tex_3d.glsl","shaders/fragment_tex_3d.glsl");
		shader.use(gl);

		texture = new Texture(gl, textureFileName1, textureExt1, true);
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
				}
				System.out.println(NUM_SLICES);
			}
		});
	}

	private void updateSliceCount(GL3 gl, int numSlices) {
		lastSliceCount = NUM_SLICES;
		if (mesh != null) {
			mesh.destroy(gl);
		}

		ArrayList<Point3D> cylinderVertices = new ArrayList<>();
		ArrayList<Integer> cylinderIndicies = new ArrayList<>();
		ArrayList<Point2D> cylinderTexture = new ArrayList<>();

		// Add the centres.
		cylinderVertices.add(new Point3D(0.0f, 0.0f, 0.0f));
		cylinderTexture.add(new Point2D(0.0f, 0.0f));
		cylinderVertices.add(new Point3D(0.0f, 0.0f, -1.0f));
		cylinderTexture.add(new Point2D(0.0f, 1.0f));

		// Add all the other points.
		for (float z = 0.0f; z >= -1.0f; z -= 1.0f) {
			for (int i = 0; i < NUM_SLICES; ++i) {
				cylinderVertices.add(new Point3D((float) Math.cos(Math.toRadians(i * 360.0f / NUM_SLICES)), (float) Math.sin(Math.toRadians(i * 360.0f / NUM_SLICES)), z));
				cylinderTexture.add(new Point2D((float)i / NUM_SLICES, -z));
			}
		}

		// Now to do each slice.
		for (int i = 0; i < NUM_SLICES; ++i) {
			// Do the top triangle.
			cylinderIndicies.add(0);
			cylinderIndicies.add(2 + i);
			cylinderIndicies.add(2 + ((i + 1) % NUM_SLICES));

			cylinderIndicies.add(2 + ((i + 1) % NUM_SLICES));
			cylinderIndicies.add(2 + i);
			cylinderIndicies.add(2 + NUM_SLICES + i);

			cylinderIndicies.add(2 + ((i + 1) % NUM_SLICES));
			cylinderIndicies.add(2 + NUM_SLICES + i);
			cylinderIndicies.add(2 + NUM_SLICES + ((i + 1) % NUM_SLICES));

			cylinderIndicies.add(1);
			cylinderIndicies.add(2 + NUM_SLICES + ((i + 1) % NUM_SLICES));
			cylinderIndicies.add(2 + NUM_SLICES + i);
		}

		mesh = new TriangleMesh(cylinderVertices, cylinderIndicies, true, cylinderTexture);
		mesh.init(gl);
	}

	@Override
	public void display(GL3 gl) {
		super.display(gl);

		//gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_LINE);
		gl.glActiveTexture(GL3.GL_TEXTURE0);
		gl.glBindTexture(GL3.GL_TEXTURE_2D, texture.getId());
		Shader.setPenColor(gl, Color.LIGHT_GRAY);

		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);

		if (lastSliceCount != NUM_SLICES) {
			updateSliceCount(gl, NUM_SLICES);
		}

		//CoordFrame3D.identity().draw(gl);

		mesh.draw(gl, CoordFrame3D.identity());
	}
}
