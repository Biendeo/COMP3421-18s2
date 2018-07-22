package unsw.graphics.geometry;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import unsw.graphics.Point2DBuffer;
import unsw.graphics.Shader;

import java.util.ArrayList;
import java.util.List;

/**
 * A polygon in space.
 *
 * This class is mutable as new points can be added to the polygon.
 *
 * @author Thomas Moffet
 * Mostly inspired by LineStrip2D.
 */
public class Polygon2D {
    private List<Point2D> points;

    public Polygon2D() {
        points = new ArrayList<>();
    }

    public Polygon2D(List<Point2D> points) {
        this.points = new ArrayList<>(points);
    }

    public void draw(GL3 gl) {
        Point2DBuffer buffer = new Point2DBuffer(points);

        int[] names = new int[1];
        gl.glGenBuffers(1, names, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, names[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, points.size() * 2 * Float.BYTES,
                buffer.getBuffer(), GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(Shader.POSITION, 2, GL.GL_FLOAT, false, 0, 0);

        // This is so that if the polygon consists of two points, it at least draws the line.
        int drawMode = GL.GL_TRIANGLE_FAN;
        if (points.size() < 3) {
            drawMode = GL.GL_LINE_STRIP;
        }

        gl.glDrawArrays(drawMode, 0, points.size());

        gl.glDeleteBuffers(1, names, 0);
    }

    public void add(Point2D p) {
        points.add(p);
    }

    public Point2D getLast() {
        return points.get(points.size() - 1);
    }

    public List<Point2D> getPoints() {
        return points;
    }
}
