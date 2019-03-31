

//  Don't modify this file, it's created by tool 'extract_libgdx_test

package de.longri.cachebox3.platform_test.tests;

import de.longri.serializable.*;

/**
 * Created by Longri on 03.11.15.
 */
public class TestObjectFloat implements Serializable {

    protected float value1 = 0;
    protected float value2 = 0;
    protected float value3 = 0;
    protected float value4 = 0;
    protected float value5 = 0;
    protected float value6 = 0;
    protected float value7 = 0;
    protected float value8 = 0;
    protected float value9 = 0;
    protected float value10 = 0;
    protected float value11 = 0;
    protected float value12 = 0;
    protected float value13 = 0;
    protected float value14 = 0;


    @Override
    public void serialize(StoreBase writer) {
        writer.write(value1);
        writer.write(value2);
        writer.write(value3);
        writer.write(value4);
        writer.write(value5);
        writer.write(value6);
        writer.write(value7);
        writer.write(value8);
        writer.write(value9);
        writer.write(value10);
        writer.write(value11);
        writer.write(value12);
        writer.write(value13);
        writer.write(value14);
    }

    @Override
    public void deserialize(StoreBase reader) {
        value1 = reader.readFloat();
        value2 = reader.readFloat();
        value3 = reader.readFloat();
        value4 = reader.readFloat();
        value5 = reader.readFloat();
        value6 = reader.readFloat();
        value7 = reader.readFloat();
        value8 = reader.readFloat();
        value9 = reader.readFloat();
        value10 = reader.readFloat();
        value11 = reader.readFloat();
        value12 = reader.readFloat();
        value13 = reader.readFloat();
        value14 = reader.readFloat();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TestObjectFloat) {
            TestObjectFloat obj = (TestObjectFloat) other;

            if (obj.value1 != this.value1) return false;
            if (obj.value2 != this.value2) return false;
            if (obj.value3 != this.value3) return false;
            if (obj.value4 != this.value4) return false;
            if (obj.value5 != this.value5) return false;
            if (obj.value6 != this.value6) return false;
            if (obj.value7 != this.value7) return false;
            if (obj.value8 != this.value8) return false;
            if (obj.value9 != this.value9) return false;
            if (obj.value10 != this.value10) return false;
            if (obj.value11 != this.value11) return false;
            if (obj.value12 != this.value12) return false;
            if (obj.value13 != this.value13) return false;
            if (obj.value14 != this.value14) return false;


            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("value1=" + value1 + "\n");
        sb.append("value2=" + value2 + "\n");
        sb.append("value3=" + value3 + "\n");
        sb.append("value4=" + value4 + "\n");
        sb.append("value5=" + value5 + "\n");
        sb.append("value6=" + value6 + "\n");
        sb.append("value7=" + value7 + "\n");
        sb.append("value8=" + value8 + "\n");
        sb.append("value9=" + value9 + "\n");
        sb.append("value10=" + value10 + "\n");
        sb.append("value11=" + value11 + "\n");
        sb.append("value12=" + value12 + "\n");
        sb.append("value13=" + value13 + "\n");
        sb.append("value14=" + value14 + "\n");
        return sb.toString();
    }
}
