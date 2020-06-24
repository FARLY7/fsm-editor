package StateMachine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.samples.javadraw.MySelectionTool;
import CH.ifa.draw.samples.net.NodeFigure;
import CH.ifa.draw.samples.pert.PertFigureCreationTool;
import CH.ifa.draw.tool.ConnectedTextTool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.TextTool;


public  class Application extends DrawApplication {

	private static final long serialVersionUID = -7715041272799303145L;
	static private final String PERTIMAGES = "/CH/ifa/draw/samples/pert/images/";

    public static void main(String[] args) {
		Application pert = new Application();
		pert.open();
		pert.setBounds(200, 200, 800, 600);
    }

    Application() {
        super("State Machine Editor");
    }

    //-- main -----------------------------------------------------------

	@Override
	protected void createTools(JPanel palette) {
        super.createTools(palette);  
        
        Tool tool = new StateCreationTool(view(), new StateFigure());
        palette.add(createToolButton("/StateMachine/STATE", "State Tool", tool));

              
		tool = new StateConnectionTool(view(), new StateConnection());
		palette.add(createToolButton("/StateMachine/TRANS", "State Transition Tool", tool));

		tool = new TextTool(view(), new TextFigure());
		palette.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));
    }
	
	@Override
	protected Tool createSelectionTool() {
		return new StateMachine.MySelectionTool(view());
	}
	
	
	@Override
	protected void createMenus(JMenuBar mb) {
		super.createMenus(mb);
		mb.add(createPrintTreeMenu());
		mb.add(createSimulationMenu());
	}
	
	protected JMenu createPrintTreeMenu() {
		
		JMenu menu = new JMenu("State Machine");
		JMenuItem mi = new JMenuItem("Print Transitions");
		
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("____ State Machine Transitions ____");
				
				Enumeration<Figure> figures = drawing().figures();
				for ( ; figures.hasMoreElements(); )
				{ 
					Figure f = figures.nextElement();
					if(f instanceof StateFigure)
						// ((CompStateFigure) f).WhoAmI();
						((StateFigure) f).printTransitions();
				}
			}
		});
		menu.add(mi);

		return menu;
	}
	
	/* Hacked together simulation just for proof of concept. It only works if you pass in a valid string.
	 */
	public JMenu createSimulationMenu() {
		
		JMenu menu = new JMenu("Simulation");
		
		JMenuItem mi = new JMenuItem("Check Validity");		
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame frame = new JFrame();
				
				if(checkValidity())
				{	
					JOptionPane.showMessageDialog(frame, "State Machine is VALID");
				}
				else JOptionPane.showMessageDialog(frame, "State Machine is INVALID", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		menu.add(mi);
		
		mi = new JMenuItem("Run Sample Input");		
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame frame = new JFrame();
				String input = "";
	    		
				if(checkValidity())
	    			input = JOptionPane.showInputDialog("Please enter the input variable for transition: ");
	    		else JOptionPane.showMessageDialog(frame, "State Machine is INVALID", "Error", JOptionPane.ERROR_MESSAGE);
		    	
	    		if(input != null)
		    		runSimulation(input);
			}
		});
		menu.add(mi);
		return menu;
	}
	
	public void runSimulation(String input)
	{
		Enumeration<Figure> figures = drawing().figures();
		JFrame frame = new JFrame();

		System.out.println("------- Simulation Output ------");
		for ( ; figures.hasMoreElements() ; )
		{ 
			Figure oldF = figures.nextElement();
			if(oldF instanceof StateFigure)
			{	
				StateFigure state = (StateFigure) oldF;
				if(state.getType() == StateFigure.START)
				{	
					for(int i = 0 ; i < input.length() ; i++)
					{
						StateFigure nextState = state.nextState(input.charAt(i) + "");
						if(nextState != null)
						{
							System.out.println("\t" + state.getName() + " --- (" + input.charAt(i) + ") ---> " + nextState.getName());
							state = nextState;
						} else break;
					}
					
				if(state.getType() == StateFigure.END) JOptionPane.showMessageDialog(frame, "Input String is VALID");
				else { JOptionPane.showMessageDialog(frame, "Input String is INVALID", "Error", JOptionPane.ERROR_MESSAGE); break; }
				}
			}
		}
		System.out.println("--------------------------------");
	}
	
	public boolean checkValidity()
	{
		boolean startFound = false;
		
		Enumeration<Figure> figures = drawing().figures();
		for ( ; figures.hasMoreElements() ; )
		{ 
			Figure f = figures.nextElement();
			if(f instanceof StateFigure)
			{
				StateFigure state = (StateFigure) f;
				if(state.getTransitions().size() != 2) return false;
				if(startFound == true && state.getType() == StateFigure.START) return false;
				if(startFound == false && state.getType() == StateFigure.START) startFound = true;
			}
		}
		return startFound;
	}
}