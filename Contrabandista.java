import greenfoot.*;

/**
 * Inimigo que vaga pela ilha (reaproveita vagar() do molde).
 *
 * Ao encostar no Faroleiro, tenta sabotá-lo entregando uma OndaGigante
 * com um valor sorteado. Às vezes o valor sorteado é inválido e a
 * própria onda nem chega a existir: RecursoInvalidoException.
 */
public class Contrabandista extends HabitanteDaIlha
{
    private static final int ESPERA_ENTRE_SABOTAGENS = 120;

    private String tipo;
    private int espera = 0;
    private int sabotagens = 0;

    public Contrabandista(String id, int posX, int posY, int energia, String tipo)
    {
        super(id, posX, posY, energia);
        this.tipo = tipo;
        setImage(desenhar());
    }

    /** Sobrescreve SEM throws: pode lançar menos do que o molde. */
    @Override
    public void patrulhar()
    {
        vagar(2, 4);
    }

    @Override
    public void act()
    {
        if (!emJogo()) {
            return;
        }
        patrulhar();
        if (espera > 0) {
            espera--;
            return;
        }
        Faroleiro faroleiro = faroleiroAoAlcance();
        if (faroleiro != null) {
            sabotar(faroleiro);
        }
    }

    private void sabotar(Faroleiro faroleiro)
    {
        sabotagens++;
        int forca = Greenfoot.getRandomNumber(31) - 5;   // de -5 a 25
        try {
            RecursoDoFarol onda = new OndaGigante(getId() + "-S" + sabotagens, forca);
            faroleiro.operarRecurso(onda);
            IO.println(getId() + " (" + tipo + ") sabotou " + faroleiro.getId() + " com uma onda de " + forca);
        } catch (RecursoInvalidoException e) {
            IO.println("Sabotagem falhou: " + e.getMessage());
        } catch (FaroleiroSemEnergiaException e) {
            ilha().encerrar(e.getMessage(), false);
        } finally {
            espera = ESPERA_ENTRE_SABOTAGENS;
        }
    }

    private static GreenfootImage desenhar()
    {
        GreenfootImage img = new GreenfootImage(30, 40);
        // casaco escuro
        img.setColor(new Color(110, 30, 40));
        img.fillRect(6, 16, 18, 24);
        // rosto
        img.setColor(new Color(220, 180, 140));
        img.fillOval(9, 6, 12, 12);
        // tapa-olho
        img.setColor(Color.BLACK);
        img.fillOval(16, 10, 4, 4);
        img.drawLine(9, 11, 21, 11);
        // chapéu de três pontas
        img.setColor(new Color(30, 30, 30));
        img.fillPolygon(new int[] {2, 15, 28}, new int[] {8, 0, 8}, 3);
        // barril de contrabando
        img.setColor(new Color(140, 90, 40));
        img.fillOval(0, 24, 10, 14);
        return img;
    }
}
