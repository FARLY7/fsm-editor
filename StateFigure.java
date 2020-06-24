package StateMachine;

import java.awt.*;
import java.util.*;
import java.io.*;

//import orrery.NorthEastHandle;
//import orrery.NorthWestHandle;
//import orrery.SouthEastHandle;
//import orrery.SouthWestHandle;

import CH.ifa.draw.figure.CompositeFigure;
import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.*;
import CH.ifa.draw.handle.ConnectionHandle;
import CH.ifa.draw.locator.RelativeLocator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

public class StateFigure extends CompositeFigure {
	
	public static final int START = 0;
	public static final int GEN = 1;
	public static final int END = 2;
	private int type;
	
    private Rectangle fDisplayBox;
    
    private Vector<StateFigure> fPreStates;    
    private Vector<Transition> transitions;
    
    private TextFigure name;
    private EllipseFigure state;

    private static final long serialVersionUID = -7877776240236946511L;
    @SuppressWarnings("unused")
	private int pertFigureSerializedDataVersion = 1;

    public StateFigure() { this(""); }
    public StateFigure(String name) {

        fPreStates = new Vector<StateFigure>();
        fDisplayBox = new Rectangle(0, 0, 0, 0);
        transitions = new Vector<Transition>();

        type = GEN;
        
        state = new EllipseFigure();
        state.setAttribute("FillColor", new Color(0x38,0x8E,0x8E)); /* Teal */
        add(state);
        
        Font fb = new Font("Helvetica", Font.BOLD, 20);        
        this.name = new TextFigure();
        this.name.setFont(fb);
        this.name.setText(name);
        this.name.setAttribute("TextColor",Color.WHITE);
        add(this.name);
    }

    /* =============================================== */
	public void addPreState(StateFigure figure) {
		if (!fPreStates.contains(figure)) {
			fPreStates.addElement(figure);
		}
	}
	public void removePreState(StateFigure figure) {
		fPreStates.removeElement(figure);
	}
	public int getNumPreStates() {
		return fPreStates.size();
	}
	
	/* =========== TRANSITION METHODS ================ */
	public void addTransition(Transition transition) {
		transitions.add(transition);
	}
	public void removeTransition(String input) {
		for(int i = 0 ; i < transitions.size() ; i++)
		{
			if(transitions.get(i).getInput().equals(input)) transitions.remove(i);
		}
	}
	public int getNumTransitions() { return transitions.size(); }
	
	public StateFigure nextState(String input)
	{
		for(Transition t : transitions)
		{
			if(t.getInput().equals(input)) return t.getState();
		}
		return null;
	}
	
    public Transition			getTransition(int index){ return transitions.get(index); }
    public Vector<Transition>	getTransitions()		{ return transitions; }
	/* ============================================== */
    public String 	getName()				{ return name.getText(); }
    public void 	setName(String name)	{ this.name.setText(name); } 
    public void 	setColor(Color color)	{ state.setAttribute("FillColor", color); }
    public int		getType()				{ return type; }
    public void		setType(int type) {
    	this.type = type;
    	
    	switch(type){
    	case START: state.setAttribute("FillColor", new Color(56, 199, 84)); break;
    	case GEN: state.setAttribute("FillColor", new Color(0x38,0x8E,0x8E)); break;
    	case END: state.setAttribute("FillColor", new Color(227, 64, 64)); break;
    	};	
    }
    /* ================================================ */


    protected void basicMoveBy(int x, int y) {
	    fDisplayBox.translate(x, y);
	    super.basicMoveBy(x, y);
	}

    public Rectangle displayBox() {
        return new Rectangle(
            fDisplayBox.x,
            fDisplayBox.y,
            fDisplayBox.width,
            fDisplayBox.height);
    }

    @Override
    public void basicDisplayBox(Point origin, Point corner) {
    	
		int width = corner.x - origin.x; 
		int height = corner.y - origin.y; 
		int size = Math.max(width, height);
		
		if(size < 30) size = 30;
		if(size > 60) size = 60;
		corner = new Point(origin.x + size, origin.y + size); 
		
		fDisplayBox = new Rectangle(origin);
		fDisplayBox.add(corner);

		/* \/\/\/\/\/\/\/\/ Layout \/\/\/\/\/\/\/\/\/ */
		Enumeration<Figure> k = figures();
		while (k.hasMoreElements()) 
		{	
			Figure f = k.nextElement();
			if(f instanceof EllipseFigure)
			{
				f.basicDisplayBox(origin, corner);
			}
			else if(f instanceof TextFigure)
			{							
				f.basicDisplayBox(origin, corner);
				
				int textW  = (int) f.size().getWidth();
				int textH  = (int) f.size().getHeight();
				
				int diffX = (int) fDisplayBox.getCenterX() - f.center().x;
				int diffY = (int) fDisplayBox.getCenterY() - f.center().y;			
				
				Point origin2 = new Point(fDisplayBox.x + diffX, fDisplayBox.y + diffY);
				Point corner2 = new Point(origin2.x 	+ textW, origin2.y	   + textH);
				
				f.basicDisplayBox(origin2, corner2);		
			}
		}; 
    }


    public void WhoAmI()
    {    	
    	System.out.println("\n==== State: " + getName() + " ====");
    	if(fPreStates.size() != 0) {
	    	for(StateFigure state : fPreStates) {
	    		System.out.println("(in) " + state.getName());
	    	}
    	}
    	if(transitions.size() != 0) {
	    	for(Transition t : transitions) {
	    		System.out.println("(out) " + t.getState().getName() + "(" + t.getInput() + ")");
	    	}
    	}
    	System.out.println("===================");
    }
    
    public void printTransitions()
    {
    	System.out.println("\n== State: " + getName() + " ==");
    	for(Transition t : transitions)
    	{
    		System.out.println("(" + t.getInput() + ") ----> " + t.getState().getName());
    	}
    }

    public void draw(Graphics g) {  	
        super.draw(g);
    }

    public Vector<Handle> handles() {
		
		Vector <Handle> handles = new Vector <Handle> (); 
		handles.add(new CircularResizeHandle(this, RelativeLocator.southWest(), 30, 60)); 
		handles.add(new CircularResizeHandle(this, RelativeLocator.southEast(), 30, 60)); 
		handles.add(new CircularResizeHandle(this, RelativeLocator.northWest(), 30, 60)); 
		handles.add(new CircularResizeHandle(this, RelativeLocator.northEast(), 30, 60)); 
		
		handles.addElement(new ConnectionHandle(this, RelativeLocator.north(),
				new StateConnection()));
		handles.addElement(new ConnectionHandle(this, RelativeLocator.east(),
				new StateConnection()));
		handles.addElement(new ConnectionHandle(this, RelativeLocator.south(),
				new StateConnection()));
		handles.addElement(new ConnectionHandle(this, RelativeLocator.west(),
				new StateConnection()));
		return handles; 
    }

    public void update(FigureChangeEvent e) {
//        if (e.getFigure() == figureAt(1))  // duration has changed
//            updateDurations();
       // if (needsLayout()) {
    	
            //layout();
            changed();
//  }
    }

    public void figureChanged(FigureChangeEvent e) {
        update(e);
    }


    public void figureRemoved(FigureChangeEvent e) {
        update(e);
    }
    
    public Insets connectionInsets() {
        Rectangle r = fDisplayBox;
        int cx = r.width/2;
        int cy = r.height/2;
        return new Insets(cy, cx, cy, cx);
    }
    
    //-- store / load ------------ FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE
    //  FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE FIX THESE

    public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeInt(fDisplayBox.x);
        dw.writeInt(fDisplayBox.y);
        dw.writeInt(fDisplayBox.width);
        dw.writeInt(fDisplayBox.height);

//        writeTasks(dw, fPreStates);
//        writeTasks(dw, transitions);
//        writeTasks(dw, fPostStates);
    }

//    public void writeTasks(StorableOutput dw, Vector<CompStateFigure> v) {
//        dw.writeInt(v.size());
//        Enumeration<CompStateFigure> i = v.elements();
//        while (i.hasMoreElements())
//            dw.writeStorable((Storable) i.nextElement());
//    }

    public void read(StorableInput dr) throws IOException {
        super.read(dr);
        fDisplayBox = new Rectangle(
            dr.readInt(),
            dr.readInt(),
            dr.readInt(),
            dr.readInt());
      //  layout();
        fPreStates = readTasks(dr);
//        fPostStates = readTasks(dr);
    }

    public Vector<StateFigure> readTasks(StorableInput dr) throws IOException {
        int size = dr.readInt();
        Vector<StateFigure> v = new Vector<StateFigure>(size);
        for (int i=0; i<size; i++)
            v.addElement((StateFigure)dr.readStorable());
        return v;
    }
}
