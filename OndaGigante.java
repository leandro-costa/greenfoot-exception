import greenfoot.*;

/**
 * Apaga a chama: usar() devolve o valor negativo.
 * No mar, a onda avança da direita para a esquerda.
 */
public class OndaGigante extends ItemDaTempestade
{
    public OndaGigante(String id, int valor) throws RecursoInvalidoException
    {
        super(id, valor);
        setImage(desenhar());
    }

    @Override
    public int usar()
    {
        return -getValor();
    }

    @Override
    public void act()
    {
        super.act();
        if (getWorld() != null && !((IlhaDasTempestades) getWorld()).isEncerrado()) {
            setLocation(getX() - 2, getY());
            if (getX() <= IlhaDasTempestades.MAR_ESQUERDA) {
                getWorld().removeObject(this);
            }
        }
    }

    private static GreenfootImage desenhar()
    {
        GreenfootImage img = new GreenfootImage(44, 30);
        img.setColor(new Color(30, 90, 200));
        img.fillOval(0, 8, 44, 30);
        img.setColor(new Color(90, 160, 240));
        img.fillOval(4, 2, 26, 22);
        img.setColor(new Color(235, 245, 255));
        img.fillOval(4, 2, 12, 8);
        img.fillOval(14, 0, 10, 7);
        return img;
    }
}
