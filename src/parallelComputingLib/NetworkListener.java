package parallelComputingLib;

/**
 * <p>Title: NetworkListener</p>
 *
 * <p>Description: This interface is used to setup the event model for networking.  Classes that implement this interface
 * can listen for events that might be for them by overriding networkEventOccurred or can produce events if they are a
 * network class.  If classes using this are a network class producing events they will want to override addListener() and
 * removeListener() so that classes can register themselves as needing events sent to them.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Chris Holcombe
 * @version 1.0
 */


public interface NetworkListener {
  /**
   * Add a new listener class.  All listener classes must implement NetworkListener in order to receieve events.
   * @param listener NetworkListener
   */
  public void addListener(NetworkListener listener);
  /**
   * Removes a listener class.
   * @param listener NetworkListener
   */
  public void removeListener(NetworkListener listener);
  /**
   * This method is called on all listener's when an event has occurred.
   * @param e NetworkEvent The object which stores all pertinent details about the event.
   */
  public void networkEventOccurred(NetworkEvent e);
}
