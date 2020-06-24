package StateMachine;

import java.util.Enumeration;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.util.HJDError;

public  class StateCreationTool extends CreationTool {

    public StateCreationTool(DrawingView view, Figure prototype) {
        super(view, prototype);
    }

    @Override
	protected Figure createFigure() {
    	/* Written this piece of code so tool still complies with Prototype design pattern 
    	 * Calls the super.createFigure to do the cloning of prototype, and then appends the
    	 * states name */
    	StateFigure state = (StateFigure) super.createFigure();
        state.setName("S" + numberOfStates());
        return (Figure) state;
    }
    
    public int numberOfStates()
    {
		Enumeration<Figure> figures = drawing().figures();
		int count = 0;
		
		for ( ; figures.hasMoreElements(); ) { 
			if(figures.nextElement() instanceof StateFigure)
				count++;
		}
		return count;
    }
}
