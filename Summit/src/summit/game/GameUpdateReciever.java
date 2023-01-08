/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

public interface GameUpdateReciever{

    /**
     * This method allows {@code GameObjects} to recieve game information through gameupdateeventssw  
     * 
     * @param e A {@link GameUpdateEvent} provided by the 
    */
    public void update(GameUpdateEvent e) throws Exception;
}