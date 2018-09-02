package unsw.graphics.examples;

import java.awt.Color;
import java.io.IOException;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * This is a simple application for viewing models.
 * 
 * Different PLY models have vastly different scales, so you may need to scale
 * the model up or down to view it properly.
 * 
 * High resolution models are not included with UNSWgraph due to their large
 * file sizes. They can be downloaded here:
 * 
 * https://www.dropbox.com/s/tg2y5kvzbgb3pco/big.zip?dl=1
 * 
 * @author Robert Clifton-Everest
 *
 */
public class ModelViewer extends Application3D {

    private static final boolean USE_LIGHTING = true;

    private float rotateY;

    private TriangleMesh model;

    private TriangleMesh base;

    private Shader gouraudShader;
    private Shader phongShader;
    private Shader phongFixedShader;
    private Shader halfLambertShader;
    private Shader lightAttenuationShader;

    private int currentShader;
    private boolean rotating;

    public ModelViewer() throws IOException {
        super("Model viewer", 600, 600);
        model = new TriangleMesh("res/models/bunny.ply", true);
        base = new TriangleMesh("res/models/cube_normals.ply", true);
    }

    @Override
    public void init(GL3 gl) {
        super.init(gl);
        model.init(gl);
        base.init(gl);
        if (USE_LIGHTING) {
            gouraudShader = new Shader(gl, "shaders/vertex_gouraud.glsl","shaders/fragment_gouraud.glsl");
            phongShader = new Shader(gl, "shaders/vertex_phong.glsl","shaders/fragment_phong.glsl");
            phongFixedShader = new Shader(gl, "shaders/vertex_tutorial_1.glsl","shaders/fragment_tutorial_1.glsl");
            halfLambertShader = new Shader(gl, "shaders/vertex_tutorial_2.glsl","shaders/fragment_tutorial_2.glsl");
            lightAttenuationShader = new Shader(gl, "shaders/vertex_tutorial_3.glsl","shaders/fragment_tutorial_3.glsl");

            currentShader = 0;
            rotating = true;
            getWindow().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                        currentShader = (currentShader + 1) % 5;
                    } else if (keyEvent.getKeyCode() == KeyEvent.VK_R) {
                        rotating = !rotating;
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {}
            });
        }
    }

    @Override
    public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, 1, 1, 100));
    }

    public static void main(String[] args) throws IOException {
        ModelViewer example = new ModelViewer();
        example.start();

    }

    @Override
    public void display(GL3 gl) {
        if (USE_LIGHTING) {
            switch (currentShader) {
                case 0:
                default:
                    gouraudShader.use(gl);
                    getWindow().setTitle("Model viewer: Gouraud");
                    break;
                case 1:
                    phongShader.use(gl);
                    getWindow().setTitle("Model viewer: Phong");
                    break;
                case 2:
                    phongFixedShader.use(gl);
                    getWindow().setTitle("Model viewer: Phong Fixed");
                    break;
                case 3:
                    halfLambertShader.use(gl);
                    getWindow().setTitle("Model viewer: Half Lambert");
                    break;
                case 4:
                    lightAttenuationShader.use(gl);
                    getWindow().setTitle("Model viewer: Light Attenuation");
                    break;
            }
        }

        super.display(gl);

        // Compute the view transform
        CoordFrame3D view = CoordFrame3D.identity().translate(0, 0, -2)
                // Uncomment the line below to rotate the camera
                // .rotateY(rotateY)
                .translate(0, 0, 2);
        Shader.setViewMatrix(gl, view.getMatrix());

        // Set the lighting properties
        Shader.setPoint3D(gl, "lightPos", new Point3D(0, 0, 5));
        Shader.setColor(gl, "lightIntensity", Color.WHITE);
        Shader.setColor(gl, "ambientIntensity", new Color(0.2f, 0.2f, 0.2f));
        
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
        Shader.setColor(gl, "specularCoeff", new Color(0.8f, 0.8f, 0.8f));
        Shader.setFloat(gl, "phongExp", 16f);

        // This one is my doing.
        Shader.setFloat(gl, "k", 0.1f);

        // The coordinate frame for both objects
        CoordFrame3D frame = CoordFrame3D.identity().translate(0, -0.5f, -2);

        // The coordinate frame for the model we're viewing.
        CoordFrame3D modelFrame = frame
                // Uncomment the line below to rotate the model
                .rotateY(rotateY)

                // This translation and scale works well for the bunny and
                // dragon1
                .translate(0, -0.2f, 0).scale(5, 5, 5);
        // This scale works well for the apple
        // .scale(5, 5, 5);
        // This translation and scale works well for dragon2
        // .translate(0,0.33f,0).scale(0.008f, 0.008f, 0.008f);
        Shader.setPenColor(gl, new Color(0.5f, 0.5f, 0.5f));
        model.draw(gl, modelFrame);

        // A blue base for the model to sit on.
        CoordFrame3D baseFrame = 
                frame.translate(0, -0.5f, 0).scale(0.5f, 0.5f, 0.5f);
        Shader.setPenColor(gl, Color.BLUE);
        base.draw(gl, baseFrame);

        if (rotating) {
            rotateY += 1;
        }
    }
    
    @Override
    public void destroy(GL3 gl) {
        super.destroy(gl);
        model.destroy(gl);
        base.destroy(gl);
    }

}