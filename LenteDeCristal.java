import greenfoot.*;

/** Concentra a luz: usar() devolve o dobro do valor. */
public class LenteDeCristal extends ItemDaTempestade
{
    public LenteDeCristal(String id, int valor) throws RecursoInvalidoException
    {
        super(id, valor);
        setImage(desenhar());
    }

    @Override
    public int usar()
    {
        return getValor() * 2;
    }

    private static GreenfootImage desenhar()
    {
        GreenfootImage img = new GreenfootImage(26, 26);
        img.setColor(new Color(120, 230, 255));
        img.fillPolygon(new int[] {13, 25, 13, 1}, new int[] {1, 13, 25, 13}, 4);
        img.setColor(new Color(230, 255, 255));
        img.fillPolygon(new int[] {13, 19, 13, 7}, new int[] {6, 13, 20, 13}, 4);
        img.setColor(new Color(40, 120, 160));
        img.drawPolygon(new int[] {13, 25, 13, 1}, new int[] {1, 13, 25, 13}, 4);
        return img;
    }
}
