/**
 * 
 */
package unsw.graphics.examples;


import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL3;
import unsw.graphics.Application2D;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Polygon2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Use the mouse to draw line strips.
 * 
 * Left-click to place a point, right-click to complete the current strip.
 * 
 * @author Thomas Moffet
 * Mostly inspired by LineDrawing.
 */
public class PolygonDrawing extends Application2D {

    private Polygon2D currentPolygon;

    private Point2D currentPoint;

    private List<Polygon2D> finishedPolygons;

    public PolygonDrawing() {
        super("Line Drawing", 600, 600);
        currentPolygon = new Polygon2D();
        currentPoint = new Point2D(0,0);
        finishedPolygons = new ArrayList<>();
    }

    public static void main(String[] args) {
        PolygonDrawing example = new PolygonDrawing();
        example.start();
    }
    
    @Override
    public void init(GL3 gl) {
        super.init(gl);
        getWindow().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON1)
                    currentPolygon.add(currentPoint);
                else if (ev.getButton() == MouseEvent.BUTTON3) {
                    finishedPolygons.add(currentPolygon);
                    currentPolygon = new Polygon2D();
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent ev) {
                currentPoint = fromScreenCoords(ev);
            }
        });
    }
    
    @Override
    public void display(GL3 gl) {
        super.display(gl);
        
        currentPolygon.draw(gl);
        currentPoint.draw(gl);
        
        for (Polygon2D polygon : finishedPolygons)
            polygon.draw(gl);
        
        if (!currentPolygon.getPoints().isEmpty()) {
            Line2D incomplete = new Line2D(currentPoint, currentPolygon.getLast());
            incomplete.draw(gl);
        }
    }

    private Point2D fromScreenCoords(MouseEvent ev) {
        //We need to map from pixel coordinates to coordinates on the canvas
        float x = 2f*ev.getX()/getWindow().getSurfaceWidth() - 1;
        float y = -2f*ev.getY()/getWindow().getSurfaceHeight() + 1;
        return new Point2D(x, y);
    }

}
