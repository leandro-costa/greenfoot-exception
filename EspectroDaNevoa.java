import greenfoot.*;

/**
 * Inimigo lento e semitransparente (reaproveita vagar() do molde).
 *
 * Ao encostar no Faroleiro, "entrega" um recurso feito de névoa,
 * que não existe: operarRecurso(null). Na revisão isso era ignorado
 * em silêncio; agora vira IllegalArgumentException e é tratado aqui.
 */
public class EspectroDaNevoa extends HabitanteDaIlha
{
    private static final int ESPERA_ENTRE_ASSOMBROS = 150;

    private int nivel;
    private int espera = 0;
    private int fase = 0;

    public EspectroDaNevoa(String id, int posX, int posY, int energia, int nivel)
    {
        super(id, posX, posY, energia);
        this.nivel = nivel;
        setImage(desenhar());
    }

    /** Sobrescreve SEM throws: pode lançar menos do que o molde. */
    @Override
    public void patrulhar()
    {
        vagar(nivel, 2);
        // a névoa pulsa: transparência entre 80 e 200
        fase = (fase + 3) % 240;
        int alfa = fase < 120 ? 80 + fase : 320 - fase;
        getImage().setTransparency(alfa);
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
            assombrar(faroleiro);
        }
    }

    private void assombrar(Faroleiro faroleiro)
    {
        espera = ESPERA_ENTRE_ASSOMBROS;
        IO.println(getId() + " entrega a " + faroleiro.getId() + " um recurso feito de névoa...");
        try {
            faroleiro.operarRecurso(null);
        } catch (IllegalArgumentException e) {
            IO.println("Operação recusada: " + e.getMessage());
        } catch (FaroleiroSemEnergiaException e) {
            ilha().encerrar(e.getMessage(), false);
        } finally {
            IO.println(faroleiro.getId() + " está com energia " + faroleiro.getEnergia());
        }
    }

    private static GreenfootImage desenhar()
    {
        GreenfootImage img = new GreenfootImage(36, 42);
        img.setColor(new Color(225, 235, 245));
        img.fillOval(4, 0, 28, 28);
        img.fillRect(4, 14, 28, 20);
        // barra ondulada
        img.fillOval(4, 30, 9, 10);
        img.fillOval(13, 30, 9, 12);
        img.fillOval(22, 30, 10, 10);
        // olhos
        img.setColor(new Color(20, 30, 60));
        img.fillOval(11, 10, 5, 7);
        img.fillOval(20, 10, 5, 7);
        return img;
    }
}
