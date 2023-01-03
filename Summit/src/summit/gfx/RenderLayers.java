/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

import java.util.ArrayList;

/**
 * The RenderLayers class stores all the Paintable objects to be rendered, which are stored in layers.
 * Each layer is an ArrayList of Paintable objects, and there are 10 layers stored in an array.
 * Once all the Paintable objects have been added, the {@link RenderLayers#renderLayers(PaintEvent)} method is called, 
 * which calls the {@link Paintable#paint(PaintEvent)} method on all the objects in order of their layer.
 */
public class RenderLayers{

    /**The layer index to which game tiles are added to */
    public static final int TILE_LAYER = 2;

    /**The layer index to which enitites and structures are added to */
    public static final int STRUCTURE_ENTITY_LAYER = 5;

    /**The top most render layer*/
    public static final int TOP_LAYER = 9;

    private ArrayList<Paintable>[] layers;

    @SuppressWarnings(value = "unchecked")
    public RenderLayers(int n_layers){
        this.layers = new ArrayList[n_layers];

        for (int i = 0; i < layers.length; i++) {
            layers[i] = new ArrayList<>();
        }
    }

    public void addToLayer(int layer, Paintable p){
        this.layers[layer].add(p);
    }

    public ArrayList<Paintable> getLayer(int layer){
        return this.layers[layer];
    }

    /**
     * Calls the paint method for all added {@link Paintable} objects in each layer, in order
     * 
     * @param e A {@link PaintEvent} object
     */
    public void renderLayers(PaintEvent e){
        for (ArrayList<Paintable> layer : layers) {
            for (Paintable p : layer) {
                try{
                    p.paint(e);
                } catch(Exception ex){
                    System.out.println(ex);
                }
            }
        }
    }
}
