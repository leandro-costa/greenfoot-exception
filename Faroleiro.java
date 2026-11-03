import greenfoot.*;

/**
 * O jogador: o Faroleiro, controlado pelas setas do teclado.
 *
 * - Anda pela ilha com as setas (moverPosicao herdado do molde).
 * - A cada intervalo, patrulha o farol: anda uma casa para a direita,
 *   gasta 10 de energia e ganha 1 ponto.
 * - Ao encostar em um recurso trazido pela tempestade, opera o recurso.
 *
 * Mudanças do Desafio 20:
 * - operarRecurso(null) lança IllegalArgumentException.
 * - Com energia 0, operarRecurso e patrulhar lançam
 *   FaroleiroSemEnergiaException (o jogo termina de verdade).
 */
public class Faroleiro extends HabitanteDaIlha
{
    private static final int PASSO = 3;
    private static final int INTERVALO_DE_PATRULHA = 150;
    private static final String SEM_ENERGIA = "O farol está sem energia suficiente para continuar.";

    private int pontuacao;
    private int relogio = 0;

    public Faroleiro(String id, int posX, int posY, int energia, int pontuacao)
    {
        super(id, posX, posY, energia);
        this.pontuacao = pontuacao;
        setImage(desenhar());
    }

    public int getPontuacao()
    {
        return pontuacao;
    }

    @Override
    public void patrulhar() throws FaroleiroSemEnergiaException
    {
        if (getEnergia() == 0) {
            throw new FaroleiroSemEnergiaException(SEM_ENERGIA);
        }
        moverPosicao(1, 0);
        setEnergia(getEnergia() - 10);
        IO.println(getId() + " patrulha o farol");
    }

    public void operarRecurso(RecursoDoFarol recurso) throws FaroleiroSemEnergiaException
    {
        if (recurso == null) {
            throw new IllegalArgumentException("Recurso ausente para operar.");
        }
        if (getEnergia() == 0) {
            // a energia NÃO muda neste caso
            throw new FaroleiroSemEnergiaException(SEM_ENERGIA);
        }
        setEnergia(getEnergia() + recurso.usar());
    }

    // ------------------------------------------------------------------
    // Greenfoot: o act() é quem chama os métodos do jogo e trata as
    // exceções (o papel que o Main tem na versão de console).
    // ------------------------------------------------------------------

    @Override
    public void act()
    {
        if (!emJogo()) {
            return;
        }
        andarComTeclado();
        coletarRecurso();
        relogio++;
        if (relogio % INTERVALO_DE_PATRULHA == 0) {
            patrulharNaTempestade();
        }
    }

    private void andarComTeclado()
    {
        if (Greenfoot.isKeyDown("left")) {
            moverPosicao(-PASSO, 0);
        }
        if (Greenfoot.isKeyDown("right")) {
            moverPosicao(PASSO, 0);
        }
        if (Greenfoot.isKeyDown("up") && posY - PASSO > IlhaDasTempestades.MAR_TOPO) {
            moverPosicao(0, -PASSO);
        }
        if (Greenfoot.isKeyDown("down") && posY + PASSO < IlhaDasTempestades.MAR_FUNDO) {
            moverPosicao(0, PASSO);
        }
    }

    private void patrulharNaTempestade()
    {
        try {
            patrulhar();
            pontuacao++;
        } catch (FaroleiroSemEnergiaException e) {
            ilha().encerrar(e.getMessage(), false);
        }
    }

    private void coletarRecurso()
    {
        ItemDaTempestade item = (ItemDaTempestade) getOneIntersectingObject(ItemDaTempestade.class);
        if (item == null) {
            return;
        }
        int antes = getEnergia();
        try {
            operarRecurso(item);
            IO.println(getId() + " operou " + item.getNome() + ": energia " + antes + " -> " + getEnergia());
        } catch (FaroleiroSemEnergiaException e) {
            ilha().encerrar(e.getMessage(), false);
        } finally {
            // operado ou recusado, o recurso some do mar
            getWorld().removeObject(item);
        }
    }

    private static GreenfootImage desenhar()
    {
        GreenfootImage img = new GreenfootImage(30, 42);
        // capa de chuva amarela
        img.setColor(new Color(240, 200, 30));
        img.fillPolygon(new int[] {15, 3, 27}, new int[] {12, 40, 40}, 3);
        // rosto
        img.setColor(new Color(245, 210, 170));
        img.fillOval(9, 8, 12, 12);
        // chapéu
        img.setColor(new Color(200, 160, 20));
        img.fillRect(5, 6, 20, 4);
        img.fillRect(9, 1, 12, 6);
        // lanterna
        img.setColor(new Color(255, 250, 180));
        img.fillOval(22, 22, 7, 7);
        img.setColor(Color.BLACK);
        img.drawOval(22, 22, 7, 7);
        return img;
    }
}
