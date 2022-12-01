/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

public interface GameUpdateReciever{

    /**
    * ticking + game physics + situational checking
    */
    public void update(GameUpdateEvent e);
}