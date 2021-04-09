package Coluccio;

import Brugnoli.FaithPath;

public class RedMarble extends Marble{
    /**
     * Override of the Marble method drawMarble
     */
    public void drawMarble(FaithPath faithPath)
    {
        /**
        * The only effect of the red marble is to increase the faithPath position of one step
        */
        faithPath.moveCross(1);
    }
}