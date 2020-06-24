package StateMachine;

import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.ConnectionTool;

public class StateConnectionTool extends ConnectionTool{

	public StateConnectionTool(DrawingView view, ConnectionFigure prototype) {
		super(view, prototype);
	}
	
    /**
     * Finds a connectable figure target.
     */
	@Override
    protected Figure findTarget(int x, int y, Drawing drawing) {
        Figure target = findConnectableFigure(x, y, drawing);
        Figure start = this.getStartConnector().owner();

        if (target != null
             && this.createdFigure() != null
             && target.canConnect()
             //&& !target.includes(start)
             && this.createdFigure().canConnect(start, target))
            return target;
        return null;
    }

}
