package StateMachine;

import java.awt.event.MouseEvent;
//import orrery.AtmosphereDecorator;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.SelectionTool;

/**
 * A SelectionTool that interprets double clicks to inspect the clicked figure
 */

public  class MySelectionTool extends SelectionTool {

    public MySelectionTool(DrawingView view) {
        super(view);
    }
    
    /**
     * Handles mouse down events and starts the corresponding tracker.
     */
    @Override
	public void mouseDown(MouseEvent e, int x, int y) {
       
    	if (e.getClickCount() == 2) {
        	
            Figure figure = drawing().findFigure(e.getX(), e.getY());
            if (figure != null && figure instanceof StateFigure)
            {
            	StateFigure state = (StateFigure) figure;

            	if(state.getType() == StateFigure.GEN) state.setType(StateFigure.START);
            	else if(state.getType() == StateFigure.START) state.setType(StateFigure.END);
            	else if(state.getType() == StateFigure.END) state.setType(StateFigure.GEN);

                return;
            }
        }
        super.mouseDown(e, x, y);
    }
}