/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

/**
 * 
 * The GameClickReciever interface defines a method for receiving game click
 * events.
 * 
 * Classes that implement this interface will be notified when a game click
 * event occurs.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.u
 */
public interface GameClickReciever {

    /**
     * 
     * This method is called when a game click event occurs.
     * 
     * @param e the GameUpdateEvent that contains information about the click event
     */
    public void gameClick(GameUpdateEvent e);
}
