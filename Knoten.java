import java.util.ArrayList;
import java.util.Arrays;

public class Knoten {
    private int[][] field;
    private Knoten parent;

    public Knoten() {
        initField();
        setParent(null);
    }

    /*Used when creating child nodes
    * Parameters:
    * field of Parent node
    * Coordinates of the field to be swapped with the empty field */
    public Knoten(int[][] field,int posx, int posy,Knoten parent)
    {
        initField();
        setParent(parent);
        setField(field);
        swap(posx,posy);

    }

    public void initField(){
        this.field = new int[3][3];
    }

    public Knoten getParent()
    {
        return this.parent;
    }

    public int getNumber(int posx, int posy)
    {
        return this.field[posx][posy];
    }

    public void setNumber(int posx, int posy, int value)
    {
        this.field[posx][posy] = value;
    }

    public int[] getPosition(int value)
    {
        int[] pos = new int[2];
        for(int x = 0;x < 3;x++){
            for(int y = 0;y < 3;y++){
                if(getNumber(x,y) == value){
                    pos[0] = x;
                    pos[1] = y;
                    return pos;
                }
            }
        }
        return null;
    }

    public void setField(int[][] field)
    {
        for(int x = 0;x < 3;x++){
            for(int y = 0;y < 3;y++){
                setNumber(x,y,field[x][y]);
            }
        }
    }

    public int[][] getField()
    {
        return this.field;
    }

    public void setParent(Knoten parent) {
        this.parent = parent;
    }

    //Swaps given Field with empty Field
    public void swap(int posx, int posy)
    {
        int tmp = getNumber(posx,posy);
        int[] zero = getPosition(0);
        setNumber(posx,posy,0);
        setNumber(zero[0],zero[1],tmp);
    }

    public void printKnoten(){
        for (byte x = 0;x < 3;x++){
            for (byte y = 0;y < 3;y++) {
                System.out.print(getNumber(x,y)+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public ArrayList<Knoten> getChildren(){

        ArrayList<Knoten> children = new ArrayList<Knoten>();
        int[] zero = getPosition(0);

        if(zero[0] != 2) {
            children.add(new Knoten(field,zero[0]+1,zero[1],this));
        }
        if(zero[0] != 0) {

            children.add(new Knoten(field,zero[0]-1,zero[1],this));
        }
        if(zero[1] != 2) {
            children.add(new Knoten(field, zero[0],zero[1]+1,this));
        }
        if(zero[1] != 0) {
            children.add(new Knoten(field, zero[0],zero[1]-1,this));
        }

        return children;

    }

    public ArrayList<Knoten> getChildrenNoCycle(){
        ArrayList<Knoten> children = new ArrayList<Knoten>();
        for (Knoten i:getChildren()){
            if(getParent() != null) {
                if (!Arrays.deepEquals(getParent().getField(), i.getField())) {
                    children.add(i);
                }
            }
            else children.add(i);
        }
        return children;
    }



}





