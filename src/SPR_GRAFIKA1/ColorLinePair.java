package SPR_GRAFIKA1;

import java.awt.*;
import java.io.Serializable;

public class ColorLinePair implements Serializable {

    public Color color;
    public Object object;


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


    ColorLinePair(Color color,Object object)
    {
        this.color = color;
        this.object = object;
    }

}
