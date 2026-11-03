import greenfoot.*;

/** Abastece o farol: usar() devolve o próprio valor. */
public class Querosene extends ItemDaTempestade
{
    public Querosene(String id, int valor) throws RecursoInvalidoException
    {
        super(id, valor);
        setImage(desenhar());
    }

    @Override
    public int usar()
    {
        return getValor();
    }

    private static GreenfootImage desenhar()
    {
        GreenfootImage img = new GreenfootImage(22, 28);
        img.setColor(new Color(230, 120, 20));
        img.fillRect(2, 8, 18, 20);
        img.setColor(new Color(160, 70, 10));
        img.fillRect(12, 2, 6, 7);
        img.drawRect(2, 8, 17, 19);
        img.setColor(Color.WHITE);
        img.fillPolygon(new int[] {11, 7, 15}, new int[] {12, 22, 22}, 3);
        return img;
    }
}
