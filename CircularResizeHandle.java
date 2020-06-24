package StateMachine;

import java.awt.Point; 
import java.awt.Rectangle; 
import CH.ifa.draw.framework.DrawingView; 
import CH.ifa.draw.framework.Figure; 
import CH.ifa.draw.framework.Locator; 
import CH.ifa.draw.handle.LocatorHandle; 

/** 
 * Implementation of a handle that resize its figure relative to its centre. 
 * 
 * @author Iain Simms 
 * @date 13 Nov 2008, 15:31:28 
 * @version 1.0 
 */ 
public class CircularResizeHandle extends LocatorHandle { 
	/** The owner of this handle */ 
	private final Figure owner; 

	/** The maximum size this handle can adjust to */ 
	private final int maxSize; 
	/** The minimum size this handle can adjust to */ 
	private final int minSize; 

	/** 
	 * @param owner 
	 * the owner of the handle 
	 * @param locator 
	 * the locator element to wrap (determining handle-position) 
	 * @param minSize  * the minimum size that this handle can resize to
	 * @param maxSize
	 * the maximum size that this handle can resize to 
	 */ 
	public CircularResizeHandle(final Figure owner, final Locator locator, 
			final int minSize, final int maxSize) { 
		super(owner, locator); 
		this.owner = owner; 
		this.minSize = minSize / 2; 
		this.maxSize = maxSize / 2; 
	} 

	@Override 
	public void invokeStep(final int x, final int y, final int anchorX, 
			final int anchorY, final DrawingView view) { 
		final Rectangle rect = this.owner.displayBox(); 
		final Point centre = new Point(rect.x + (rect.width / 2), rect.y 
				+ (rect.height / 2)); 
		int radius = (int) centre.distance(x, y); 
		if (radius > this.maxSize) 
			radius = this.maxSize; 
		else if (radius < this.minSize) 
			radius = this.minSize; 
		final int diameter = 2 * radius; 
		this.owner().displayBox( 
				new Rectangle(centre.x - radius, centre.y - radius, diameter, 
						diameter)); 
	} 
}