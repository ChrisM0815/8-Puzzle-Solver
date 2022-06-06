import java.util.ArrayList;
import java.util.Arrays;


public class KnotenH extends Knoten implements Comparable<KnotenH>{
    private int g;//Kostenfunktion
    private int h;//heuristische Kostenschätzfunktion
    private int f;//heuristische Bewertungsfunktion
    private boolean useH1;

    public KnotenH(int[][] field , boolean useH1) {

        setField(field);
        this.useH1 = useH1;
        setG();
        setH(this.useH1);
        setF();

    }

    public KnotenH(boolean useH1,int[][] field,int posx, int posy,Knoten parent)
    {
        initField();
        setParent(parent);
        setField(field);
        swap(posx,posy);
        this.useH1 = useH1;
        setG();
        setH(this.useH1);
        setF();

    }

    public void setG(){
        Knoten current = this.getParent();
        int counter = 0;
        while(current != null)
        {
            counter++;
            current = current.getParent();
        }
        this.g = counter;
    }

    //Joice between H1 and H2
    public void setH(boolean useH1){
        if(useH1) setH1();
        else setH2();
    }
    //Sets h to the number of wrong Fields
    private void setH1(){
        int wrongFields = 0;
        int currentField = 1;
        for(int y = 0;y < 3;y++) {
            for (int x = 0; x < 3; x++) {

                if(currentField == 9) currentField = 0;
                if(getNumber(y,x) != currentField) wrongFields++;
                currentField++;
            }
        }
        this.h = wrongFields;
    }

    //set h to Manhattan distance for all Fields
    private void setH2(){
        int sum = 0;
        int xi;//X-Koordinate the value in the currently look at Field is supposed to be
        int yi;//Y-Koordinate the value in the currently look at Field is supposed to be

        for(int y = 0;y < 3;y++) {
            for (int x = 0; x < 3; x++) {
                int value = getNumber(y,x);
                if(value != 0) {
                    xi = (value - 1) % 3;
                    yi = ((value - 1) - xi) / 3;
                    sum = sum + Math.abs(x - xi) + Math.abs(y - yi);
                }
                else{
                    sum = sum + Math.abs(x - 2) + Math.abs(y - 2);
                }

            }
        }
        this.h = sum;
    }

    public void setF(){
        this.f = this.g + this.h;
    }

    public int getF(){
        return this.f;
    }
    public int getG(){
        return this.g;
    }

    public int getH(){
        return this.h;
    }

    public ArrayList<KnotenH> getChildrenH(){
        ArrayList<KnotenH> children = new ArrayList<>();
        int[] zero = getPosition(0);

        if(zero[0] != 2) {
            children.add(new KnotenH(this.useH1,this.getField(),zero[0]+1,zero[1],this));
        }
        if(zero[0] != 0) {
            children.add(new KnotenH(this.useH1,this.getField(),zero[0]-1,zero[1],this));
        }
        if(zero[1] != 2) {

            children.add(new KnotenH(this.useH1,this.getField(),zero[0],zero[1]+1,this));
        }
        if(zero[1] != 0) {

            children.add(new KnotenH(this.useH1,this.getField(),zero[0],zero[1]-1,this));
        }

        if(children.size() == 0) return null;//Something went wrong if this is returned
        return children;
    }

    public ArrayList<KnotenH> getChildrenNoCycleH(){
        ArrayList<KnotenH> children = new ArrayList<KnotenH>();
        for (KnotenH i:getChildrenH()){
            if(getParent() != null) {
                if (!Arrays.deepEquals(getParent().getField(), i.getField())) {
                    children.add(i);
                }
            }
            else children.add(i);
        }
        return children;
    }


    @Override
    public void printKnoten(){
        for (byte x = 0;x < 3;x++){
            for (byte y = 0;y < 3;y++) {
                System.out.print(getNumber(x,y)+" ");
            }
            System.out.println();
        }
        System.out.println("Kostenfunktion g:"+this.g+" | Kostenschätzung h:"+this.h+ " | heuristische Bewertung f:"+this.f);
        System.out.println();
    }


    @Override
    public int compareTo(KnotenH knotenH) {
        if(f == knotenH.getF())
            return 0;
        else if (f > knotenH.getF())
            return 1;
        else
            return -1;
    }


}
