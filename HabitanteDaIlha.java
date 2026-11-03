import greenfoot.*;

/**
 * O molde de todos os habitantes da Ilha das Tempestades.
 *
 * Assim como a classe Decapoda do tutorial do Greenfoot reúne o que é
 * comum a Crab e Lobster, HabitanteDaIlha reúne o que é comum a
 * Faroleiro, Contrabandista e EspectroDaNevoa. Ela estende Actor para
 * poder ser colocada no mundo e é abstrata: nunca aparece sozinha.
 *
 * Mudança do Desafio 20: o construtor não "corrige em silêncio" uma
 * energia inválida. Fora de 1..100, lança IllegalArgumentException.
 */
public abstract class HabitanteDaIlha extends Actor
{
    private final String id;
    protected int posX;
    protected int posY;
    private int energia;

    // direção usada por quem vaga pela ilha (reaproveitada pelas filhas)
    private int direcaoX = 1;
    private int direcaoY = 0;

    public HabitanteDaIlha(String id, int posX, int posY, int energia)
    {
        if (energia < 1 || energia > 100) {
            // IllegalArgumentException é NÃO verificada (RuntimeException):
            // por isso o construtor não precisa declarar throws.
            throw new IllegalArgumentException("Energia inválida para " + id + ": " + energia);
        }
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        setEnergia(energia);
    }

    public String getId()
    {
        return id;
    }

    public int getEnergia()
    {
        return energia;
    }

    /**
     * Regra de jogo (continua valendo durante a partida):
     * acima de 100 fixa em 100; chegando a 0 ou menos, fixa em 0 e
     * dispara o gameOver().
     */
    public void setEnergia(int energia)
    {
        if (energia > 100) {
            this.energia = 100;
        } else if (energia <= 0) {
            this.energia = 0;
            gameOver();
        } else {
            this.energia = energia;
        }
    }

    /**
     * Desloca o habitante. Se ele estiver no mundo, a posição na tela
     * acompanha posX/posY (o Greenfoot não deixa sair da borda, então
     * sincronizamos de volta com getX()/getY()).
     */
    protected void moverPosicao(int dx, int dy)
    {
        this.posX = this.posX + dx;
        this.posY = this.posY + dy;
        if (getWorld() != null) {
            setLocation(posX, posY);
            this.posX = getX();
            this.posY = getY();
        }
    }

    /** Ao entrar no mundo, a posição do objeto passa a ser a da tela. */
    @Override
    protected void addedToWorld(World mundo)
    {
        this.posX = getX();
        this.posY = getY();
    }

    protected void gameOver()
    {
        IO.println(id + ": fim de jogo!");
    }

    /**
     * Desafio extra, opção (a): o método abstrato declara throws.
     * Quem sobrescreve pode lançar MENOS exceções do que o molde, então
     * Contrabandista e EspectroDaNevoa não precisam repetir o throws.
     */
    public abstract void patrulhar() throws FaroleiroSemEnergiaException;

    // ------------------------------------------------------------------
    // Comportamentos reaproveitados pelas filhas (mecânica de herança)
    // ------------------------------------------------------------------

    /**
     * Vaga pela ilha mudando de direção ao acaso, como o inimigo
     * aleatório do tutorial, e volta ao bater nas bordas do mar.
     */
    protected void vagar(int velocidade, int chanceDeVirar)
    {
        if (Greenfoot.getRandomNumber(100) < chanceDeVirar) {
            sortearDirecao();
        }
        int proximoX = posX + direcaoX * velocidade;
        int proximoY = posY + direcaoY * velocidade;
        if (proximoX <= IlhaDasTempestades.MAR_ESQUERDA || proximoX >= IlhaDasTempestades.MAR_DIREITA) {
            direcaoX = -direcaoX;
        }
        if (proximoY <= IlhaDasTempestades.MAR_TOPO || proximoY >= IlhaDasTempestades.MAR_FUNDO) {
            direcaoY = -direcaoY;
        }
        moverPosicao(direcaoX * velocidade, direcaoY * velocidade);
    }

    private void sortearDirecao()
    {
        direcaoX = Greenfoot.getRandomNumber(3) - 1;
        direcaoY = Greenfoot.getRandomNumber(3) - 1;
        if (direcaoX == 0 && direcaoY == 0) {
            direcaoX = 1;
        }
    }

    /** O Faroleiro que está encostando neste habitante (ou null). */
    protected Faroleiro faroleiroAoAlcance()
    {
        return (Faroleiro) getOneIntersectingObject(Faroleiro.class);
    }

    /** A ilha em que o habitante está (ou null, fora do mundo). */
    protected IlhaDasTempestades ilha()
    {
        return (IlhaDasTempestades) getWorld();
    }

    /** true se o habitante está no mundo e a partida ainda não acabou. */
    protected boolean emJogo()
    {
        return ilha() != null && !ilha().isEncerrado();
    }
}
