package Shamir;

public class ShamKeyArray {
    private ShamirKey [] keys = null;

    public ShamKeyArray (ShamirKey [] keys)
    {
        this.keys = keys ;
    }

    public ShamirKey [] getTab()
    {
        return keys;
    }
}
