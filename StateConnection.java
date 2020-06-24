package StateMachine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;

import CH.ifa.draw.command.Command;
import CH.ifa.draw.command.DeleteCommand;
import CH.ifa.draw.figure.ArrowTip;
import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.NullHandle;
import CH.ifa.draw.samples.pert.PertFigure;
import CH.ifa.draw.util.Animatable;
import CH.ifa.draw.util.TextHolder;


public class StateConnection extends LineConnection{
    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -7959500008698525009L;
    @SuppressWarnings("unused")
	private int pertDependencySerializedDataVersion = 1;
    
    private TextFigure name;

    public StateConnection() {
        setEndDecoration(new ArrowTip());
        setStartDecoration(null);
        fFrameColor = Color.BLACK;

        Font fb = new Font("Helvetica", Font.BOLD, 20);   
        name = new TextFigure();       
        name.setFont(fb);
        name.setAttribute("TextColor", Color.BLACK);
    }
    
    @Override
	public boolean canConnect(Figure start, Figure end) {
    	
    	if(start instanceof StateFigure && end instanceof StateFigure)
    	{       	
        	StateFigure source = (StateFigure) start;
        	
    		if(source.getNumTransitions() < 2) return true;
	    	else return false;
    	}
    	else return false;
    }

	@Override
	public void draw(Graphics g) {
		
		g.setColor(getFrameColor());
		
		if(fPoints.size() == 2) /* Straight arrow connection */
		{
			Point p1, p2;
			for (int i = 0; i < fPoints.size() - 1; i++) {
				p1 = fPoints.elementAt(i);
				p2 = fPoints.elementAt(i + 1);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
		else if(fPoints.size() == 3) /* Arc Connection to another state */
		{
			Point P1 = fPoints.elementAt(0);
			Point ctrl = fPoints.elementAt(1);
			Point P2 = fPoints.elementAt(2);
				
			/* Cubic curve requires 4 points, initial, end, and 2 control points.
			 * Just use the same value for control point 1 and 2 */
			CubicCurve2D.Double cubicCurve;
			
			cubicCurve = new CubicCurve2D.Double(P1.getX(), P1.getY(),
												 ctrl.getX(), ctrl.getY(),
												 ctrl.getX(), ctrl.getY(),
												 P2.getX(), P2.getY());
			
			Graphics2D g2 = (Graphics2D)g;
			g2.draw(cubicCurve);
		}
		else if(fPoints.size() == 4) /* Arc connection to the same state */
		{
			Point P1 = fPoints.elementAt(0);
			Point ctrl1 = fPoints.elementAt(1);
			Point ctrl2 = fPoints.elementAt(2);
			Point P2 = fPoints.elementAt(3);
				
			/* Cubic curve requires 4 points, initial, end, and 2 control points. */
			CubicCurve2D.Double cubicCurve;
			
			cubicCurve = new CubicCurve2D.Double(P1.getX(), P1.getY(),
												 ctrl1.getX(), ctrl1.getY(),
												 ctrl2.getX(), ctrl2.getY(),
												 P2.getX(), P2.getY());
			
			Graphics2D g2 = (Graphics2D)g;
			g2.draw(cubicCurve);
		}
		decorate(g);
		
		name.draw(g);
	} 
	
	private void decorate(Graphics g) {
		Point p3 = fPoints.elementAt(fPoints.size() - 2);
		Point p4 = fPoints.elementAt(fPoints.size() - 1);
		fEndDecoration.draw(g, p4.x, p4.y, p3.x, p3.y);
	}
    
    @Override
    public void handleConnect(Figure start, Figure end) {

    	StateFigure source = (StateFigure) start;
    	StateFigure target = (StateFigure) end;
    
    	int midX = (target.center().x - source.center().x) / 2;
    	int midY = (target.center().y - source.center().y) / 2;
    	 	    	
    	String input = JOptionPane.showInputDialog("Please enter the input variable for transition: ");
   	
    	if(source.nextState(input) == null)
    	{
    		if(input != null)
    		{
	        	name.setText(input); 
	        	
	    		source.addTransition(new Transition(target, input));
				target.addPreState(source);
			
	    		if(source == target) /* Add two control points to line if the state is connected to itself */
	    		{
	    			insertPointAt(new Point(source.center().x + midX + 50, source.center().y + midY - 50), fPoints.size()-1);
	    			insertPointAt(new Point(source.center().x + midX + 75, source.center().y + midY - 10), fPoints.size()-1);
	    		}
	    		else insertPointAt(new Point(source.center().x + midX, source.center().y + midY), fPoints.size()-1);
	
		        TextHolder textHolder = (TextHolder) ((Figure) name);
				textHolder.connect(this);
    		}
    		else listener().figureRequestRemove(new FigureChangeEvent(this));
    	} else {
    		listener().figureRequestRemove(new FigureChangeEvent(this));
    	}
    }
    
    @Override
	public void handleDisconnect(Figure start, Figure end) {
    	StateFigure source = (StateFigure) start;
    	StateFigure target = (StateFigure) end;
        if (target != null) {
            target.removePreState(source);
        }
        if (source != null) {
        	source.removeTransition(name.getText());
        }
   }


    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = super.handles();
        // don't allow to reconnect the starting figure
        handles.setElementAt(
            new NullHandle(this, PolyLineFigure.locator(0)), 0);
        return handles;
    }

}