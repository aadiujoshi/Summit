/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

import java.util.ArrayList;

public class RenderLayers{

    public static final int TILE_LAYER = 2;
    public static final int STRUCTURE_ENTITY_LAYER = 5;
    public static final int TOP_LAYER = 9;

    private ArrayList<Paintable>[] layers;

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

    public void renderLayers(PaintEvent e){
        for (ArrayList<Paintable> layer : layers) {
            for (Paintable p : layer) {
                p.paint(e);
            }
            // System.out.println("[" + layer.size() + "]");
        }
        // System.out.println("-----------------------------------------------");
    }
}
